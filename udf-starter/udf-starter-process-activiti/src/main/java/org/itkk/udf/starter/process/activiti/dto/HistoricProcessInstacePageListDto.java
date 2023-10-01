package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class HistoricProcessInstacePageListDto {
    /**
     * 数量
     */
    private Long count;
    /**
     * 列表
     */
    private List<HistoricProcessInstanceDto> list;
}
