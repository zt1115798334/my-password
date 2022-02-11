package com.zt.mypassword.base.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zt.mypassword.enums.SystemStatusCode;
import com.zt.mypassword.utils.DateUtils;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 18:27
 * description: 前后端统一消息定义协议 Message 之后前后端数据交互都按照规定的类型进行交互
 */
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultMessage {

    private final SystemStatusCode SUCCESS =  SystemStatusCode.SUCCESS;
    private final  SystemStatusCode FAILED =  SystemStatusCode.FAILED;
    // 消息头meta 存放状态信息 code message

    @JsonInclude(Include.NON_NULL)
    private Meta meta;
    // 消息内容  存储实体交互数据
    @JsonInclude(Include.NON_NULL)
    private PageData page;
    // 消息内容  存储实体交互数据
    @JsonInclude(Include.NON_NULL)
    private ScrollData scroll;

    @JsonInclude(Include.NON_NULL)
    private JSONObject data;

    @JsonInclude(Include.NON_NULL)
    private JSONArray list;

    @JsonInclude(Include.NON_NULL)
    private String value;

    public ResultMessage setMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public ResultMessage setPageData(PageData page) {
        this.page = page;
        return this;
    }

    public PageData getPage() {
        return this.page;
    }

    public ResultMessage setScroll(ScrollData scroll) {
        this.scroll = scroll;
        return this;
    }

    public ScrollData getScroll() {
        return this.scroll;
    }


    public JSONArray findPageDataList() {
        PageData pageData = this.page;
        return pageData != null ? JSONArray.parseArray(JSONArray.toJSONString(pageData.getList())) : new JSONArray();
    }

    public ResultMessage setData(Object data) {
        Object json = JSON.toJSON(data);
        if (json instanceof JSONObject) {
            this.data = JSONObject.parseObject(JSONObject.toJSONStringWithDateFormat(data, JSON.DEFFAULT_DATE_FORMAT));
        } else if (json instanceof JSONArray) {
            this.list = JSONArray.parseArray(JSONArray.toJSONStringWithDateFormat(data, JSON.DEFFAULT_DATE_FORMAT));
        } else {
            this.value = String.valueOf(data);
        }
        return this;
    }

    public Object getData() {
        return data;
    }

    public JSONArray getList() {
        return list;
    }

    public String getValue() {
        return value;
    }

    public ResultMessage correctness() {
        Meta build = Meta.builder().success(Boolean.TRUE).code(SUCCESS.getCode()).timestamp(LocalDateTime.now()).build();
        return this.setMeta(build);
    }

    public ResultMessage correctness(String statusMsg) {
        Meta build = Meta.builder().success(Boolean.TRUE).code(SUCCESS.getCode()).timestamp(LocalDateTime.now()).msg(statusMsg).build();
        return this.setMeta(build);
    }

    public ResultMessage correctness(int code, String statusMsg) {
        Meta build = Meta.builder().success(Boolean.TRUE).code(code).timestamp(LocalDateTime.now()).msg(statusMsg).build();
        return this.setMeta(build);
    }

    public ResultMessage correctnessPage(int pageNumber, int pageSize, long total, Object rows) {
        PageData build = PageData.builder().pageNumber(pageNumber).pageSize(pageSize).total(total).list(rows).build();
        return this.correctness().setPageData(build);
    }

    public ResultMessage correctnessScroll(String scrollId, Object rows) {
        ScrollData build = ScrollData.builder().scrollId(scrollId).list(rows).build();
        return this.correctness().setScroll(build);
    }

    public ResultMessage error() {
        Meta build = Meta.builder().success(Boolean.FALSE).code(FAILED.getCode()).timestamp(LocalDateTime.now()).build();
        return this.setMeta(build);
    }

    public ResultMessage error(int statusCode) {
        Meta build = Meta.builder().success(Boolean.FALSE).code(statusCode).timestamp(LocalDateTime.now()).build();
        return this.setMeta(build);
    }

    public ResultMessage error(String statusMsg) {
        Meta build = Meta.builder().success(Boolean.FALSE).code(FAILED.getCode()).timestamp(LocalDateTime.now()).msg(statusMsg).build();
        return this.setMeta(build);
    }

    public ResultMessage error(int statusCode, String statusMsg) {
        Meta build = Meta.builder().success(Boolean.FALSE).code(statusCode).timestamp(LocalDateTime.now()).msg(statusMsg).build();
        return this.setMeta(build);
    }

    public ResultMessage errorPage() {
        PageData build = PageData.builder().pageNumber(0).pageSize(0).total(0).list(Collections.EMPTY_LIST).build();
        return this.error().setPageData(build);
    }


    @ToString
    @AllArgsConstructor
    @Getter
    @Builder()
    @JsonIgnoreProperties
    public static class Meta {
        private final boolean success;
        private final int code;
        @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
        private final LocalDateTime timestamp;
        private final String msg;

    }

    @AllArgsConstructor
    @Getter
    @Builder()
    @JsonIgnoreProperties
    public static class PageData {
        private final int pageNumber;
        private final int pageSize;
        private final long total;
        private final Object list;
    }

    @AllArgsConstructor
    @Getter
    @Builder()
    @JsonIgnoreProperties
    public static class ScrollData {
        private final String scrollId;
        private final Object list;
    }
}
