package com.zt.mypassword.dto;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/10/27 15:39
 * description:
 */
public class ScrollDto<T>{

    private final List<T> content;

    private final String scrollId;

    public ScrollDto(List<T> content, String scrollId) {
        this.content = content;
        this.scrollId = scrollId;
    }

    public List<T> getContent() {
        return content;
    }

    public String getScrollId() {
        return scrollId;
    }
}
