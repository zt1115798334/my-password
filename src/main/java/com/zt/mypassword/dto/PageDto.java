package com.zt.mypassword.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/28 10:50
 * description:分页查询
 */

@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页查询")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageDto implements Serializable {
    /**
     * 页数
     */
    @ApiModelProperty(value = "页数")
    @Min(value = 1, message = "页数最小为1页")
    protected int pageNumber = 1;
    /**
     * 每页显示数量
     */
    @ApiModelProperty(value = "每页显示数量")
    @Range(min = 1, max = 10000, message = "每页显示数量最大为10000条")
    protected int pageSize = 10;

}
