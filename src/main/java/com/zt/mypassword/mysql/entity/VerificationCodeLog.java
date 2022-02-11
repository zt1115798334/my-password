package com.zt.mypassword.mysql.entity;

import com.zt.mypassword.enums.VerificationCodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/03/20 09:37
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "t_verification_code_log")
public class VerificationCodeLog {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 通知内容
     */
    private String noticeContent;
    /**
     * 验证码
     */
    private String verificationCode;
    /**
     * 验证码类型
     */
    @Enumerated(EnumType.STRING)
    private VerificationCodeType verificationCodeType;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
