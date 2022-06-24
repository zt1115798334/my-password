package com.zt.mypassword.mysql.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/02/27 18:10
 * description:
 */
@Data
@Entity
@Table(name = "t_permission")
public class Permission implements Serializable {
    @Serial
    private static final long serialVersionUID = 1890258658838804131L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限昵称
     */
    private String permissionNickname;

    /**
     * 权限字符串
     */
    private String permission;
    /**
     * 权限类型 display：显示权限   operation 操作权限
     */
    private String permissionType;
    /**
     * url地址
     */
    private String url;
    /**
     * 前台地址
     */
    private String path;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
