package org.itkk.udf.api.rbac.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.cache.Cache;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.common.dto.UserDto;
import org.itkk.udf.api.common.service.IUserService;
import org.itkk.udf.api.rbac.RbacConstant;
import org.itkk.udf.api.rbac.dto.ChangePswdDto;
import org.itkk.udf.api.rbac.dto.LoginDto;
import org.itkk.udf.api.rbac.dto.RegisteredDto;
import org.itkk.udf.api.rbac.dto.UserSaveDto;
import org.itkk.udf.api.rbac.entity.UserEntity;
import org.itkk.udf.api.rbac.repository.IUserRepository;
import org.itkk.udf.starter.cache.db.dto.DbCacheDto;
import org.itkk.udf.starter.cache.db.service.DbCacheService;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.core.id.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    /**
     * cache
     */
    @Autowired
    private Cache<String, Object> cache;

    /**
     * iUserRepository
     */
    @Autowired
    private IUserRepository iUserRepository;

    /**
     * idWorker
     */
    @Autowired
    private IdWorker idWorker;

    /**
     * dbCacheService
     */
    @Autowired
    private DbCacheService dbCacheService;

    /**
     * 登陆
     *
     * @param loginDto loginDto
     * @return String
     */
    public String login(LoginDto loginDto) {
        //获得用户信息
        UserDto userDto = this.info(loginDto.getUserId());
        //校验
        if (userDto == null) {
            throw new ParameterValidException("用户不存在");
        }
        //校验密码
        this.checkPswd(userDto.getId(), loginDto.getPswd());
        //获得token
        String token = UUID.randomUUID().toString();
        //放入缓存
        dbCacheService.set(token, userDto.getUserId(), CommonConstant.TOKEN_CACHE_TTL);
        //返回token
        return token;
    }

    /**
     * 注册
     *
     * @param registeredDto registeredDto
     * @return String
     */
    @Transactional
    public String registered(RegisteredDto registeredDto) {
        //校验
        if (registeredDto == null) {
            throw new ParameterValidException("注册信息为空");
        }
        if (!registeredDto.getPswd().equals(registeredDto.getPswdConfim())) {
            throw new ParameterValidException("两次密码输入不一致");
        }
        if (iUserRepository.selectCount(new QueryWrapper<UserEntity>().lambda().eq(UserEntity::getUserId, registeredDto.getUserId())) > 0) {
            throw new ParameterValidException("用户名已存在");
        }
        //构造实体
        UserEntity userEntity = new UserEntity();
        userEntity.setId(String.valueOf(idWorker.nextId()));
        userEntity.setUserId(registeredDto.getUserId());
        userEntity.setUserPswd(DigestUtils.md5Hex(registeredDto.getPswd()));
        userEntity.setNickName(registeredDto.getUserId());
        userEntity.setAvatarFileId(null);
        userEntity.setStatus(RbacConstant.USER_STATUS.STATUS_1.value());
        userEntity.setCreateBy(registeredDto.getUserId());
        userEntity.setCreateDate(new Date());
        userEntity.setUpdateBy(registeredDto.getUserId());
        userEntity.setUpdateDate(new Date());
        //定义锁
        final String key = RbacConstant.USER_KEY_PREFIX + registeredDto.getUserId();
        //获取锁
        if (dbCacheService.setNx(key, CoreUtil.getTraceId(), CommonConstant.LOCK_EXPAIR)) {
            try {
                //新增
                iUserRepository.insert(userEntity);
            } finally {
                //释放锁
                dbCacheService.delete(key);
            }
        } else {
            //获取锁失败
            throw new ParameterValidException(CommonConstant.DEF_LOCK_FAIL_MSG);
        }
        //登陆 & 返回
        return this.login(new LoginDto().setUserId(registeredDto.getUserId()).setPswd(registeredDto.getPswd()));
    }

    /**
     * 登出
     *
     * @param token token
     */
    public void logout(String token) {
        //判空
        if (StringUtils.isNotBlank(token)) {
            //删除缓存
            dbCacheService.delete(token);
        }
    }

    /**
     * 更新用户密码
     *
     * @param changePswdDto changePswdDto
     * @param userId        userId
     */
    @Transactional
    public void changePswd(ChangePswdDto changePswdDto, String userId) {
        //获得用户信息
        UserDto userDto = this.info(changePswdDto.getUserId());
        //校验
        if (userDto == null) {
            throw new ParameterValidException("用户不存在");
        }
        if (changePswdDto.getPswd().equals(changePswdDto.getPswdConfim())) {
            throw new ParameterValidException("两次密码输入不一致");
        }
        //校验密码
        this.checkPswd(userDto.getId(), changePswdDto.getOriginalPswd());
        //定义锁
        final String key = RbacConstant.USER_KEY_PREFIX + changePswdDto.getUserId();
        //获取锁
        if (dbCacheService.setNx(key, CoreUtil.getTraceId(), CommonConstant.LOCK_EXPAIR)) {
            try {
                //更新信息
                iUserRepository.update(null, new UpdateWrapper<UserEntity>().lambda()
                        .set(UserEntity::getUserPswd, changePswdDto.getPswd())
                        .set(UserEntity::getUpdateBy, userId)
                        .set(UserEntity::getUpdateDate, new Date())
                        .eq(UserEntity::getUserId, changePswdDto.getUserId())
                );
            } finally {
                //释放锁
                dbCacheService.delete(key);
            }
        } else {
            //获取锁失败
            throw new ParameterValidException(CommonConstant.DEF_LOCK_FAIL_MSG);
        }
    }

    /**
     * 用户信息更新
     *
     * @param userSaveDto userSaveDto
     * @param userId      userId
     */
    @Transactional
    public void save(UserSaveDto userSaveDto, String userId) {
        //获得用户信息
        UserDto userDto = this.info(userSaveDto.getUserId());
        //校验
        if (userDto == null) {
            throw new ParameterValidException("用户不存在");
        }
        if (iUserRepository.selectCount(new QueryWrapper<UserEntity>().lambda().eq(UserEntity::getNickName, userSaveDto.getNickName())) > 0) {
            throw new ParameterValidException("昵称已经存在,请重新输入");
        }
        //定义锁
        final String key = RbacConstant.USER_KEY_PREFIX + userSaveDto.getUserId();
        //获取锁
        if (dbCacheService.setNx(key, CoreUtil.getTraceId(), CommonConstant.LOCK_EXPAIR)) {
            try {
                //更新信息
                iUserRepository.update(null, new UpdateWrapper<UserEntity>().lambda()
                        .set(UserEntity::getNickName, userSaveDto.getNickName())
                        .set(UserEntity::getAvatarFileId, userSaveDto.getAvatarFileId())
                        .set(UserEntity::getUpdateBy, userId)
                        .set(UserEntity::getUpdateDate, new Date())
                        .eq(UserEntity::getUserId, userSaveDto.getUserId())
                );
            } finally {
                //释放锁
                dbCacheService.delete(key);
            }
        } else {
            //获取锁失败
            throw new ParameterValidException(CommonConstant.DEF_LOCK_FAIL_MSG);
        }
    }

    @Override
    public UserDto infoByToken(String token) {
        //判空
        if (StringUtils.isNotBlank(token)) {
            //获得token信息
            DbCacheDto dbCacheDto = dbCacheService.getDto(token);
            //判空
            if (dbCacheDto != null) {
                return this.info(token, dbCacheDto.getValue());
            }
        }
        //返回
        return null;
    }

    @Override
    public UserDto info(String userId) {
        //定义返回值
        UserDto userDto = null;
        //获得数据
        UserEntity userEntity = iUserRepository.selectOne(new QueryWrapper<UserEntity>().lambda().eq(UserEntity::getUserId, userId));
        //判空
        if (userEntity != null) {
            //实例化
            userDto = new UserDto();
            //转换
            CoreUtil.copyPropertiesIgnoreNull(userEntity, userDto);
        }
        //返回
        return userDto;
    }

    /**
     * 返回用户信息
     *
     * @param token  token
     * @param userId userId
     * @return UserDto
     */
    private UserDto info(String token, String userId) {
        //构造缓存key
        final String cacheKey = "userDto_" + token;
        //抓取缓存信息
        Object cacheValue = cache.getIfPresent(cacheKey);
        //判空
        if (cacheValue != null) {
            return (UserDto) cacheValue;
        }
        //获得用户信息
        UserDto userDto = this.info(userId);
        //判空 (status = 1 为正常 , 其它为不正常)
        if (userDto != null && Integer.valueOf(1).equals(userDto.getStatus())) {
            //放入缓存
            cache.put(cacheKey, userDto);
            //返回
            return userDto;
        }
        //返回
        return null;
    }

    /**
     * 密码校验
     *
     * @param id   id
     * @param pswd pswd
     */
    private void checkPswd(String id, String pswd) {
        //获得用户信息
        UserEntity userEntity = iUserRepository.selectById(id);
        //校验
        if (userEntity == null) {
            throw new ParameterValidException("用户不存在");
        }
        if (!userEntity.getUserPswd().equals(DigestUtils.md5Hex(pswd))) {
            throw new ParameterValidException("密码不正确");
        }
    }

}
