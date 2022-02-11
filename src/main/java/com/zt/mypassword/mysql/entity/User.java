package com.zt.mypassword.mysql.entity;

import com.zt.mypassword.enums.AccountType;
import com.zt.mypassword.enums.DeleteState;
import com.zt.mypassword.enums.EnabledState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 11:05
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "t_user")
public class User {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 账户
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 账户类型：{admin :管理员用户,ordinary:普通用户,}
     */
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    /**
     * 开启状态
     */
    @Enumerated(EnumType.STRING)
    private EnabledState enabledState;
    /**
     * 头像Id
     */
    private Long profilesPictureId;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 删除状态
     */
    @Enumerated(EnumType.STRING)
    private DeleteState deleteState;

}
