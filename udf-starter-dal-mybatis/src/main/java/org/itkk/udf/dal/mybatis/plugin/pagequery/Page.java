package org.itkk.udf.dal.mybatis.plugin.pagequery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述 : Page
 *
 * @author wangkang
 */
@ApiModel(description = "分页消息体")
@Data
public class Page<E> {

    /**
     * 当前页,默认1
     */
    @ApiModelProperty(value = "当前页", required = true, dataType = "int")
    private int curPage = 1;

    /**
     * 每页数量,默认0
     */
    @ApiModelProperty(value = "每页行数", required = true, dataType = "int")
    private int pageSize = 0;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数", required = true, dataType = "long")
    private long totalPages = 0;

    /**
     * 总条数
     */
    @ApiModelProperty(value = "总行数", required = true, dataType = "long")
    private long totalRecords = 0;

    /**
     * 结果集
     */
    @ApiModelProperty(value = "结果集", required = true, dataType = "object")
    private List<E> records;

    /**
     * 描述 : 构造函数
     *
     * @param pageResult 结果集
     */
    public Page(PageResult<E> pageResult) {
        this.curPage = pageResult.getCurPage();
        this.pageSize = pageResult.getPageSize();
        this.totalPages = pageResult.getTotalPages();
        this.totalRecords = pageResult.getTotalRecords();
        this.records = pageResult;
    }

}
