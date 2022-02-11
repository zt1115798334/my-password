package com.zt.mypassword.mysql.repo;

import com.zt.mypassword.mysql.entity.Permission;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/02/27 18:10
 * description:
 */
public interface PermissionRepository extends PagingAndSortingRepository<Permission, Long> {


}
