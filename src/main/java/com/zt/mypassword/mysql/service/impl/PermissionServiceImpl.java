package com.zt.mypassword.mysql.service.impl;

import com.zt.mypassword.mysql.entity.Permission;
import com.zt.mypassword.mysql.repo.PermissionRepository;
import com.zt.mypassword.mysql.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/02/27 18:10
 * description:
 */
@AllArgsConstructor
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAllPermissions() {
        return (List<Permission>) permissionRepository.findAll();
    }

    @Override
    public List<Permission> findPermissionList(List<Long> ids) {
        return (List<Permission>) permissionRepository.findAllById(ids);
    }
}
