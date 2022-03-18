package com.zt.mypassword.dto;

import javax.persistence.*;
import java.io.Serializable;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.zt.mypassword.enums.DepositType;
import com.zt.mypassword.mysql.entity.SafeDepositBox;
import com.zt.mypassword.mysql.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * 
 * @author zhang tong
 * date: 2022/02/17 17:44
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SafeDepositBoxDto implements Serializable {


   	@ApiModelProperty(value = "id")
	private Long id;


	/**
     * 安全名称
     */
   	@ApiModelProperty(value = "安全名称")
	private String safeName;

	/**
     * 安全账户
     */
   	@ApiModelProperty(value = "安全账户")
	private String safeAccount;
	/**
     * 安全密码
     */
   	@ApiModelProperty(value = "安全密码")
	private String safePassword;

	/**
     * 信息等级
     */
   	@ApiModelProperty(value = "信息等级")
	private DepositType depositType;

	/**
     * 更新时间
     */
   	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updatedTime;

	public static SafeDepositBox dtoChangeEntity(SafeDepositBoxDto safeDepositBoxDto,String rsaPrivateKey) {
		RSA rsa = SecureUtil.rsa(rsaPrivateKey, null);
		return SafeDepositBox
				.builder()
				.id(safeDepositBoxDto.getId())
				.safeName(safeDepositBoxDto.getSafeName())
				.safeAccount(rsa.encryptBase64(safeDepositBoxDto.getSafeAccount(), KeyType.PrivateKey))
				.safePassword(rsa.encryptBase64(safeDepositBoxDto.getSafePassword(), KeyType.PrivateKey))
				.depositType(safeDepositBoxDto.getDepositType())
				.build();
	}

	public static SafeDepositBoxDto entityChangeDto(SafeDepositBox safeDepositBox) {
		return SafeDepositBoxDto
			.builder()
			.id(safeDepositBox.getId())
			.safeName(safeDepositBox.getSafeName())
			.safeAccount(safeDepositBox.getSafeAccount())
			.safePassword(safeDepositBox.getSafePassword())
			.depositType(safeDepositBox.getDepositType())
			.updatedTime(safeDepositBox.getUpdatedTime())
			.build();
	}

	public static List<SafeDepositBoxDto> entityChangeListDto(List<SafeDepositBox> safeDepositBoxList){
		return safeDepositBoxList.stream().map(SafeDepositBoxDto::entityChangeDto).collect(Collectors.toList());
	}

}
