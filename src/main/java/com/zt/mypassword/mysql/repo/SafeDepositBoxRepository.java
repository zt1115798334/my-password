package com.zt.mypassword.mysql.repo;

import com.zt.mypassword.enums.DeleteState;
import com.zt.mypassword.mysql.entity.SafeDepositBox;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2022/2/16
 * description:
 */
public interface SafeDepositBoxRepository extends CrudRepository<SafeDepositBox, Long>, JpaSpecificationExecutor<SafeDepositBox> {
    Optional<SafeDepositBox> findByIdAndDeleteState(Long id, DeleteState deleteState);

}
