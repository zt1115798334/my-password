package com.zt.mypassword.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zt.mypassword.enums.DepositType;
import com.zt.mypassword.enums.EnabledState;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/14 14:41
 * description:
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchSafeDepositBoxDto extends PageDto {
    /**
     * 搜索内容
     */
    @ApiModelProperty(value = "搜索内容")
    private String searchValue;

    /**
     * 信息等级
     */
    @ApiModelProperty(value = "信息等级")
    private DepositType depositType;

}
