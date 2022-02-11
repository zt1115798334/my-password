package com.zt.mypassword.mysql.repo;

import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.enums.DeleteState;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 17:29
 * description:
 */
public interface UserRepository extends CrudRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    Optional<User> findByIdAndDeleteState(Long id, DeleteState deleteState);

    Optional<User> findByAccountAndDeleteState(String account, DeleteState deleteState);
}
