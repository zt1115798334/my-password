package com.zt.mypassword.mysql.repo;

import com.zt.mypassword.enums.EnabledState;
import com.zt.mypassword.mysql.entity.ProfilesPicture;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfilesPictureRepository extends CrudRepository<ProfilesPicture, Long> {

    Optional<ProfilesPicture> findByUserIdAndEnabledState(Long userId, EnabledState enabledState);

    Optional<ProfilesPicture> findByIdAndUserId(Long id,Long userId);
}
