package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class SetAssigneeDto {
    /**
     * 任务ID
     */
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    /**
     * 代办人变量名 (多个,注意 : 必须是存储相同数据的不同变量)
     */
    @NotNull(message = "代办人变量名不能为空")
    private List<String> assigneeVarName;

    /**
     * 原始代办人
     */
    @NotBlank(message = "代办人变量名不能为空")
    private String originalAssignee;

    /**
     * 代办人
     */
    @NotBlank(message = "代办人不能为空")
    private String assignee;

    /**
     * 是否发送邮件,默认false
     */
    @NotNull(message = "是否发送邮件")
    private Boolean sendMail = false;
}
