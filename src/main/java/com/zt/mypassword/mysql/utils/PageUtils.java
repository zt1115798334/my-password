package com.zt.mypassword.mysql.utils;

import com.zt.mypassword.dto.PageDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:50
 * description:
 */
public class PageUtils {
    /**
     * @param page      当前页
     * @param size      每页条数
     * @param sortName  排序字段
     * @param sortOrder 排序方向
     */
    public static PageRequest buildPageRequest(int page, int size, String sortName, Sort.Direction sortOrder) {
        return Optional.ofNullable(sortName)
                .map(s -> Optional.ofNullable(sortOrder)
                        .map(ss -> Sort.by(sortOrder, sortName)).orElseGet(() -> Sort.by(Sort.Direction.ASC, sortName)))
                .map(sort -> PageRequest.of(page - 1, size, sort))
                .orElse(PageRequest.of(page - 1, size));
    }

    public static PageRequest buildPageRequest(int page, int size, String sortName) {
        return buildPageRequest(page, size, sortName, Sort.Direction.ASC);
    }

    public static PageRequest buildPageRequest(PageDto pageDto) {
        return buildPageRequest(pageDto.getPageNumber(), pageDto.getPageSize(), null, Sort.Direction.ASC);
    }

    public static PageRequest buildPageRequest(int page, int size) {
        return buildPageRequest(page, size, null, Sort.Direction.ASC);
    }

    public static PageRequest buildPageRequest(int page, int size, Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

    public static PageRequest buildPageRequest(PageDto pageDto, Sort sort) {
        return PageRequest.of(pageDto.getPageNumber() - 1, pageDto.getPageSize(), sort);
    }

    public static Integer getOffset(Integer pageNumber, Integer pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
