package org.itkk.udf.api.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class SendMailDto implements Serializable {
    private static final long serialVersionUID = 6443090530533741230L; //ID
    private String emailSubject; //标题
    private String emailContent; //主题
    private Set<String> addres; //地址列表
    private Map<String, File> fileMap; //附件列表
}
