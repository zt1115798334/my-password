package com.zt.mypassword.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/08/20 13:34
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "t_user_log")
public class UserLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1890258658838804135L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 操作员ID
     */
    private Long userId;
    /**
     * 操作员姓名
     */
    private String name;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 操作详情
     */
    private String content;
    /**
     * IP
     */
    private String ip;
    /**
     * 创建时间
     */
    private LocalDateTime time;
    /**
     * 类
     */
    private String classify;
    /**
     * 方法
     */
    private String fun;

    /**
     * 返回值
     */
    private String response;


}
