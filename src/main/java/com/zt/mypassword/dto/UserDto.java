package com.zt.mypassword.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.enums.AccountType;
import com.zt.mypassword.enums.EnabledState;
import com.zt.mypassword.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/14 14:41
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {
    /**
     *
     */
    private Long id;
    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String account;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @JSONField(serialize = false)
    private String password;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 账户类型
     */
    @ApiModelProperty(value = "账户类型")
    private AccountType accountType;
    /**
     * 开启状态
     */
    @ApiModelProperty(value = "开启状态")
    private EnabledState enabledState;
    /**
     * 头像路径
     */
    @ApiModelProperty(value = "头像路径")
    private String profilesPicturePath;
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "部门Id")
    private Long departmentId;


    public static User dtoChangeEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .account(userDto.getAccount())
                .password(userDto.getPassword())
                .userName(userDto.getUserName())
                .phone(userDto.getPhone())
                .accountType(userDto.getAccountType())
                .lastLoginTime(userDto.getLastLoginTime())
                .build();
    }

    public static UserDto entityChangeDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .accountType(user.getAccountType())
                .enabledState(user.getEnabledState())
                .lastLoginTime(user.getLastLoginTime())
                .build();
    }

    public static List<UserDto> entityChangeListDto(List<User> userList) {
        return userList.stream().map(UserDto::entityChangeDto).collect(Collectors.toList());
    }
}
