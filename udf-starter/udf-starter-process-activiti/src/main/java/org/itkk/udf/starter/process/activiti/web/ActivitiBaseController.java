package org.itkk.udf.starter.process.activiti.web;

import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.RestResponse;
import org.itkk.udf.starter.process.activiti.dto.*;
import org.itkk.udf.starter.process.activiti.service.ActivitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 流程引擎服务
 */
public abstract class ActivitiBaseController {

    /**
     * activitiService
     */
    @Autowired
    private ActivitiService activitiService;

    /**
     * request
     */
    @Autowired
    private HttpServletRequest request;

    /**
     * response
     */
    @Autowired
    private HttpServletResponse response;

    /**
     * 获得当前用户ID
     *
     * @return String
     */
    public abstract String getCurrentUserId();

    /**
     * 生成流程追踪图片
     *
     * @param processInstanceId processInstanceId
     * @throws IOException IOException
     */
    @GetMapping("process/instance/image/{processInstanceId}")
    public void processInstanceImage(@PathVariable String processInstanceId) throws IOException {
        //设置response
        response.setCharacterEncoding(CoreConstant.CHARACTER_SET);
        response.setContentType("image/jpeg");
        response.setDateHeader("Expires", 0L);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        //输出文件
        final int buffInt = 1024;
        byte[] buff = new byte[buffInt];
        try (
                InputStream inputStream = activitiService.processInstanceImage(processInstanceId);
                OutputStream os = response.getOutputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream)
        ) {
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        }
    }

    /**
     * 查询流程定义列表
     *
     * @return RestResponse<List < ProcessDefinitionDto>>
     */
    @GetMapping("get/process/definition/list")
    public RestResponse<List<ProcessDefinitionDto>> getProcessDefinitionList() {
        return new RestResponse<>(activitiService.getProcessDefinitionList());
    }

    /**
     * 启动流程实例
     *
     * @param startProcessInstanceDto startProcessInstanceDto
     * @return RestResponse<ProcessInstanceDto>
     */
    @PostMapping("start/process/instance")
    public RestResponse<ProcessInstanceDto> startProcessInstance(@Valid @RequestBody StartProcessInstanceDto startProcessInstanceDto) {
        return new RestResponse<>(activitiService.startProcessInstance(getCurrentUserId(), startProcessInstanceDto));
    }

    /**
     * 删除流程实例
     *
     * @param delProcessInstanceDto delProcessInstanceDto
     * @return RestResponse<String>
     */
    @PostMapping("del/process/instance")
    public RestResponse<String> delProcessInstance(@Valid @RequestBody DelProcessInstanceDto delProcessInstanceDto) {
        activitiService.delProcessInstance(delProcessInstanceDto);
        return RestResponse.success();
    }

    /**
     * 我发起的流程实例列表
     *
     * @param startProcessInstanceListDto startProcessInstanceListDto
     * @return RestResponse<HistoricProcessInstacePageListDto>
     */
    @PostMapping("start/process/instance/list")
    public RestResponse<HistoricProcessInstacePageListDto> startProcessInstanceList(@Valid @RequestBody StartProcessInstanceListDto startProcessInstanceListDto) {
        return new RestResponse<>(activitiService.startProcessInstanceList(getCurrentUserId(), startProcessInstanceListDto));
    }

    /**
     * 流程详情
     *
     * @param processInstanceId processInstanceId
     * @return RestResponse<HistoricProcessInstanceDto>
     */
    @GetMapping("historic/process/{processInstanceId}")
    public RestResponse<HistoricProcessInstanceDto> historicProcess(@PathVariable String processInstanceId) {
        return new RestResponse<>(activitiService.historicProcess(processInstanceId));
    }

    /**
     * 我的已处理任务列表
     *
     * @param processedListDto processedListDto
     * @return RestResponse<TaskPageListDto>
     */
    @PostMapping("processed/list")
    public RestResponse<HistoricTaskPageListDto> processedList(@Valid @RequestBody ProcessedListDto processedListDto) {
        return new RestResponse<>(activitiService.processedList(getCurrentUserId(), processedListDto));
    }

    /**
     * 我的待处理任务列表
     *
     * @param pendingListDto pendingListDto
     * @return RestResponse<TaskPageListDto>
     */
    @PostMapping("pending/list")
    public RestResponse<TaskPageListDto> pendingList(@Valid @RequestBody PendingListDto pendingListDto) {
        return new RestResponse<>(activitiService.pendingList(getCurrentUserId(), pendingListDto));
    }

    /**
     * 所有运行中的任务列表
     *
     * @param allRuntimeTaskListDto allRuntimeTaskListDto
     * @return RestResponse<TaskPageListDto>
     */
    @PostMapping("all/runtime/task/list")
    public RestResponse<TaskPageListDto> allRuntimeTaskList(@Valid @RequestBody AllRuntimeTaskListDto allRuntimeTaskListDto) {
        return new RestResponse<>(activitiService.allRuntimeTaskList(allRuntimeTaskListDto));
    }

    /**
     * 获得任务的流程变量
     *
     * @param taskVariablesDto taskVariablesDto
     * @return RestResponse<Map < String, Object>>
     */
    @PostMapping("task/variables")
    public RestResponse<Map<String, Object>> getTaskVariables(@Valid @RequestBody TaskVariablesDto taskVariablesDto) {
        return new RestResponse<>(activitiService.getTaskVariables(taskVariablesDto, getCurrentUserId()));
    }

    /**
     * 获得任务的流程变量(运维)
     *
     * @param dto dto
     * @return RestResponse<List < Map < String, String>>>
     */
    @PostMapping("task/variables/ops")
    public RestResponse<List<Map<String, String>>> getTaskVariablesOps(@Valid @RequestBody TaskVariablesBatchDto dto) {
        return new RestResponse<>(activitiService.getTaskVariablesOps(dto));
    }

    /**
     * 任务详情
     *
     * @param taskId taskId
     * @return RestResponse<TaskDto>
     */
    @GetMapping("pending/task/{taskId}")
    public RestResponse<TaskDto> pendingTask(@PathVariable String taskId) {
        return new RestResponse<>(activitiService.pendingTask(taskId, getCurrentUserId()));
    }

    /**
     * 暂存任务(保存流程变量,但是不完成任务)
     *
     * @param temporaryStorageTaskDto temporaryStorageTaskDto
     * @return RestResponse<String>
     */
    @PostMapping("temporary/storage/task")
    public RestResponse<String> temporaryStorageTask(@Valid @RequestBody TemporaryStorageTaskDto temporaryStorageTaskDto) {
        activitiService.temporaryStorageTask(temporaryStorageTaskDto, getCurrentUserId());
        return RestResponse.success();
    }

    /**
     * 返回流程备注
     *
     * @param processInstanceId processInstanceId
     * @return RestResponse<List < CommentDto>>
     */
    @GetMapping("get/task/comment/list/{processInstanceId}")
    public RestResponse<List<CommentDto>> getTaskCommentList(@PathVariable String processInstanceId) {
        return new RestResponse<>(activitiService.getTaskCommentList(processInstanceId));
    }

    /**
     * 设置代办人
     *
     * @param setAssigneeDto setAssigneeDto
     * @return RestResponse<String>
     */
    @PostMapping("set/assignee")
    public RestResponse<String> setAssignee(@Valid @RequestBody SetAssigneeDto setAssigneeDto) {
        activitiService.setAssignee(setAssigneeDto, getCurrentUserId());
        return RestResponse.success();
    }

    /**
     * 更新任务
     *
     * @param updateTaskDto updateTaskDto
     * @return RestResponse<String>
     */
    @PostMapping("update/task")
    public RestResponse<String> updateTask(@Valid @RequestBody UpdateTaskDto updateTaskDto) {
        activitiService.updateTask(updateTaskDto);
        return RestResponse.success();
    }

    /**
     * 完成任务
     *
     * @param completeTaskDto completeTaskDto
     * @return RestResponse<String>
     */
    @PostMapping("complete/task")
    public RestResponse<String> completeTask(@Valid @RequestBody CompleteTaskDto completeTaskDto) {
        activitiService.completeTask(completeTaskDto, getCurrentUserId());
        return RestResponse.success();
    }

    ///**
    // * 查询任务历史
    // *
    // * @param processInstanceId processInstanceId
    // * @return RestResponse<List < HistoricTaskInstanceDto>>
    // */
    //@GetMapping("historic/task/instance/list/{processInstanceId}")
    //public RestResponse<List<HistoricTaskInstanceDto>> historicTaskInstanceList(@PathVariable String processInstanceId) {
    //    return new RestResponse<>(activitiService.historicTaskInstanceList(processInstanceId));
    //}

    ///**
    // * 待签收任务列表
    // *
    // * @param pendingReceiptListDto pendingReceiptListDto
    // * @return RestResponse<TaskPageListDto>
    // */
    //@PostMapping("pending/receipt/list")
    //public RestResponse<TaskPageListDto> pendingReceiptList(@Valid @RequestBody PendingReceiptListDto pendingReceiptListDto) {
    //    return new RestResponse<>(activitiService.pendingReceiptList(getCurrentUserId(), pendingReceiptListDto));
    //}

    ///**
    // * 签收任务
    // *
    // * @param claimTaskDto claimTaskDto
    // * @return RestResponse<String>
    // */
    //@PostMapping("claim/task")
    //public RestResponse<String> claimTask(@Valid @RequestBody ClaimTaskDto claimTaskDto) {
    //    activitiService.claimTask(claimTaskDto);
    //    return RestResponse.success();
    //}

    ///**
    // * 退回签收任务
    // *
    // * @param taskId taskId
    // * @return RestResponse<String>
    // */
    //@PostMapping("unclaim/task/{taskId}")
    //public RestResponse<String> unClaimTask(@PathVariable String taskId) {
    //    activitiService.unClaimTask(taskId);
    //    return RestResponse.success();
    //}

    ///**
    // * 委托任务
    // *
    // * @param delegateTaskDto delegateTaskDto
    // * @return RestResponse<String>
    // */
    //@PostMapping("delegate/task")
    //public RestResponse<String> delegateTask(@Valid @RequestBody DelegateTaskDto delegateTaskDto) {
    //    activitiService.delegateTask(delegateTaskDto);
    //    return RestResponse.success();
    //}

    ///**
    // * 解决委托任务
    // *
    // * @param resolveTaskDto resolveTaskDto
    // * @return RestResponse<String>
    // */
    //@PostMapping("resolve/task")
    //public RestResponse<String> resolveTask(@Valid @RequestBody ResolveTaskDto resolveTaskDto) {
    //    activitiService.resolveTask(resolveTaskDto);
    //    return RestResponse.success();
    //}

    ///**
    // * 获得流程定义
    // *
    // * @param processDefinitionId processDefinitionId
    // * @return RestResponse<ProcessDefinitionDto>
    // */
    //@GetMapping("get/process/definition/{processDefinitionId}")
    //public RestResponse<ProcessDefinitionDto> getProcessDefinition(@PathVariable String processDefinitionId) {
    //    return new RestResponse<>(activitiService.getProcessDefinition(processDefinitionId));
    //}

    ///**
    // * 获得流程实例历史信息
    // *
    // * @param processInstanceId processInstanceId
    // * @return RestResponse<HistoricProcessInstanceDto>
    // */
    //@GetMapping("get/historic/process/instance/{processInstanceId}")
    //public RestResponse<HistoricProcessInstanceDto> getHistoricProcessInstance(@PathVariable String processInstanceId) {
    //    return new RestResponse<>(activitiService.getHistoricProcessInstance(processInstanceId));
    //}
}
