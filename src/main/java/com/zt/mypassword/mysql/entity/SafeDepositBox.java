package com.zt.mypassword.mysql.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

import com.zt.mypassword.enums.DeleteState;
import com.zt.mypassword.enums.DepositType;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 * 
 * @author zhang tong
 * date: 2022/02/16 15:17
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table (name = "t_safe_deposit_box")
public class SafeDepositBox implements Serializable {

	@Serial
	private static final long serialVersionUID =  1890258658838804138L;

   	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
     * 用户ID
     */
	private Long userId;

	/**
     * 安全名称
     */
	private String safeName;

	/**
     * 安全账户
     */
	private String safeAccount;

	/**
     * 安全密码
     */
	private String safePassword;

	/**
     * 信息等级
     */
	@Enumerated(value = EnumType.STRING)
	private DepositType depositType;

	/**
     * 创建时间
     */
	private LocalDateTime createdTime;

	/**
     * 更新时间
     */
	private LocalDateTime updatedTime;

	/**
     * 删除状态
     */
	@Enumerated(value = EnumType.STRING)
	private DeleteState deleteState;

}
