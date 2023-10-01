package org.itkk.udf.starter.process.activiti;

import lombok.Data;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.history.HistoryLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
@ConfigurationProperties(prefix = ActivitiProperties.ACTIVITI_PROPERTIES_PRE_FIX)
@Validated
@Data
public class ActivitiProperties {

    /**
     * 配置文件前缀
     */
    protected static final String ACTIVITI_PROPERTIES_PRE_FIX = "org.itkk.udf.starter.process.activiti.properties";

    /**
     * 历史级别
     */
    @NotNull(message = ACTIVITI_PROPERTIES_PRE_FIX + ".historyLevel不能为空")
    private HistoryLevel historyLevel = HistoryLevel.AUDIT;

    /**
     * 字体名称
     */
    @NotBlank(message = ACTIVITI_PROPERTIES_PRE_FIX + ".fontName不能为空")
    private String fontName = "宋体";

    /**
     * 全局监听器
     */
    private List<ActivitiEventListener> eventListeners;

}
