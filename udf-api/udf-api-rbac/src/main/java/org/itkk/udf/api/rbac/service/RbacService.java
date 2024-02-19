package org.itkk.udf.api.rbac.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.itkk.udf.api.rbac.RbacConstant;
import org.itkk.udf.api.rbac.RbacProperties;
import org.itkk.udf.api.rbac.dto.*;
import org.itkk.udf.api.rbac.entity.RoleEntity;
import org.itkk.udf.api.rbac.entity.RoleFunctionOptionEntity;
import org.itkk.udf.api.rbac.entity.UserRoleEntity;
import org.itkk.udf.api.rbac.repository.IRoleFunctionOptionRepository;
import org.itkk.udf.api.rbac.repository.IRoleRepository;
import org.itkk.udf.api.rbac.repository.IUserRoleRepository;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RbacService {

    /**
     * rbacProperties
     */
    @Autowired
    private RbacProperties rbacProperties;

    /**
     * iRoleFunctionOptionRepository
     */
    @Autowired
    private IRoleFunctionOptionRepository iRoleFunctionOptionRepository;

    /**
     * iRoleRepository
     */
    @Autowired
    private IRoleRepository iRoleRepository;

    /**
     * iUserRoleRepository
     */
    @Autowired
    private IUserRoleRepository iUserRoleRepository;

    /**
     * cache
     */
    @Autowired
    private Cache<String, Object> cache;

    /**
     * 获得用户菜单
     *
     * @param token  token
     * @param userId userId
     * @return List<MenuDto>
     */
    public List<MenuDto> loadMenu(String token, String userId) {
        return this.getAndRefReshbuildMenuFunctionOptionDtoCache(token, userId).getMenuDtoList();
    }

    /**
     * 获得角色菜单
     *
     * @param roleCode roleCode
     * @return List<MenuDto>
     */
    public List<MenuDto> loadRoleMenu(String roleCode) {
        Map<String, RbacRoleDto> rbacRoleDtoMap = new HashMap<>();
        this.buildUserRoleFunctionOption(rbacRoleDtoMap, iRoleRepository.selectBatchIds(Lists.newArrayList(roleCode)));
        return this.buildMenuFunctionOption(rbacRoleDtoMap).getMenuDtoList();
    }

    /**
     * 获得所有菜单
     *
     * @return List<MenuDto>
     */
    public List<MenuDto> loadAllMenu() {
        //定义返回值
        List<MenuDto> rv = new ArrayList<>();
        //获得root菜单
        List<Map.Entry<String, RbacMenuDto>> rootMenuList = rbacProperties.getMenu().entrySet().stream().filter(entry -> RbacConstant.ROOT_MENU_CODE.equals(entry.getValue().getParentCode())).collect(Collectors.toList());
        //判空
        if (CollectionUtils.isNotEmpty(rootMenuList)) {
            //遍历
            rootMenuList.forEach(entry -> {
                //构造菜单
                MenuDto menuDto = new MenuDto();
                menuDto.setCode(entry.getKey());
                menuDto.setName(entry.getValue().getName());
                menuDto.setClassName(entry.getValue().getClassName());
                menuDto.setOrder(entry.getValue().getOrder());
                //递归找寻子项
                loadAllMenu(menuDto);
                //放入结果集
                rv.add(menuDto);
            });
            //排序
            rv.sort(Comparator.comparing(MenuDto::getOrder));
        }
        //返回
        return rv;
    }

    /**
     * 加载所有菜单数据
     *
     * @param parentMenuDto parentMenuDto
     */
    private void loadAllMenu(MenuDto parentMenuDto) {
        //处理子菜单
        {
            //获得子菜单
            List<Map.Entry<String, RbacMenuDto>> menuDtoList = rbacProperties.getMenu().entrySet().stream().filter(entry -> parentMenuDto.getCode().equals(entry.getValue().getParentCode())).collect(Collectors.toList());
            //判空
            if (CollectionUtils.isNotEmpty(menuDtoList)) {
                //初始化父菜单的子菜单集合
                parentMenuDto.setChildMenus(new ArrayList<>());
                //遍历
                menuDtoList.forEach(entry -> {
                    //构造菜单
                    MenuDto menuDto = new MenuDto();
                    menuDto.setCode(entry.getKey());
                    menuDto.setName(entry.getValue().getName());
                    menuDto.setClassName(entry.getValue().getClassName());
                    menuDto.setOrder(entry.getValue().getOrder());
                    //递归找寻子项
                    loadAllMenu(menuDto);
                    //放入结果集
                    parentMenuDto.getChildMenus().add(menuDto);
                });
                //排序
                parentMenuDto.getChildMenus().sort(Comparator.comparing(MenuDto::getOrder));
            }
        }
        //处理子功能
        {
            //获得菜单关联的功能
            List<String> functionCodeList = rbacProperties.getMenu().get(parentMenuDto.getCode()).getFunctions();
            //判空
            if (CollectionUtils.isNotEmpty(functionCodeList)) {
                //初始化父菜单的子功能集合
                parentMenuDto.setChildFunctions(new ArrayList<>());
                //遍历
                for (String functionCode : functionCodeList) {
                    //获得功能
                    RbacFunctionDto rbacFunctionDto = rbacProperties.getFunction().get(functionCode);
                    //构造功能
                    FunctionDto functionDto = new FunctionDto();
                    functionDto.setCode(functionCode);
                    functionDto.setName(rbacFunctionDto.getName());
                    functionDto.setOrder(rbacFunctionDto.getOrder());
                    //获得操作
                    if (MapUtils.isNotEmpty(rbacFunctionDto.getOption())) {
                        //初始化功能的操作列表
                        functionDto.setOptions(new ArrayList<>());
                        //遍历
                        rbacFunctionDto.getOption().forEach((key, value) -> {
                            //构造操作
                            OptionDto optionDto = new OptionDto();
                            optionDto.setCode(key);
                            optionDto.setName(value.getName());
                            //添加到功能中
                            functionDto.getOptions().add(optionDto);
                        });
                    }
                    //添加到菜单中
                    parentMenuDto.getChildFunctions().add(functionDto);
                }
                //去重
                parentMenuDto.setChildFunctions(parentMenuDto.getChildFunctions().stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCode()))), ArrayList::new)));
                //排序
                parentMenuDto.getChildFunctions().sort(Comparator.comparing(FunctionDto::getOrder));
            }
        }
    }

    /**
     * api鉴权
     *
     * @param token  token
     * @param userId userId
     * @param uri    uri
     * @param method method
     */
    public void checkApi(String token, String userId, String uri, String method) {
        //获得数据
        Map<String, List<String>> api = this.loadApi(token, userId);
        //判断
        if (MapUtils.isEmpty(api)) {
            log.warn("该用户无任何权限 : traceId -> {} , userId -> {} , URI -> {} , METHOD -> {}", CoreUtil.getTraceId(), userId, uri, method);
            throw new ParameterValidException("无请求权限");
        }
        if (!api.containsKey(uri)) {
            log.warn("无URI权限 : traceId -> {} , userId -> {} , URI -> {} , METHOD -> {}", CoreUtil.getTraceId(), userId, uri, method);
            throw new ParameterValidException("无请求权限");
        }
        if (!api.get(uri).contains(method.toUpperCase())) {
            log.warn("无METHOD权限 : traceId -> {} , userId -> {} , URI -> {} , METHOD -> {}", CoreUtil.getTraceId(), userId, uri, method);
            throw new ParameterValidException("无请求权限");
        }
    }

    /**
     * 获得用户API
     *
     * @param token  token
     * @param userId userId
     * @return Map<String, List < String>>
     */
    private Map<String, List<String>> loadApi(String token, String userId) {
        return this.getAndRefReshbuildMenuFunctionOptionDtoCache(token, userId).getApiList();
    }

    /**
     * 返回和刷新用户的菜单和操作的权限数据缓存
     *
     * @param token  token
     * @param userId userId
     */
    private BuildMenuFunctionOptionDto getAndRefReshbuildMenuFunctionOptionDtoCache(String token, String userId) {
        //构造缓存key
        final String rbacCacheKey = RbacConstant.RBAC_CACHE_KEY_PREFIX + token;
        //从缓存获得数据
        Object cacheValue = cache.getIfPresent(rbacCacheKey);
        //判空
        if (cacheValue != null) {
            return (BuildMenuFunctionOptionDto) cacheValue;
        }
        //获得数据
        BuildMenuFunctionOptionDto buildMenuFunctionOptionDto = buildMenuFunctionOption(userId);
        //放入缓存
        cache.put(rbacCacheKey, buildMenuFunctionOptionDto);
        //返回
        return buildMenuFunctionOptionDto;
    }

    /**
     * 构造用户的菜单和操作的权限数据
     *
     * @param userId userId
     * @return BuildMenuFunctionOptionDto
     */
    private BuildMenuFunctionOptionDto buildMenuFunctionOption(String userId) {
        return this.buildMenuFunctionOption(this.buildUserRoleFunctionOption(userId));
    }

    /**
     * 构造用户的菜单和操作的权限数据
     *
     * @param roleData roleData
     * @return BuildMenuFunctionOptionDto
     */
    private BuildMenuFunctionOptionDto buildMenuFunctionOption(Map<String, RbacRoleDto> roleData) {
        //常量
        final Map<String, RbacFunctionDto> function = new HashMap<>(this.rbacProperties.getFunction());
        final Map<String, RbacMenuDto> menu = new HashMap<>(this.rbacProperties.getMenu());
        final Map<String, RbacApiDto> api = new HashMap<>(this.rbacProperties.getApi());
        //定义返回值
        final BuildMenuFunctionOptionDto rv = new BuildMenuFunctionOptionDto();
        //初始化返回值
        rv.setMenuDtoList(new ArrayList<>());
        rv.setApiList(new HashMap<>());
        //判断role集合不为空
        if (MapUtils.isNotEmpty(roleData)) {
            //定义中间数据结构
            final Map<String, FunctionDto> functionDtoMap = new HashMap<>();
            final Map<String, MenuSimpleDto> menuSimpleDtoMap = new HashMap<>();
            //构造function集合
            {
                //遍历role集合
                roleData.values().forEach(rbacRoleDto -> {
                    //判断角色中的function集合不为空
                    if (MapUtils.isNotEmpty(rbacRoleDto.getFunction())) {
                        //遍历角色中的function集合
                        rbacRoleDto.getFunction().forEach((functionCode, optionCodeList) -> {
                            //判断functionCode存在基础数据中
                            if (function.containsKey(functionCode)) {
                                //获得function基础数据
                                final RbacFunctionDto rbacFunctionDto = function.get(functionCode);
                                //如果中间数据结构中存在此function,则返回,不存在,则新构造一个
                                final FunctionDto functionDto = Optional.ofNullable(functionDtoMap.get(functionCode)).orElse(new FunctionDto().setCode(functionCode).setName(rbacFunctionDto.getName()).setOrder(rbacFunctionDto.getOrder()));
                                //判断function中的optionCode集合不为空
                                if (CollectionUtils.isNotEmpty(optionCodeList)) {
                                    //function中的optionCode集合(只处理存在基础数据里的optionCode)
                                    optionCodeList.stream().filter(item -> rbacFunctionDto.getOption().containsKey(item)).forEach(optionCode -> {
                                        //获得option基础数据
                                        final RbacFunctionOptionDto rbacFunctionOptionDto = rbacFunctionDto.getOption().get(optionCode);
                                        //判断functiuon中的options为空,则初始化
                                        if (functionDto.getOptions() == null) {
                                            functionDto.setOptions(new ArrayList<>());
                                        }
                                        //将option数据装入function中
                                        functionDto.getOptions().add(new OptionDto().setCode(optionCode).setName(rbacFunctionOptionDto.getName()));
                                    });
                                    //将function中的option列表去重
                                    Optional.ofNullable(functionDto.getOptions()).ifPresent(optionDtos -> functionDto.setOptions(functionDto.getOptions().stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCode()))), ArrayList::new))));
                                }
                                //装入中间数据结构
                                functionDtoMap.put(functionCode, functionDto);
                            }
                        });
                    }
                    //判断角色中的api集合不为空
                    if (CollectionUtils.isNotEmpty(rbacRoleDto.getApis())) {
                        //构造api列表
                        buildApiDto(api, rv.getApiList(), rbacRoleDto.getApis());
                    }
                });
            }
            //构造菜单集合
            {
                //遍历menu & 过滤menu中没有配置functions的节点
                menu.entrySet().stream().filter(entry -> CollectionUtils.isNotEmpty(entry.getValue().getFunctions())).forEach(entry -> {
                    //获得menu代码
                    final String menuCode = entry.getKey();
                    //获得menu实体
                    final RbacMenuDto rbacMenuDto = entry.getValue();
                    //如果menu中间数据结构中存在此menu,则返回,不存在,则新构造一个
                    final MenuSimpleDto menuSimpleDto = Optional.ofNullable(menuSimpleDtoMap.get(menuCode)).orElse(new MenuSimpleDto().setCode(menuCode).setName(rbacMenuDto.getName()).setClassName(rbacMenuDto.getClassName()).setOrder(rbacMenuDto.getOrder()).setParentCode(rbacMenuDto.getParentCode()).setChildFunctions(CollectionUtils.isNotEmpty(rbacMenuDto.getFunctions()) ? new ArrayList<>() : null));
                    //遍历menu基础数据中的function集合,并加入中间数据结构的function集合中(只处理在function中间数据结构中存在的数据)
                    rbacMenuDto.getFunctions().stream().filter(item -> functionDtoMap.containsKey(item)).forEach(functionCode -> menuSimpleDto.getChildFunctions().add(functionDtoMap.get(functionCode)));
                    //判断menu的function是否存在数据
                    if (CollectionUtils.isNotEmpty(menuSimpleDto.getChildFunctions())) {
                        //menu中间数据结构中的function集合排序去重
                        menuSimpleDto.setChildFunctions(menuSimpleDto.getChildFunctions().stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCode()))), ArrayList::new)).stream().sorted(Comparator.comparing(FunctionDto::getOrder)).collect(Collectors.toList()));
                        //装入中间数据结构
                        menuSimpleDtoMap.put(menuCode, menuSimpleDto);
                        //找寻menu父菜单
                        buildMenuSimpleDtoMap(menuSimpleDto.getParentCode(), menu, menuSimpleDtoMap);
                    }
                });
            }
            //构造树形菜单集合
            {
                // 抽取menu中间数据结构的value集合 & 过滤 & 排序 & 去重
                List<MenuSimpleDto> menuSimpleDtoList = menuSimpleDtoMap.values().stream()
                        .filter(item -> item.getParentCode().equals(RbacConstant.ROOT_MENU_CODE))
                        .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCode()))), ArrayList::new))
                        .stream()
                        .sorted(Comparator.comparing(MenuSimpleDto::getOrder))
                        .collect(Collectors.toList());
                //判空
                if (CollectionUtils.isNotEmpty(menuSimpleDtoList)) {
                    //遍历
                    menuSimpleDtoList.forEach(menuSimpleDto -> {
                        //构造menu
                        MenuDto item = new MenuDto().setCode(menuSimpleDto.getCode()).setName(menuSimpleDto.getName()).setClassName(menuSimpleDto.getClassName()).setOrder(menuSimpleDto.getOrder()).setChildFunctions(menuSimpleDto.getChildFunctions());
                        //查找menu的子节点
                        buildMenuDto(function, api, rv.getApiList(), item, menuSimpleDtoMap);
                        //填充menu
                        rv.getMenuDtoList().add(item);
                        //构造api列表
                        buildApiDto(function, api, rv.getApiList(), item.getChildFunctions());
                    });
                }
            }
        }
        //返回
        return rv;
    }

    /**
     * 构造api列表
     *
     * @param api         api
     * @param apiList     apiList
     * @param roleApiList roleApiList
     */
    private void buildApiDto(final Map<String, RbacApiDto> api, final Map<String, List<String>> apiList, List<String> roleApiList) {
        //判空
        if (CollectionUtils.isNotEmpty(roleApiList)) {
            //遍历
            roleApiList.forEach(roleApiCole -> {
                //判断
                if (api.containsKey(roleApiCole)) {
                    //获得数据
                    RbacApiDto rbacApiDto = api.get(roleApiCole);
                    //判断uri
                    if (!apiList.containsKey(rbacApiDto.getUri())) {
                        apiList.put(rbacApiDto.getUri(), new ArrayList<>());
                    }
                    //判断method
                    if (!apiList.get(rbacApiDto.getUri()).contains(rbacApiDto.getMethod())) {
                        apiList.get(rbacApiDto.getUri()).add(rbacApiDto.getMethod().toUpperCase());
                    }
                }
            });
        }
    }

    /**
     * 构造api列表
     *
     * @param function        function
     * @param api             api
     * @param apiList         apiList
     * @param functionDtoList functionDtoList
     */
    private void buildApiDto(final Map<String, RbacFunctionDto> function, final Map<String, RbacApiDto> api, final Map<String, List<String>> apiList, final List<FunctionDto> functionDtoList) {
        //判空
        if (CollectionUtils.isNotEmpty(functionDtoList)) {
            //遍历
            functionDtoList.forEach(functionDto -> {
                //判空
                if (CollectionUtils.isNotEmpty(functionDto.getOptions())) {
                    //遍历
                    functionDto.getOptions().forEach(optionDto -> {
                        //判断
                        if (function.containsKey(functionDto.getCode()) && function.get(functionDto.getCode()).getOption().containsKey(optionDto.getCode())) {
                            //获得apiCode集合
                            List<String> apiCodes = function.get(functionDto.getCode()).getOption().get(optionDto.getCode()).getApis();
                            //判空
                            if (CollectionUtils.isNotEmpty(apiCodes)) {
                                //遍历
                                apiCodes.forEach(apiCode -> {
                                    //判断
                                    if (api.containsKey(apiCode)) {
                                        //获得数据
                                        RbacApiDto rbacApiDto = api.get(apiCode);
                                        //判断uri
                                        if (!apiList.containsKey(rbacApiDto.getUri())) {
                                            apiList.put(rbacApiDto.getUri(), new ArrayList<>());
                                        }
                                        //判断method
                                        if (!apiList.get(rbacApiDto.getUri()).contains(rbacApiDto.getMethod())) {
                                            apiList.get(rbacApiDto.getUri()).add(rbacApiDto.getMethod());
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 构造菜单
     *
     * @param function         function
     * @param api              api
     * @param apiList          apiList
     * @param parentMenuDto    parentMenuDto
     * @param menuSimpleDtoMap menuSimpleDtoMap
     */
    private void buildMenuDto(final Map<String, RbacFunctionDto> function, final Map<String, RbacApiDto> api, final Map<String, List<String>> apiList, final MenuDto parentMenuDto, final Map<String, MenuSimpleDto> menuSimpleDtoMap) {
        //获得数据 过滤 & 排序 & 去重
        List<MenuSimpleDto> menuSimpleDtoList = menuSimpleDtoMap.values().stream()
                .filter(item -> item.getParentCode().equals(parentMenuDto.getCode()))
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCode()))), ArrayList::new))
                .stream()
                .sorted(Comparator.comparing(MenuSimpleDto::getOrder))
                .collect(Collectors.toList());
        //判空
        if (CollectionUtils.isNotEmpty(menuSimpleDtoList)) {
            //实例化
            parentMenuDto.setChildMenus(new ArrayList<>());
            //遍历
            menuSimpleDtoList.forEach(menuSimpleDto -> {
                //构造
                MenuDto menuDto = new MenuDto().setCode(menuSimpleDto.getCode()).setName(menuSimpleDto.getName()).setClassName(menuSimpleDto.getClassName()).setOrder(menuSimpleDto.getOrder()).setChildFunctions(menuSimpleDto.getChildFunctions());
                //查找子节点
                buildMenuDto(function, api, apiList, menuDto, menuSimpleDtoMap);
                //填充menu
                parentMenuDto.getChildMenus().add(menuDto);
                //构造api列表
                buildApiDto(function, api, apiList, menuDto.getChildFunctions());
            });
        }
    }

    /**
     * 寻找父菜单
     *
     * @param menuCode         menuCode
     * @param menu             menu
     * @param menuSimpleDtoMap menuSimpleDtoMap
     */
    private void buildMenuSimpleDtoMap(final String menuCode, final Map<String, RbacMenuDto> menu, final Map<String, MenuSimpleDto> menuSimpleDtoMap) {
        //获得数据
        RbacMenuDto rbacMenuDto = menu.get(menuCode);
        //判空
        if (rbacMenuDto != null) {
            //装入
            menuSimpleDtoMap.put(menuCode, Optional.ofNullable(menuSimpleDtoMap.get(menuCode)).orElse(new MenuSimpleDto().setCode(menuCode).setClassName(rbacMenuDto.getClassName()).setName(rbacMenuDto.getName()).setOrder(rbacMenuDto.getOrder()).setParentCode(rbacMenuDto.getParentCode()).setChildFunctions(CollectionUtils.isNotEmpty(rbacMenuDto.getFunctions()) ? new ArrayList<>() : null)));
            //判断不是顶层
            if (!RbacConstant.ROOT_MENU_CODE.equals(rbacMenuDto.getParentCode())) {
                //递归向上
                buildMenuSimpleDtoMap(rbacMenuDto.getParentCode(), menu, menuSimpleDtoMap);
            }
        }
    }

    /**
     * 构造当前用户角色功能操作的数据结构
     *
     * @param userId userId
     * @return Map<String, RbacRoleDto>
     */
    private Map<String, RbacRoleDto> buildUserRoleFunctionOption(String userId) {
        //定义返回值 & 获得公共角色数据
        final Map<String, RbacRoleDto> rv = new HashMap<>(this.rbacProperties.getRolePublic());
        //判断是运维角色,则添加运维角色
        if (CollectionUtils.isNotEmpty(this.rbacProperties.getOpsUsers()) && this.rbacProperties.getOpsUsers().contains(userId)) {
            rv.putAll(this.rbacProperties.getRoleOps());
        }
        //获得用户角色列表
        List<UserRoleEntity> userRoleEntityList = iUserRoleRepository.selectList(new QueryWrapper<UserRoleEntity>().lambda().eq(UserRoleEntity::getUserId, userId));
        List<RoleEntity> roleEntityList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userRoleEntityList)) {
            roleEntityList = iRoleRepository.selectList(new QueryWrapper<RoleEntity>().lambda().eq(RoleEntity::getStatus, RbacConstant.ROLE_STATUS.STATUS_1.value()).in(RoleEntity::getCode, userRoleEntityList.stream().map(UserRoleEntity::getRoleCode).distinct().collect(Collectors.toList())));
        }
        //构造
        this.buildUserRoleFunctionOption(rv, roleEntityList);
        //返回
        return rv;
    }

    /**
     * 构造当前用户角色功能操作的数据结构
     *
     * @param rv             rv
     * @param roleEntityList roleEntityList
     * @return Map<String, RbacRoleDto>
     */
    private void buildUserRoleFunctionOption(Map<String, RbacRoleDto> rv, List<RoleEntity> roleEntityList) {
        //判空
        if (CollectionUtils.isNotEmpty(roleEntityList)) {
            //获得角色功能操作列表
            List<RoleFunctionOptionEntity> roleFunctionOptionEntityList = iRoleFunctionOptionRepository.selectList(new QueryWrapper<RoleFunctionOptionEntity>().lambda().in(RoleFunctionOptionEntity::getRoleCode, roleEntityList.stream().map(RoleEntity::getCode).collect(Collectors.toList())));
            //判空
            if (CollectionUtils.isNotEmpty(roleFunctionOptionEntityList)) {
                //将角色信息转换成map结构
                Map<String, RoleEntity> roleMap = Maps.uniqueIndex(roleEntityList, RoleEntity::getCode);
                //遍历
                roleFunctionOptionEntityList.forEach(roleFunctionOptionEntity -> {
                    //只处理roleCode存在的情况
                    if (roleMap.containsKey(roleFunctionOptionEntity.getRoleCode())) {
                        //角色代码不在结果集
                        if (!rv.containsKey(roleFunctionOptionEntity.getRoleCode())) {
                            rv.put(roleFunctionOptionEntity.getRoleCode(), new RbacRoleDto());
                            rv.get(roleFunctionOptionEntity.getRoleCode()).setName(roleMap.get(roleFunctionOptionEntity.getRoleCode()).getName());
                            rv.get(roleFunctionOptionEntity.getRoleCode()).setFunction(new HashMap<>());
                        }
                        //功能代码不在结果集里
                        if (!rv.get(roleFunctionOptionEntity.getRoleCode()).getFunction().containsKey(roleFunctionOptionEntity.getFunctionCode())) {
                            rv.get(roleFunctionOptionEntity.getRoleCode()).getFunction().put(roleFunctionOptionEntity.getFunctionCode(), new ArrayList<>());
                        }
                        //操作代码不在结果集里
                        if (!rv.get(roleFunctionOptionEntity.getRoleCode()).getFunction().get(roleFunctionOptionEntity.getFunctionCode()).contains(roleFunctionOptionEntity.getOptionCode())) {
                            rv.get(roleFunctionOptionEntity.getRoleCode()).getFunction().get(roleFunctionOptionEntity.getFunctionCode()).add(roleFunctionOptionEntity.getOptionCode());
                        }
                    }
                });
            }
        }
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @Valid
    private static class BuildMenuFunctionOptionDto {
        /**
         * 菜单集合
         */
        private List<MenuDto> menuDtoList;

        /**
         * API集合 ( key : uri , value : httpMethod集合 )
         */
        private Map<String, List<String>> apiList;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @Valid
    private static class MenuSimpleDto implements Serializable {

        /**
         * ID
         */
        private static final long serialVersionUID = -4436379076719077791L;

        /**
         * 代码
         */
        private String code;

        /**
         * 名称
         */
        private String name;

        /**
         * 样式名称
         */
        private String className;

        /**
         * 排序
         */
        private Integer order = 1;

        /**
         * 父代码
         */
        private String parentCode;

        /**
         * 子功能
         */
        private List<FunctionDto> childFunctions;

    }

}
