package org.itkk.udf.aliyun.dysms.web;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.aliyun.dysms.api.DysmsWarpper;
import org.itkk.udf.cache.redis.lock.RedisBasedDistributedLock;
import org.itkk.udf.core.RestResponse;
import org.itkk.udf.core.exception.ParameterValidException;
import org.itkk.udf.core.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * AliuyunDysmsController
 */
@RestController
public class AliuyunDysmsController implements IAliuyunDysmsController {

    /**
     * VERIFICATION_CODE_CACHE_KEY_PREFIX
     */
    private static final String VERIFICATION_CODE_CACHE_KEY_PREFIX = ":VERIFICATION_CODE:";

    /**
     * profiles
     */
    @Value("${spring.profiles.active}")
    private String profiles;

    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * redisBasedDistributedLock
     */
    @Autowired
    private RedisBasedDistributedLock redisBasedDistributedLock;

    /**
     * dysmsWarpper
     */
    @Autowired
    private DysmsWarpper dysmsWarpper;

    /**
     * httpServletRequest
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * objectMapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public RestResponse<Boolean> sendVerificationCode() throws JsonProcessingException, ClientException {
        //常量
        final String signName = "漓淼汽车科技";
        final String templateCode = "SMS_123798312";
        final int num4 = 4;
        final int num5 = 5;
        final int num50 = 50;
        //定义返回值
        boolean result = false;
        //获得手机号
        String phoneNumber = httpServletRequest.getHeader("phoneNumber");
        //判空
        if (StringUtils.isBlank(phoneNumber)) {
            throw new ParameterValidException("手机号不能为空", null);
        }
        //锁定 (同一个手机号50秒只能发送成功一次验证码短消息)
        String lockKey = "VERIFICATION_CODE:" + phoneNumber;
        if (redisBasedDistributedLock.lock(lockKey, num50, TimeUnit.SECONDS, null)) {
            //获得验证码
            String code = IdUtil.genRandomNumber(num4);
            //构造参数
            Map<String, String> param = new HashMap<>();
            param.put("code", code);
            //发送
            result = dysmsWarpper.send(phoneNumber, signName, templateCode, objectMapper.writeValueAsString(param));
            //判断是否成功,成功则缓存code,失败则解锁
            if (result) {
                //放入缓存
                String key = getKey(phoneNumber);
                redisTemplate.opsForValue().set(key, code, num5, TimeUnit.MINUTES);
            } else {
                //解锁
                redisBasedDistributedLock.unlock(lockKey);
            }
        }
        //返回
        return new RestResponse<>(result);
    }

    @Override
    public RestResponse<Boolean> checkVerificationCode() {
        //获得手机号
        String phoneNumber = httpServletRequest.getHeader("phoneNumber");
        //判空
        if (StringUtils.isBlank(phoneNumber)) {
            throw new ParameterValidException("手机号不能为空", null);
        }
        //获得验证码
        String verificationCode = httpServletRequest.getHeader("verificationCode");
        //判空
        if (StringUtils.isBlank(verificationCode)) {
            throw new ParameterValidException("验证码不能为空", null);
        }
        //获得缓存值
        String key = getKey(phoneNumber);
        Object cacheValueObject = redisTemplate.opsForValue().get(key);
        //比对验证
        boolean result = cacheValueObject != null && cacheValueObject.equals(verificationCode);
        //如果校验通过,删除缓存值
        if (result) {
            redisTemplate.delete(key);
        }
        //返回
        return new RestResponse<>(result);
    }

    /**
     * getKey
     *
     * @param phoneNumber phoneNumber
     * @return String
     */
    private String getKey(String phoneNumber) {
        return profiles + VERIFICATION_CODE_CACHE_KEY_PREFIX + phoneNumber;
    }

}
