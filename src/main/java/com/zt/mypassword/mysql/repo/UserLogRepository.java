package com.zt.mypassword.mysql.repo;

import com.zt.mypassword.mysql.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/08/20 13:34
 * description:
 */
public interface UserLogRepository extends CrudRepository<UserLog, Long>, JpaRepository<UserLog,Long> {

}
