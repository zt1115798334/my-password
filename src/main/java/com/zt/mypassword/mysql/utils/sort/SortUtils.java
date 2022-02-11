package com.zt.mypassword.mysql.utils.sort;

import org.springframework.data.domain.Sort;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 16:05
 * description:
 */
public class SortUtils {

    public static Sort basicSort() {
        return basicSort(Sort.Direction.ASC, "id");
    }

    public static Sort basicSort(Sort.Direction sort, String orderField) {
        return Sort.by(sort, orderField);
    }

    public static Sort basicSort(SortDto... dtoArr) {
        Sort result = null;
        for (SortDto dto : dtoArr) {
            if (result == null) {
                result = Sort.by(dto.getOrderType(), dto.getOrderField());
            } else {
                result = result.and(Sort.by(dto.getOrderType(), dto.getOrderField()));
            }
        }
        return result;
    }
}
