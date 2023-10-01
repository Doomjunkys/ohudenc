package org.itkk.udf.starter.process.activiti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchDto {

	private String processInstanceBusinessKey;

	private String processDefinitionKey;

	private String taskDefinitionKey;


}
