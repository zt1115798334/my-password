package com.zt.mypassword.mysql.utils.sort;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Sort;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:05
 * description:
 */
@Data
@AllArgsConstructor
public class SortDto {

    //排序方式
    private Sort.Direction orderType;

    //排序字段
    private String orderField;

    //默认为DESC排序
    public SortDto(String orderField) {
        this.orderField = orderField;
        this.orderType = Sort.Direction.ASC;
    }
}
