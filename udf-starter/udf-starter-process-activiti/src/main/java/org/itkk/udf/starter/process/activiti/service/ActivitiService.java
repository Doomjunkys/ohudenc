package org.itkk.udf.starter.process.activiti.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import org.itkk.udf.starter.cache.db.service.DbCacheService;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.process.activiti.ActivitiConstant;
import org.itkk.udf.starter.process.activiti.IActivitiUserInfo;
import org.itkk.udf.starter.process.activiti.customer.CustomerHistoricTaskInstanceQueryImpl;
import org.itkk.udf.starter.process.activiti.customer.CustomerHistoryServiceImpl;
import org.itkk.udf.starter.process.activiti.customer.CustomerTaskQueryImpl;
import org.itkk.udf.starter.process.activiti.customer.CustomerTaskServiceImpl;
import org.itkk.udf.starter.process.activiti.dto.*;
import org.itkk.udf.starter.process.activiti.repository.IActivitRepository;
import org.itkk.udf.starter.process.activiti.util.ActivitiUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class ActivitiService {
    /**
     * 流程引擎
     */
    @Autowired
    private ProcessEngine processEngine;

    /**
     * iActivitiUserInfo
     */
    @Autowired
    private IActivitiUserInfo iActivitiUserInfo;

    /**
     * dbCacheService
     */
    @Autowired
    private DbCacheService dbCacheService;

    /**
     * cache
     */
    @Autowired
    private Cache<String, Object> cache;

    /**
     * iActivitRepository
     */
    @Autowired
    private IActivitRepository iActivitRepository;

    /**
     * internalObjectMapper
     */
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper internalObjectMapper;

    /**
     * 启动流程
     *
     * @param authenticatedUserId     authenticatedUserId
     * @param startProcessInstanceDto startProcessInstanceDto
     * @return 流程实例
     */
    @Transactional
    public ProcessInstanceDto startProcessInstance(String authenticatedUserId, StartProcessInstanceDto startProcessInstanceDto) {
        try {
            //设定流程发起人
            this.processEngine.getIdentityService().setAuthenticatedUserId(authenticatedUserId);
            //businessKey判空
            if (StringUtils.isNotBlank(startProcessInstanceDto.getBusinessKey())) {
                //businessKey判断重复
                long count = this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceBusinessKey(startProcessInstanceDto.getBusinessKey()).count();
                if (count != 0) {
                    throw new ParameterValidException("业务主键" + startProcessInstanceDto.getBusinessKey() + "正在流程中");
                }
            }
            //启动流程
            ProcessInstance processInstance = this.processEngine.getRuntimeService().startProcessInstanceByKey(
                    startProcessInstanceDto.getProcessDefinitionKey(),
                    startProcessInstanceDto.getBusinessKey(),
                    startProcessInstanceDto.getVariables()
            );
            //businessKey判空
            if (StringUtils.isBlank(startProcessInstanceDto.getBusinessKey())) {
                //更新流程实例的businessKey,使用流程实例ID作为businessKey
                this.processEngine.getRuntimeService().updateBusinessKey(processInstance.getProcessInstanceId(), processInstance.getProcessInstanceId());
            }
            //processInstanceName判空
            if (StringUtils.isNotBlank(startProcessInstanceDto.getProcessInstanceName())) {
                this.processEngine.getRuntimeService().setProcessInstanceName(processInstance.getProcessInstanceId(), startProcessInstanceDto.getProcessInstanceName());
            }
            //转换DTO
            ProcessInstanceDto dto = ActivitiUtil.buildDto(processInstance);
            //日志输出
            log.info(CoreUtil.getTraceId() + " start process instance : {}", dto);
            //返回流程实例
            return dto;
        } finally {
            //清除ThreadLocal
            this.processEngine.getIdentityService().setAuthenticatedUserId(null);
        }
    }

    /**
     * 根据业务key获得流程实例
     *
     * @param businessKey businessKey
     * @return ProcessInstanceDto
     */
    public ProcessInstanceDto getProcessInstanceByBusinessKey(String businessKey) {
        ProcessInstance processInstance = this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        if (processInstance != null) {
            return ActivitiUtil.buildDto(processInstance);
        }
        return null;
    }

    ///**
    // * 根据业务key获得流程实例
    // *
    // * @param businessKey businessKey
    // * @return ProcessInstanceDto
    // */
    //public HistoricProcessInstanceDto getHisProcessInstanceByBusinessKey(String businessKey) {
    //    HistoricProcessInstance historicProcessInstance = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
    //    if (historicProcessInstance != null) {
    //        return ActivitiUtil.buildDto(historicProcessInstance, null);
    //    }
    //    return null;
    //}

    /**
     * 删除流程实例
     *
     * @param delProcessInstanceDto delProcessInstanceDto
     */
    public void delProcessInstance(DelProcessInstanceDto delProcessInstanceDto) {
        //判断流程是否存在
        if (this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(delProcessInstanceDto.getProcessInstanceId()).count() > 0) {
            //锁名称
            final String key = ActivitiConstant.LOCAK_PROC_INST_KEY_PREFIX + delProcessInstanceDto.getProcessInstanceId();
            final Long expair = 5 * 60L;
            //尝试锁
            if (dbCacheService.setNx(key, CoreUtil.getTraceId(), expair)) { //锁成功
                try {
                    //处理删除原因
                    if (StringUtils.isNotBlank(delProcessInstanceDto.getDeleteReason())) {
                        final String defDeleteReason = "取消";
                        delProcessInstanceDto.setDeleteReason(defDeleteReason);
                    }
                    //取消流程
                    this.processEngine.getRuntimeService().deleteProcessInstance(delProcessInstanceDto.getProcessInstanceId(), delProcessInstanceDto.getDeleteReason());
                } finally {
                    //锁释放
                    dbCacheService.delete(key);
                }
            } else { //锁失败
                throw new ParameterValidException("流程实例ID:" + delProcessInstanceDto.getProcessInstanceId() + "正在被处理中,请稍后再试");
            }
        }
    }

    /**
     * 根据业务键查询流程实例
     *
     * @param businessKey businessKey
     * @return HistoricProcessInstanceDto
     */
    public HistoricProcessInstanceDto processInstanceByBusinessKey(String businessKey) {
        //定义返回值
        HistoricProcessInstanceDto rv = null;
        //获得流程实例Id
        String processInstanceId = iActivitRepository.processInstanceIdByBusinessKey(businessKey);
        //判空
        if (StringUtils.isNotBlank(processInstanceId)) {
            //定义查询
            HistoricProcessInstanceQuery historicProcessInstanceQuery = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery();
            //定义任务查询条件
            historicProcessInstanceQuery.processInstanceId(processInstanceId);
            historicProcessInstanceQuery.includeProcessVariables();
            //查询
            HistoricProcessInstance historicProcessInstance = historicProcessInstanceQuery.singleResult();
            //判空
            if (historicProcessInstance != null) {
                //转换
                rv = ActivitiUtil.buildDto(historicProcessInstance, null);
            }
        }
        //返回
        return rv;
    }

    /**
     * 根据业务键模糊查询流程列表
     *
     * @param businessKey businessKey
     * @return List<HistoricProcessInstanceDto>
     */
    public List<HistoricProcessInstanceDto> processInstanceListByBusinessKeyLike(String businessKey) {
        //获得流程实例Id
        List<String> processInstanceIdList = iActivitRepository.processInstanceIdByBusinessKeyLike("%" + businessKey + "%");

        if (CollectionUtils.isEmpty(processInstanceIdList)) {
            return new ArrayList<>(0);
        }

        //定义查询
        HistoricProcessInstanceQuery historicProcessInstanceQuery = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery();
        //定义任务查询条件
        historicProcessInstanceQuery.processInstanceIds(new HashSet<>(processInstanceIdList));
        historicProcessInstanceQuery.includeProcessVariables();
        //返回列表(分页)
        List<HistoricProcessInstance> list = historicProcessInstanceQuery.list();
        //dto转换
        List<HistoricProcessInstanceDto> dtoList = null;
        if (CollectionUtils.isNotEmpty(list)) {
            dtoList = new ArrayList<>();
            for (HistoricProcessInstance historicProcessInstance : list) {
                //查询当前活动的任务详情
                List<HistoricTaskInstanceDto> historicTaskInstanceDtos = new ArrayList<>();
                //if (historicProcessInstance.getEndTime() == null) {
                //    List<HistoricTaskInstance> historicTaskInstances = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().unfinished().processInstanceId(historicProcessInstance.getId()).list();
                //    if (CollectionUtils.isNotEmpty(historicTaskInstances)) {
                //        historicTaskInstances.forEach(item -> {
                //            String userName = null;
                //            Long userDeptId = null;
                //            String userDeptName = null;
                //            if (StringUtils.isNotBlank(item.getAssignee())) {
                //                userName = iActivitiUserInfo.getUserName(item.getAssignee());
                //                userDeptId = iActivitiUserInfo.getDeptId(item.getAssignee());
                //                userDeptName = iActivitiUserInfo.getDeptName(item.getAssignee());
                //            }
                //            HistoricTaskInstanceDto dto = ActivitiUtil.buildDto(item, item.getAssignee(), userName, userDeptId, userDeptName);
                //            historicTaskInstanceDtos.add(dto);
                //        });
                //    }
                //}
                //dto转换
                dtoList.add(ActivitiUtil.buildDto(historicProcessInstance, historicTaskInstanceDtos));
            }
        }
        //返回
        return dtoList;
    }

    /**
     * 我发起的流程实例列表
     *
     * @param startedBy                   startedBy
     * @param startProcessInstanceListDto startProcessInstanceListDto
     * @return HistoricProcessInstacePageListDto
     */
    public HistoricProcessInstacePageListDto startProcessInstanceList(String startedBy, StartProcessInstanceListDto startProcessInstanceListDto) {
        //定义查询
        HistoricProcessInstanceQuery historicProcessInstanceQuery = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery();
        //定义任务查询条件
        historicProcessInstanceQuery.startedBy(startedBy);
        //是否结束的流程
        if (startProcessInstanceListDto.getFinished() != null) {
            if (startProcessInstanceListDto.getFinished()) {
                historicProcessInstanceQuery.finished();
            } else {
                historicProcessInstanceQuery.unfinished();
            }
        }
        //流程是否删除
        if (startProcessInstanceListDto.getDeleted() != null) {
            if (startProcessInstanceListDto.getDeleted()) {
                historicProcessInstanceQuery.deleted();
            } else {
                historicProcessInstanceQuery.notDeleted();
            }
        }
        //定义排序
        historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc();
        //查询数量
        long count = historicProcessInstanceQuery.count();
        //返回列表(分页)
        List<HistoricProcessInstance> list = historicProcessInstanceQuery.listPage(getFistResult(startProcessInstanceListDto.getPage(), startProcessInstanceListDto.getRows()), startProcessInstanceListDto.getRows());
        //dto转换
        List<HistoricProcessInstanceDto> dtoList = null;
        if (CollectionUtils.isNotEmpty(list)) {
            dtoList = new ArrayList<>();
            for (HistoricProcessInstance historicProcessInstance : list) {
                //查询当前活动的任务详情
                List<HistoricTaskInstanceDto> historicTaskInstanceDtos = new ArrayList<>();
                if (historicProcessInstance.getEndTime() == null) {
                    List<HistoricTaskInstance> historicTaskInstances = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().unfinished().processInstanceId(historicProcessInstance.getId()).list();
                    if (CollectionUtils.isNotEmpty(historicTaskInstances)) {
                        historicTaskInstances.forEach(item -> {
                            //获得用户信息
                            String userName = null;
                            Long userDeptId = null;
                            String userDeptName = null;
                            if (StringUtils.isNotBlank(item.getAssignee())) {
                                userName = iActivitiUserInfo.getUserName(item.getAssignee());
                                userDeptId = iActivitiUserInfo.getDeptId(item.getAssignee());
                                userDeptName = iActivitiUserInfo.getDeptName(item.getAssignee());
                            }
                            //获得流程实例
                            HistoricProcessInstanceDto historicProcessInstanceDto = this.historicProcess(item.getProcessInstanceId());
                            //获得需要的信息
                            String processDefinitionName = historicProcessInstanceDto.getProcessDefinitionName();
                            String processDefinitionKey = historicProcessInstanceDto.getProcessDefinitionKey();
                            //转换
                            HistoricTaskInstanceDto dto = ActivitiUtil.buildDto(item, processDefinitionKey, processDefinitionName, item.getAssignee(), userName, userDeptId, userDeptName);
                            //添加
                            historicTaskInstanceDtos.add(dto);
                        });
                    }
                }
                //dto转换
                dtoList.add(ActivitiUtil.buildDto(historicProcessInstance, historicTaskInstanceDtos));
            }
        }
        //返回
        return new HistoricProcessInstacePageListDto().setCount(count).setList(dtoList);
    }

    /**
     * 获得流程的当前任务(取第一个)
     *
     * @param processInstanceId processInstanceId
     * @return HistoricTaskInstanceDto
     */
    public HistoricTaskInstanceDto getFirstProcessCurrentTask(String processInstanceId) {
        List<HistoricTaskInstance> historicTaskInstances = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().unfinished().processInstanceId(processInstanceId).list();
        if (CollectionUtils.isNotEmpty(historicTaskInstances)) {
            //获得第一个
            HistoricTaskInstance item = historicTaskInstances.get(0);
            //获得用户信息
            String userName = null;
            Long userDeptId = null;
            String userDeptName = null;
            if (StringUtils.isNotBlank(item.getAssignee())) {
                userName = iActivitiUserInfo.getUserName(item.getAssignee());
                userDeptId = iActivitiUserInfo.getDeptId(item.getAssignee());
                userDeptName = iActivitiUserInfo.getDeptName(item.getAssignee());
            }
            //获得流程实例
            HistoricProcessInstanceDto historicProcessInstanceDto = this.historicProcess(item.getProcessInstanceId());
            //获得需要的信息
            String processDefinitionName = historicProcessInstanceDto.getProcessDefinitionName();
            String processDefinitionKey = historicProcessInstanceDto.getProcessDefinitionKey();
            //转换 && 返回
            return ActivitiUtil.buildDto(item, processDefinitionKey, processDefinitionName, item.getAssignee(), userName, userDeptId, userDeptName);
        }
        //返回
        return null;
    }

    /**
     * 流程是否被删除
     *
     * @param processInstanceId processInstanceId
     * @return boolean
     */
    public boolean isDelete(String processInstanceId) {
        return this.processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).deleted().count() > 0;
    }

    /**
     * 流程详情(历史)
     *
     * @param processInstanceId processInstanceId
     * @return HistoricProcessInstanceDto
     */
    public HistoricProcessInstanceDto historicProcess(String processInstanceId) {
        ////构造缓存key
        //final String cacheKey = "HistoricProcessInstanceDto_" + processInstanceId;
        ////抓取缓存信息
        //Object cacheValue = cache.getIfPresent(cacheKey);
        ////判空
        //if (cacheValue != null) {
        //    return (HistoricProcessInstanceDto) cacheValue;
        //}
        //获得任务
        HistoricProcessInstance historicProcessInstance = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();
        //判空
        if (historicProcessInstance == null) {
            throw new ParameterValidException("流程实例ID:" + processInstanceId + "不存在");
        }
        ////缓存
        //cache.put(cacheKey, dto);
        //转换 & 返回
        return ActivitiUtil.buildDto(historicProcessInstance, null);
    }

    /**
     * 已处理任务列表
     *
     * @param assignee         assignee
     * @param processedListDto processedListDto
     * @return HisTaskPageListDto
     */
    public HistoricTaskPageListDto processedList(String assignee, ProcessedListDto processedListDto) {
        //定义查询
        CustomerHistoricTaskInstanceQueryImpl query = ((CustomerHistoryServiceImpl) this.processEngine.getHistoryService()).createCustomerHistoricTaskInstanceQuery();
        //定义任务查询条件
        if (StringUtils.isNotBlank(processedListDto.getProcessDefinitionName())) {
            query.processDefinitionNameLike("%" + processedListDto.getProcessDefinitionName() + "%");
        }
        if (CollectionUtils.isNotEmpty(processedListDto.getProcessDefinitionKeyList())) {
            query.processDefinitionKeyIn(processedListDto.getProcessDefinitionKeyList());
        }
        if (BooleanUtils.isTrue(processedListDto.getIncludeProcessVariables())) {
            query.includeProcessVariables();
        }
        query.taskAssignee(assignee);
        query.finished();
        //定义排序
        query.orderByTaskCreateTime();
        query.desc();
        //查询任务数量
        long count = query.count();
        //返回待处理任务列表(分页)
        List<HistoricTaskInstance> list = query.listPage(getFistResult(processedListDto.getPage(), processedListDto.getRows()), processedListDto.getRows());
        //dto转换
        List<HistoricTaskInstanceDto> dtoList = null;
        if (CollectionUtils.isNotEmpty(list)) {
            dtoList = new ArrayList<>();
            for (HistoricTaskInstance historicTaskInstance : list) {
                //获得流程实例
                HistoricProcessInstanceDto historicProcessInstanceDto = this.historicProcess(historicTaskInstance.getProcessInstanceId());
                //获得需要的信息
                String processDefinitionName = historicProcessInstanceDto.getName();
                String processDefinitionKey = historicProcessInstanceDto.getProcessDefinitionKey();
                Date processInstanceStartTime = historicProcessInstanceDto.getStartTime();
                String startUserId = historicProcessInstanceDto.getStartUserId();
                String userName = iActivitiUserInfo.getUserName(startUserId);
                Long userDeptId = iActivitiUserInfo.getDeptId(startUserId);
                String userDeptName = iActivitiUserInfo.getDeptName(startUserId);
                //dto转换
                dtoList.add(ActivitiUtil.buildDto(historicTaskInstance, processDefinitionKey, processDefinitionName, startUserId, userName, userDeptId, userDeptName));
            }
        }
        //返回
        return new HistoricTaskPageListDto().setCount(count).setList(dtoList);
    }

    /**
     * 待处理任务列表
     *
     * @param assignee       assignee
     * @param pendingListDto pendingListDto
     * @return TaskPageListDto
     */
    public TaskPageListDto pendingList(String assignee, PendingListDto pendingListDto) {
        //定义任务查询
        CustomerTaskQueryImpl toClaimQuery = ((CustomerTaskServiceImpl) this.processEngine.getTaskService()).createCustomerTaskQuery();
        //排除不显示的task
        if (CollectionUtils.isNotEmpty(pendingListDto.getTaskDefinitionKeyNotInList())) {
            toClaimQuery.taskDefinitionKeyNotInList(pendingListDto.getTaskDefinitionKeyNotInList());
        }
        //定义任务查询条件
        if (StringUtils.isNotBlank(pendingListDto.getProcessDefinitionName())) {
            toClaimQuery.processDefinitionNameLike("%" + pendingListDto.getProcessDefinitionName() + "%");
        }
        toClaimQuery.taskAssignee(assignee);
        toClaimQuery.active();
        //定义排序
        toClaimQuery.orderByTaskCreateTime();
        toClaimQuery.desc();
        //查询任务数量
        long count = toClaimQuery.count();
        //返回待处理任务列表(分页)
        List<Task> list = toClaimQuery.listPage(getFistResult(pendingListDto.getPage(), pendingListDto.getRows()), pendingListDto.getRows());
        //dto转换
        List<TaskDto> dtoList = null;
        if (CollectionUtils.isNotEmpty(list)) {
            dtoList = new ArrayList<>();
            for (Task task : list) {
                //获得流程实例
                ProcessInstanceDto processInstance = this.getProcessInstanceDto(task.getProcessInstanceId());
                //获得需要的信息
                String processDefinitionName = StringUtils.isNotBlank(processInstance.getName()) ? processInstance.getName() : processInstance.getProcessDefinitionName();
                String processDefinitionKey = processInstance.getProcessDefinitionKey();
                Date processInstanceStartTime = processInstance.getStartTime();
                String startUserId = processInstance.getStartUserId();
                String startUserName = iActivitiUserInfo.getUserName(startUserId);
                Long startUserDeptId = iActivitiUserInfo.getDeptId(startUserId);
                String startUserDeptName = iActivitiUserInfo.getDeptName(startUserId);
                //dto转换
                dtoList.add(ActivitiUtil.buildDto(processDefinitionKey, processDefinitionName, processInstanceStartTime, startUserId, startUserName, startUserDeptId, startUserDeptName, task));
            }
        }
        //返回
        return new TaskPageListDto().setCount(count).setList(dtoList);
    }

    /**
     * 任务搜索
     */
    public List<Task> taskSearch(String assignee, TaskSearchDto taskSearchDto) {
        //定义任务查询
        TaskQuery toClaimQuery = this.processEngine.getTaskService().createTaskQuery();
        //定义任务查询条件

        if (StringUtils.isNotBlank(taskSearchDto.getProcessInstanceBusinessKey())) {
            toClaimQuery.processInstanceBusinessKey(taskSearchDto.getProcessInstanceBusinessKey());
        }

        if (StringUtils.isNotBlank(taskSearchDto.getProcessDefinitionKey())) {
            toClaimQuery.processDefinitionKey(taskSearchDto.getProcessDefinitionKey());
        }

        if (StringUtils.isNotBlank(taskSearchDto.getTaskDefinitionKey())) {
            toClaimQuery.taskDefinitionKey(taskSearchDto.getTaskDefinitionKey());
        }

        if (StringUtils.isNotEmpty(assignee)) {
            toClaimQuery.taskAssignee(assignee);
        }

        toClaimQuery.active();
        //定义排序
        //toClaimQuery.orderByTaskCreateTime();
        //toClaimQuery.desc();

        //返回
        return toClaimQuery.list();
    }

    /**
     * 所有运行中的任务列表
     *
     * @param allRuntimeTaskListDto allRuntimeTaskListDto
     * @return TaskPageListDto
     */
    public TaskPageListDto allRuntimeTaskList(AllRuntimeTaskListDto allRuntimeTaskListDto) {
        //定义任务查询
        TaskQuery toClaimQuery = this.processEngine.getTaskService().createTaskQuery();
        //定义任务查询条件
        if (StringUtils.isNotBlank(allRuntimeTaskListDto.getProcessDefinitionName())) {
            toClaimQuery.processDefinitionNameLike("%" + allRuntimeTaskListDto.getProcessDefinitionName() + "%");
        }
        if (StringUtils.isNotBlank(allRuntimeTaskListDto.getProcessInstanceId())) {
            toClaimQuery.processInstanceId(allRuntimeTaskListDto.getProcessInstanceId());
        }
        if (StringUtils.isNotBlank(allRuntimeTaskListDto.getTaskId())) {
            toClaimQuery.taskId(allRuntimeTaskListDto.getTaskId());
        }
        toClaimQuery.active();
        //定义排序
        toClaimQuery.orderByTaskCreateTime();
        toClaimQuery.desc();
        //查询任务数量
        long count = toClaimQuery.count();
        //返回待处理任务列表(分页)
        List<Task> list = toClaimQuery.listPage(getFistResult(allRuntimeTaskListDto.getPage(), allRuntimeTaskListDto.getRows()), allRuntimeTaskListDto.getRows());
        //dto转换
        List<TaskDto> dtoList = null;
        if (CollectionUtils.isNotEmpty(list)) {
            dtoList = new ArrayList<>();
            for (Task task : list) {
                //获得流程实例
                ProcessInstanceDto processInstance = this.getProcessInstanceDto(task.getProcessInstanceId());
                //获得需要的信息
                String processDefinitionName = StringUtils.isNotBlank(processInstance.getName()) ? processInstance.getName() : processInstance.getProcessDefinitionName();
                String processDefinitionKey = processInstance.getProcessDefinitionKey();
                Date processInstanceStartTime = processInstance.getStartTime();
                String startUserId = processInstance.getStartUserId();
                String startUserName = iActivitiUserInfo.getUserName(startUserId);
                Long startUserDeptId = iActivitiUserInfo.getDeptId(startUserId);
                String startUserDeptName = iActivitiUserInfo.getDeptName(startUserId);
                //dto转换
                dtoList.add(ActivitiUtil.buildDto(processDefinitionKey, processDefinitionName, processInstanceStartTime, startUserId, startUserName, startUserDeptId, startUserDeptName, task));
            }
        }
        //返回
        return new TaskPageListDto().setCount(count).setList(dtoList);
    }

    /**
     * 获得任务变量
     *
     * @param taskVariablesDto taskVariablesDto
     * @return Map<String, Object>
     */
    public Map<String, Object> getTaskVariables(TaskVariablesDto taskVariablesDto, String userId) {
        //获得任务
        Task task = this.processEngine.getTaskService().createTaskQuery().taskId(taskVariablesDto.getTaskId()).singleResult();
        //判空
        if (task == null) {
            throw new ParameterValidException("任务ID:" + taskVariablesDto.getTaskId() + "不存在,或已被处理");
        }
        //判断当前登陆人是否是办理人
        if (!userId.equals(task.getAssignee())) {
            throw new ParameterValidException("您目前不是这个任务的办理人");
        }
        //获取 & 返回
        return this.processEngine.getTaskService().getVariables(taskVariablesDto.getTaskId(), taskVariablesDto.getVariableNames());
    }

    /**
     * 获得任务变量(运维)
     *
     * @param dto dto
     * @return List<Map < String, String>>
     */
    public List<Map<String, String>> getTaskVariablesOps(TaskVariablesBatchDto dto) {
        //定义返回值
        final List<Map<String, String>> rs = new ArrayList<>();
        //判空
        if (CollectionUtils.isNotEmpty(dto.getTaskIdList()) || CollectionUtils.isNotEmpty(dto.getVariableNames())) {
            //遍历
            dto.getTaskIdList().forEach(tId -> {
                //去除空格
                final String taskId = tId.trim();
                //实例化明细
                final Map<String, String> item = new HashMap<>();
                //放入taskId;
                item.put("taskId", taskId);
                //遍历
                dto.getVariableNames().forEach(variableName -> {
                    //获得流程变量
                    Object valObj = this.processEngine.getTaskService().getVariable(taskId, variableName);
                    //判空
                    if (valObj == null) {
                        item.put(variableName, null);
                    } else {
                        try {
                            item.put(variableName, internalObjectMapper.writeValueAsString(valObj));
                        } catch (JsonProcessingException e) {
                            item.put(variableName, e.getMessage());
                            log.warn("流程变量json转换错误", e);
                        }
                    }
                });
                //放入结果集
                rs.add(item);
            });
        }
        //返回
        return rs;
    }


    /**
     * 任务详情
     *
     * @param taskId taskId
     * @return TaskDto
     */
    public TaskDto pendingTask(String taskId, String userId) {
        //获得任务
        Task task = this.processEngine.getTaskService().createTaskQuery()
                .taskId(taskId)
                .includeProcessVariables()
                .includeTaskLocalVariables()
                .singleResult();
        //判空
        if (task == null) {
            throw new ParameterValidException("任务ID:" + taskId + "不存在,或已被处理");
        }
        //判断当前登陆人是否是办理人
        if (!userId.equals(task.getAssignee())) {
            throw new ParameterValidException("您目前不是这个任务的办理人");
        }
        //获得流程实例
        ProcessInstanceDto processInstance = this.getProcessInstanceDto(task.getProcessInstanceId());
        //获得需要的信息
        String processDefinitionName = StringUtils.isNotBlank(processInstance.getName()) ? processInstance.getName() : processInstance.getProcessDefinitionName();
        String processDefinitionKey = processInstance.getProcessDefinitionKey();
        Date processInstanceStartTime = processInstance.getStartTime();
        String startUserId = processInstance.getStartUserId();
        String startUserName = iActivitiUserInfo.getUserName(startUserId);
        Long startUserDeptId = iActivitiUserInfo.getDeptId(startUserId);
        String startUserDeptName = iActivitiUserInfo.getDeptName(startUserId);
        //返回
        return ActivitiUtil.buildDto(processDefinitionKey, processDefinitionName, processInstanceStartTime, startUserId, startUserName, startUserDeptId, startUserDeptName, task);
    }

    /**
     * 获得流程实例
     *
     * @param processInstanceId processInstanceId
     * @return ProcessInstanceDto
     */
    private ProcessInstanceDto getProcessInstanceDto(String processInstanceId) {
        //构造缓存key
        final String cacheKey = "ProcessInstanceDto_" + processInstanceId;
        //抓取缓存信息
        Object cacheValue = cache.getIfPresent(cacheKey);
        //判空
        if (cacheValue != null) {
            return (ProcessInstanceDto) cacheValue;
        }
        //获得流程实例
        ProcessInstanceDto dto = ActivitiUtil.buildDto(this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult());
        //放入缓存
        cache.put(cacheKey, dto);
        //返回
        return dto;
    }

    /**
     * 返回流程备注信息
     *
     * @param processInstanceId processInstanceId
     * @return List<CommentDto>
     */
    public List<CommentDto> getTaskCommentList(String processInstanceId) {
        //获得流程备注
        List<Comment> commentList = this.processEngine.getTaskService().getProcessInstanceComments(processInstanceId);
        //dto转换
        List<CommentDto> dtoList = null;
        if (CollectionUtils.isNotEmpty(commentList)) {
            dtoList = new ArrayList<>();
            for (Comment comment : commentList) {
                //获得必要信息
                HistoricTaskInstanceDto historicTaskInstanceDto = getHistoricTaskInstanceDto(comment.getTaskId());
                String taskDefinitionName = historicTaskInstanceDto.getName();
                String userId = historicTaskInstanceDto.getAssignee();
                String userName = iActivitiUserInfo.getUserName(userId);
                Long userDeptId = iActivitiUserInfo.getDeptId(userId);
                String userDeptName = iActivitiUserInfo.getDeptName(userId);
                //填充
                dtoList.add(ActivitiUtil.buildDto(taskDefinitionName, userId, userName, userDeptId, userDeptName, comment));
            }
        }
        //返回
        return dtoList;
    }

    /**
     * 获取当前任务的评论信息
     */
    public Comment getTaskComment(String processInstanceId, String taskId) {
        //获得流程备注
        List<Comment> commentList = this.processEngine.getTaskService().getProcessInstanceComments(processInstanceId);

        if (CollectionUtils.isNotEmpty(commentList)) {
            for (Comment comment : commentList) {
                if (comment.getTaskId().equals(taskId)) {
                    return comment;
                }
            }
        }

        return null;
    }

    /**
     * 获得任务历史的任务
     *
     * @param taskId taskId
     * @return HistoricTaskInstanceDto
     */
    private HistoricTaskInstanceDto getHistoricTaskInstanceDto(String taskId) {
        //构造缓存key
        final String cacheKey = "HistoricTaskInstanceDto_" + taskId;
        //抓取缓存信息
        Object cacheValue = cache.getIfPresent(cacheKey);
        //判空
        if (cacheValue != null) {
            return (HistoricTaskInstanceDto) cacheValue;
        }
        //获得数据
        HistoricTaskInstance historicTaskInstance = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        //获得流程实例
        HistoricProcessInstanceDto historicProcessInstanceDto = this.historicProcess(historicTaskInstance.getProcessInstanceId());
        //获得需要的信息
        String processDefinitionName = historicProcessInstanceDto.getProcessDefinitionName();
        String processDefinitionKey = historicProcessInstanceDto.getProcessDefinitionKey();
        Date processInstanceStartTime = historicProcessInstanceDto.getStartTime();
        String startUserId = historicProcessInstanceDto.getStartUserId();
        String userName = iActivitiUserInfo.getUserName(startUserId);
        Long userDeptId = iActivitiUserInfo.getDeptId(startUserId);
        String userDeptName = iActivitiUserInfo.getDeptName(startUserId);
        //转换
        HistoricTaskInstanceDto dto = ActivitiUtil.buildDto(historicTaskInstance, processDefinitionKey, processDefinitionName, startUserId, userName, userDeptId, userDeptName);
        //放入缓存
        cache.put(cacheKey, dto);
        //返回
        return dto;
    }

    /**
     * 设置代办人
     *
     * @param setAssigneeDto setAssigneeDto
     */
    @Transactional
    public void setAssignee(SetAssigneeDto setAssigneeDto, String userId) {
        //判空
        if (CollectionUtils.isNotEmpty(setAssigneeDto.getAssigneeVarName())) {
            //获得任务
            Task task = this.processEngine.getTaskService().createTaskQuery().taskId(setAssigneeDto.getTaskId()).singleResult();
            //判空
            if (task == null) {
                throw new ParameterValidException("任务ID:" + setAssigneeDto.getTaskId() + "不存在,或已被处理");
            }
            //判断当前登陆人是否是办理人
            if (!userId.equals(task.getAssignee())) {
                throw new ParameterValidException("您目前不是这个任务的办理人");
            }
            //锁名称
            final String key = ActivitiConstant.LOCAK_TASK_KEY_PREFIX + setAssigneeDto.getTaskId();
            final Long expair = 5 * 60L;
            //尝试锁
            if (dbCacheService.setNx(key, CoreUtil.getTraceId(), expair)) { //锁成功
                try {
                    for (String assigneeVarName : setAssigneeDto.getAssigneeVarName()) {
                        //获得流程变量
                        Object assigneeObj = this.processEngine.getTaskService().getVariable(setAssigneeDto.getTaskId(), assigneeVarName);
                        //类型判断
                        if (assigneeObj instanceof String) { //单个审批人
                            //更新流程变量
                            this.processEngine.getTaskService().setVariable(setAssigneeDto.getTaskId(), assigneeVarName, setAssigneeDto.getAssignee());
                        } else if (assigneeObj instanceof List) { //会签
                            //强制类型转换
                            List<String> assigneeList;
                            try {
                                assigneeList = (List<String>) assigneeObj;
                            } catch (Exception e) {
                                throw new ParameterValidException("不支持变量" + setAssigneeDto.getAssigneeVarName() + "的代办人更新");
                            }
                            //找到列表中的第一个匹配的值,更新成新的
                            int index = assigneeList.indexOf(setAssigneeDto.getOriginalAssignee());
                            if (index == -1) {
                                throw new ParameterValidException("变量" + setAssigneeDto.getAssigneeVarName() + "中没有找到" + setAssigneeDto.getOriginalAssignee());
                            }
                            assigneeList.set(index, setAssigneeDto.getAssignee());
                            //更新流程变量
                            this.processEngine.getTaskService().setVariable(setAssigneeDto.getTaskId(), assigneeVarName, assigneeList);
                        } else {
                            throw new ParameterValidException("不支持变量" + setAssigneeDto.getAssigneeVarName() + "的代办人更新");
                        }
                        //更新当前任务的代办人
                        this.processEngine.getTaskService().setAssignee(setAssigneeDto.getTaskId(), setAssigneeDto.getAssignee());
                    }
                } finally {
                    //锁释放
                    dbCacheService.delete(key);
                }
                //发送邮件
                if (setAssigneeDto.getSendMail()) {
                    iActivitiUserInfo.sendMail(ActivitiConstant.EMAIL_TYPE.setAssignee, setAssigneeDto.getTaskId(), SetUtils.hashSet(setAssigneeDto.getAssignee()));
                }
            } else { //锁失败
                throw new ParameterValidException("任务ID:" + setAssigneeDto.getTaskId() + "正在被处理中,请稍后再试");
            }
        }
    }

    /**
     * 更新任务
     *
     * @param updateTaskDto updateTaskDto
     */
    @Transactional
    public void updateTask(UpdateTaskDto updateTaskDto) {
        //获得任务
        Task task = this.processEngine.getTaskService().createTaskQuery().taskId(updateTaskDto.getTaskId()).singleResult();
        //判空
        if (task == null) {
            throw new ParameterValidException("任务ID:" + updateTaskDto.getTaskId() + "不存在,或已被处理");
        }
        //锁名称
        final String key = ActivitiConstant.LOCAK_TASK_KEY_PREFIX + updateTaskDto.getTaskId();
        final Long expair = 5 * 60L;
        //尝试锁
        if (dbCacheService.setNx(key, CoreUtil.getTraceId(), expair)) { //锁成功
            try {
                //更新
                this.processEngine.getTaskService().setVariable(updateTaskDto.getTaskId(), updateTaskDto.getName(), updateTaskDto.getValue());
            } finally {
                //锁释放
                dbCacheService.delete(key);
            }
        } else { //锁失败
            throw new ParameterValidException("任务ID:" + updateTaskDto.getTaskId() + "正在被处理中,请稍后再试");
        }
    }

    /**
     * 完成任务
     *
     * @param completeTaskDto completeTaskDto
     * @param userId          userId
     */
    @Transactional
    public void completeTask(CompleteTaskDto completeTaskDto, String userId) {
        //获得任务
        Task task = this.processEngine.getTaskService().createTaskQuery().taskId(completeTaskDto.getTaskId()).singleResult();
        //判空
        if (task == null) {
            throw new ParameterValidException("任务ID:" + completeTaskDto.getTaskId() + "不存在,或已被处理");
        }
        //判断当前登陆人是否是办理人
        if (!userId.equals(task.getAssignee())) {
            throw new ParameterValidException("您目前不是这个任务的办理人");
        }
        //锁名称
        final String key = ActivitiConstant.LOCAK_TASK_KEY_PREFIX + completeTaskDto.getTaskId();
        final Long expair = 5 * 60L;
        //尝试锁
        if (dbCacheService.setNx(key, CoreUtil.getTraceId(), expair)) { //锁成功
            try {
                //记录备注
                this.processEngine.getTaskService().addComment(completeTaskDto.getTaskId(), completeTaskDto.getProcessInstanceId(), completeTaskDto.getCommentType(), completeTaskDto.getCommentMessage());
                //清空本地变量
                Map<String, Object> variablesLocal = this.processEngine.getTaskService().getVariablesLocal(completeTaskDto.getTaskId());
                if (MapUtils.isNotEmpty(variablesLocal)) {
                    this.processEngine.getTaskService().removeVariablesLocal(completeTaskDto.getTaskId(), variablesLocal.keySet());
                }
                //完成任务
                this.processEngine.getTaskService().complete(completeTaskDto.getTaskId(), completeTaskDto.getVariables());
            } finally {
                //锁释放
                dbCacheService.delete(key);
            }
        } else { //锁失败
            throw new ParameterValidException("任务ID:" + completeTaskDto.getTaskId() + "正在被处理中,请稍后再试");
        }
    }

    /**
     * 暂存任务(保存流程变量,但是不完成)
     *
     * @param temporaryStorageTaskDto temporaryStorageTaskDto
     * @param userId                  userId
     */
    @Transactional
    public void temporaryStorageTask(TemporaryStorageTaskDto temporaryStorageTaskDto, String userId) {
        //获得任务
        Task task = this.processEngine.getTaskService().createTaskQuery().taskId(temporaryStorageTaskDto.getTaskId()).singleResult();
        //判空
        if (task == null) {
            throw new ParameterValidException("任务ID:" + temporaryStorageTaskDto.getTaskId() + "不存在,或已被处理");
        }
        //判断当前登陆人是否是办理人
        if (!userId.equals(task.getAssignee())) {
            throw new ParameterValidException("您目前不是这个任务的办理人");
        }
        //锁名称
        final String key = ActivitiConstant.LOCAK_TASK_KEY_PREFIX + temporaryStorageTaskDto.getTaskId();
        final Long expair = 5 * 60L;
        //尝试锁
        if (dbCacheService.setNx(key, CoreUtil.getTraceId(), expair)) { //锁成功
            try {
                this.processEngine.getTaskService().setVariablesLocal(temporaryStorageTaskDto.getTaskId(), temporaryStorageTaskDto.getVariables());
            } finally {
                //锁释放
                dbCacheService.delete(key);
            }
        } else { //锁失败
            throw new ParameterValidException("任务ID:" + temporaryStorageTaskDto.getTaskId() + "正在被处理中,请稍后再试");
        }
    }

    /**
     * 查询流程定义列表
     *
     * @return List<ProcessDefinitionDto>
     */
    public List<ProcessDefinitionDto> getProcessDefinitionList() {
        //定义查询条件
        ProcessDefinitionQuery processDefinitionQuery = this.processEngine.getRepositoryService().createProcessDefinitionQuery()
                .active()
                .latestVersion()
                .orderByProcessDefinitionId()
                .asc();
        //查询列表
        List<ProcessDefinition> list = processDefinitionQuery.list();
        //dto转换
        List<ProcessDefinitionDto> dtoList = null;
        if (CollectionUtils.isNotEmpty(list)) {
            dtoList = new ArrayList<>();
            for (ProcessDefinition processDefinition : list) {
                String sartFormKey = this.processEngine.getFormService().getStartFormKey(processDefinition.getId());
                dtoList.add(ActivitiUtil.buildDto(processDefinition, sartFormKey));
            }
        }
        //返回
        return dtoList;
    }

    /**
     * 生成流程追踪图片
     *
     * @param processInstanceId processInstanceId
     * @return InputStream
     * @throws IOException IOException
     */
    public InputStream processInstanceImage(String processInstanceId) throws IOException {
        // 获取bpmnModel
        HistoricProcessInstance historicProcessInstance = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        BpmnModel bpmnModel = this.processEngine.getRepositoryService().getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        //获得流程节点
        List<String> executedActivityIdList = new ArrayList<>();
        if (historicProcessInstance.getEndTime() == null) {
            executedActivityIdList = this.processEngine.getRuntimeService().getActiveActivityIds(processInstanceId);
        }
        //
        //获得图片流 & 返回
        return this.processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator().generateDiagram(
                bpmnModel, "JPEG",
                executedActivityIdList, new ArrayList<>(),
                this.processEngine.getProcessEngineConfiguration().getActivityFontName(),
                this.processEngine.getProcessEngineConfiguration().getLabelFontName(),
                this.processEngine.getProcessEngineConfiguration().getAnnotationFontName(),
                null, 1.0
        );

        // 获取流程中已经执行的节点，按照执行先后顺序排序
        //List<HistoricActivityInstance> historicActivityInstanceList = this.processEngine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().asc().list();
        // 构造已执行的节点ID集合
        //List<String> executedActivityIdList = historicActivityInstanceList.stream().map(a -> a.getActivityId()).collect(Collectors.toList());
        // 获取流程已发生流转的线ID集合
        //List<String> flowIds = this.getExecutedFlows(bpmnModel, historicActivityInstanceList);
    }

    /**
     * 更新任务流程变量
     *
     * @param processInstanceId processInstanceId
     * @param name              name
     * @param value             value
     */
    @Transactional
    public void updateTaskProcessVariable(String processInstanceId, String name, String value) {
        //获得当前正在活动的任务定义key
        List<String> executedActivityIdList = this.processEngine.getRuntimeService().getActiveActivityIds(processInstanceId);
        //判空
        if (CollectionUtils.isEmpty(executedActivityIdList)) {
            throw new ParameterValidException("该流程实例没有正在活动的任务");
        }
        //用第一个任务定义key获得任务实例
        Task task = this.processEngine.getTaskService().createTaskQuery().taskDefinitionKey(executedActivityIdList.get(0)).processInstanceId(processInstanceId).active().singleResult();
        //更新流程变量
        this.processEngine.getTaskService().setVariable(task.getId(), name, value);
    }

    /**
     * 返回正在活动的任务ID
     *
     * @param processInstanceId processInstanceId
     * @param taskDefinitionKey taskDefinitionKey
     * @return String
     */
    public String getActiveTaskId(String processInstanceId, String taskDefinitionKey) {
        Task task = this.processEngine.getTaskService().createTaskQuery().taskDefinitionKey(taskDefinitionKey).processInstanceId(processInstanceId).active().singleResult();
        if (task != null) {
            return task.getId();
        }
        return null;
    }

    /**
     * 更新流程业务键
     *
     * @param processInstanceId processInstanceId
     * @param businessKey       businessKey
     */
    @Transactional
    public void updateBusinessKey(String processInstanceId, String businessKey) {
        //businessKey判断重复
        long count = this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceBusinessKey(businessKey).count();
        if (count != 0) {
            throw new ParameterValidException("业务主键" + businessKey + "正在流程中");
        }
        //更新
        this.processEngine.getRuntimeService().updateBusinessKey(processInstanceId, businessKey);
    }

    /**
     * 更新流程发起人
     *
     * @param procInstId  procInstId
     * @param startUserId startUserId
     */
    @Transactional
    public void updateProcInstStartUserId(String procInstId, String startUserId) {
        //锁名称
        final String key = ActivitiConstant.LOCAK_PROC_INST_KEY_PREFIX + procInstId;
        final Long expair = 5 * 60L;
        //尝试锁
        if (dbCacheService.setNx(key, CoreUtil.getTraceId(), expair)) { //锁成功
            try {
                iActivitRepository.updateProcInstStartUserId(procInstId, startUserId);
            } finally {
                //锁释放
                dbCacheService.delete(key);
            }
        } else { //锁失败
            throw new ParameterValidException("流程实例ID:" + procInstId + "正在被处理中,请稍后再试");
        }
    }

    @Transactional
    public void changeProcInstName(String processInstanceId, String name) {
        //锁名称
        final String key = ActivitiConstant.LOCAK_PROC_INST_KEY_PREFIX + processInstanceId;
        final Long expair = 5 * 60L;
        //尝试锁
        if (dbCacheService.setNx(key, CoreUtil.getTraceId(), expair)) { //锁成功
            try {
                processEngine.getRuntimeService().setProcessInstanceName(processInstanceId, name);
            } finally {
                //锁释放
                dbCacheService.delete(key);
            }
        } else { //锁失败
            throw new ParameterValidException("流程实例ID:" + processInstanceId + "正在被处理中,请稍后再试");
        }
    }

    ///**
    // * 查询任务历史
    // *
    // * @param processInstanceId processInstanceId
    // * @return List<HistoricTaskInstanceDto>
    // */
    //public List<HistoricTaskInstanceDto> historicTaskInstanceList(String processInstanceId) {
    //    //定义查询
    //    HistoricTaskInstanceQuery historicTaskInstanceQuery = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery();
    //    //定义查询条件
    //    historicTaskInstanceQuery.processInstanceId(processInstanceId);
    //    //包含本地变量
    //    historicTaskInstanceQuery.includeTaskLocalVariables();
    //    //定义排序
    //    historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().desc();
    //    //查询list
    //    List<HistoricTaskInstance> list = historicTaskInstanceQuery.list();
    //    //dto转换
    //    List<HistoricTaskInstanceDto> dtoList = null;
    //    if (CollectionUtils.isNotEmpty(list)) {
    //        dtoList = new ArrayList<>();
    //        for (HistoricTaskInstance historicTaskInstance : list) {
    //            //获得需要的信息
    //            String userId = historicTaskInstance.getAssignee();
    //            String userName = iActivitiUserInfo.getUserName(userId);
    //            Long userDeptId = iActivitiUserInfo.getDeptId(userId);
    //            String userDeptName = iActivitiUserInfo.getDeptName(userId);
    //            //dto转换
    //            dtoList.add(ActivitiUtil.buildDto(historicTaskInstance, userId, userName, userDeptId, userDeptName));
    //        }
    //    }
    //    //返回
    //    return dtoList;
    //}

    ///**
    // * 待签收任务列表
    // *
    // * @param candidateUser         candidateUser
    // * @param pendingReceiptListDto pendingReceiptListDto
    // * @return TaskPageListDto
    // */
    //public TaskPageListDto pendingReceiptList(String candidateUser, PendingReceiptListDto pendingReceiptListDto) {
    //    //定义任务查询
    //    TaskQuery toClaimQuery = this.processEngine.getTaskService().createTaskQuery();
    //    //定义任务查询条件
    //    toClaimQuery.taskCandidateUser(candidateUser);
    //    toClaimQuery.active();
    //    //定义排序
    //    toClaimQuery.orderByTaskCreateTime();
    //    toClaimQuery.desc();
    //    //查询任务数量
    //    long count = toClaimQuery.count();
    //    //待处理任务列表(分页)
    //    List<Task> list = toClaimQuery.listPage(getFistResult(pendingReceiptListDto.getPage(), pendingReceiptListDto.getRows()), pendingReceiptListDto.getRows());
    //    //dto转换
    //    List<TaskDto> dtoList = null;
    //    if (CollectionUtils.isNotEmpty(list)) {
    //        dtoList = new ArrayList<>();
    //        for (Task task : list) {
    //            //获得流程实例
    //            ProcessInstance processInstance = this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
    //            //获得需要的信息
    //            String processDefinitionName = processInstance.getProcessDefinitionName();
    //            Date processInstanceStartTime = processInstance.getStartTime();
    //            String startUserId = processInstance.getStartUserId();
    //            String startUserName = iActivitiUserInfo.getUserName(startUserId);
    //            Long startUserDeptId = iActivitiUserInfo.getDeptId(startUserId);
    //            String startUserDeptName = iActivitiUserInfo.getDeptName(startUserId);
    //            //dto转换
    //            dtoList.add(ActivitiUtil.buildDto(processDefinitionName, processInstanceStartTime, startUserId, startUserName, startUserDeptId, startUserDeptName, task));
    //        }
    //    }
    //    //返回
    //    return new TaskPageListDto().setCount(count).setList(dtoList);
    //}

    ///**
    // * 签收任务
    // *
    // * @param claimTaskDto claimTaskDto
    // */
    //public void claimTask(ClaimTaskDto claimTaskDto) {
    //    this.processEngine.getTaskService().claim(claimTaskDto.getTaskId(), claimTaskDto.getAssignee());
    //}

    ///**
    // * 退回签收任务
    // *
    // * @param taskId taskId
    // */
    //public void unClaimTask(String taskId) {
    //    this.processEngine.getTaskService().unclaim(taskId);
    //}

    ///**
    // * 委托任务
    // *
    // * @param delegateTaskDto delegateTaskDto
    // */
    //public void delegateTask(DelegateTaskDto delegateTaskDto) {
    //    this.processEngine.getTaskService().delegateTask(delegateTaskDto.getTaskId(), delegateTaskDto.getAssignee());
    //}

    ///**
    // * 解决委托任务
    // *
    // * @param resolveTaskDto resolveTaskDto
    // */
    //public void resolveTask(ResolveTaskDto resolveTaskDto) {
    //    this.processEngine.getTaskService().resolveTask(resolveTaskDto.getTaskId(), resolveTaskDto.getVariables());
    //}

    ///**
    // * 获得流程定义
    // *
    // * @param processDefinitionId processDefinitionId
    // * @return ProcessDefinitionDto
    // */
    //public ProcessDefinitionDto getProcessDefinition(String processDefinitionId) {
    //    ProcessDefinition processDefinition = this.processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    //    String sartFormKey = this.processEngine.getFormService().getStartFormKey(processDefinition.getId());
    //    return ActivitiUtil.buildDto(processDefinition, sartFormKey);
    //}

    ///**
    // * 获得流程实例历史信息
    // *
    // * @param processInstanceId processInstanceId
    // * @return HistoricProcessInstanceDto
    // */
    //public HistoricProcessInstanceDto getHistoricProcessInstance(String processInstanceId) {
    //    HistoricProcessInstance historicProcessInstance = this.processEngine.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    //    return ActivitiUtil.buildDto(historicProcessInstance);
    //}

    ///**
    // * 获得流程中高亮的线
    // *
    // * @param bpmnModel                 bpmnModel
    // * @param historicActivityInstances historicActivityInstances
    // * @return List<String>
    // */
    //private List<String> getExecutedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
    //    // 流转线ID集合
    //    List<String> flowIdList = new ArrayList<>();
    //    // 全部活动实例
    //    List<FlowNode> historicFlowNodeList = new LinkedList<>();
    //    // 已完成的历史活动节点
    //    List<HistoricActivityInstance> finishedActivityInstanceList = new LinkedList<>();
    //    for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
    //        historicFlowNodeList.add((FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true));
    //        if (historicActivityInstance.getEndTime() != null) {
    //            finishedActivityInstanceList.add(historicActivityInstance);
    //        }
    //    }
    //    // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
    //    for (HistoricActivityInstance currentActivityInstance : finishedActivityInstanceList) {
    //        // 获得当前活动对应的节点信息及outgoingFlows信息
    //        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
    //        List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();
    //        /**
    //         * 遍历outgoingFlows并找到已已流转的
    //         * 满足如下条件认为已已流转：
    //         * 1.当前节点是并行网关或包含网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
    //         * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最近的流转节点视为有效流转
    //         */
    //        if (BpmsActivityTypeEnum.PARALLEL_GATEWAY.getType().equals(currentActivityInstance.getActivityType())
    //                || BpmsActivityTypeEnum.INCLUSIVE_GATEWAY.getType().equals(currentActivityInstance.getActivityType())) {
    //            // 遍历历史活动节点，找到匹配Flow目标节点的
    //            for (SequenceFlow sequenceFlow : sequenceFlowList) {
    //                FlowNode targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
    //                if (historicFlowNodeList.contains(targetFlowNode)) {
    //                    flowIdList.add(sequenceFlow.getId());
    //                }
    //            }
    //        } else {
    //            List<Map<String, String>> tempMapList = new LinkedList<>();
    //            // 遍历历史活动节点，找到匹配Flow目标节点的
    //            for (SequenceFlow sequenceFlow : sequenceFlowList) {
    //                for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
    //                    if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
    //                        tempMapList.add(UtilMisc.toMap("flowId", sequenceFlow.getId(), "activityStartTime", String.valueOf(historicActivityInstance.getStartTime().getTime())));
    //                    }
    //                }
    //            }
    //            // 遍历匹配的集合，取得开始时间最早的一个
    //            long earliestStamp = 0L;
    //            String flowId = null;
    //            for (Map<String, String> map : tempMapList) {
    //                long activityStartTime = Long.valueOf(map.get("activityStartTime"));
    //                if (earliestStamp == 0 || earliestStamp >= activityStartTime) {
    //                    earliestStamp = activityStartTime;
    //                    flowId = map.get("flowId");
    //                }
    //            }
    //            flowIdList.add(flowId);
    //        }
    //    }
    //    return flowIdList;
    //}

    ///**
    // * IO转base64字符串
    // *
    // * @param in in
    // * @return String
    // * @throws IOException IOException
    // */
    //private String ioToBase64(InputStream in) throws IOException {
    //    try {
    //        byte[] bytes = new byte[in.available()];
    //        in.read(bytes);
    //        return Base64.encodeBase64String(bytes);
    //    } finally {
    //        in.close();
    //    }
    //}

    /**
     * 计算页数
     *
     * @param page 页数
     * @param rows 行数
     * @return int
     */
    private int getFistResult(int page, int rows) {
        return (page - 1) * rows;
    }
}
