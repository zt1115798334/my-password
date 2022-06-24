package com.zt.mypassword.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zt.mypassword.mysql.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2021/10/23 10:51
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissionDto implements Serializable {
    /**
     *
     */
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
     * 前台地址
     */
    private String path;


    public static PermissionDto entityChangeDto(Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .permissionName(permission.getPermissionName())
                .permissionNickname(permission.getPermissionNickname())
                .permission(permission.getPermission())
                .permissionType(permission.getPermissionType())
                .path(permission.getPath())
                .build();
    }

    public static List<PermissionDto> entityChangeListDto(List<Permission> permissionList) {
        return permissionList.stream().map(PermissionDto::entityChangeDto).collect(Collectors.toList());
    }


}
