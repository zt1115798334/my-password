package com.zt.mypassword.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zt.mypassword.mysql.entity.UserLog;
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
 * @author zhang tong
 * date: 2018/08/20 13:34
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLogDto  implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Long id;
    /**
     * 操作员ID
     */
    @ApiModelProperty(value = "操作员ID", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Long userId;
    /**
     * 操作员姓名
     */
    @ApiModelProperty(value = "操作员姓名", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String name;
    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String type;
    /**
     * 操作详情
     */
    @ApiModelProperty(value = "操作详情", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String content;
    /**
     * IP
     */
    @ApiModelProperty(value = "IP", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private String ip;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    @ApiModelProperty(value = "创建时间", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private LocalDateTime time;

    public static UserLogDto entityChangeDto(UserLog userLog) {
        return UserLogDto.builder()
                .id(userLog.getId())
                .userId(userLog.getUserId())
                .name(userLog.getName())
                .type(userLog.getType())
                .content(userLog.getContent())
                .ip(userLog.getIp())
                .time(userLog.getTime())
                .build();
    }

    public static List<UserLogDto> entityChangeListDto(List<UserLog> userLogList) {
        return userLogList.stream().map(UserLogDto::entityChangeDto).collect(Collectors.toList());
    }

}
