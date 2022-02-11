package com.zt.mypassword.mysql.repo;

import com.zt.mypassword.mysql.entity.VerificationCodeLog;
import org.springframework.data.repository.CrudRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/03/20 09:37
 * description:
 */
public interface VerificationCodeLogRepository extends CrudRepository<VerificationCodeLog, Long> {

}
