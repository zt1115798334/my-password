package com.zt.mypassword.mysql.service.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.zt.mypassword.dto.PageDto;
import com.zt.mypassword.dto.ProfilesPictureDto;
import com.zt.mypassword.enums.DeleteState;
import com.zt.mypassword.enums.EnabledState;
import com.zt.mypassword.exception.custom.OperationException;
import com.zt.mypassword.mysql.entity.ProfilesPicture;
import com.zt.mypassword.mysql.entity.QProfilesPicture;
import com.zt.mypassword.mysql.entity.QSafeDepositBox;
import com.zt.mypassword.mysql.entity.SafeDepositBox;
import com.zt.mypassword.mysql.repo.ProfilesPictureRepository;
import com.zt.mypassword.mysql.service.ProfilesPictureService;
import com.zt.mypassword.mysql.utils.PageUtils;
import com.zt.mypassword.properties.FileProperties;
import com.zt.mypassword.utils.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProfilesPictureServiceImpl implements ProfilesPictureService {

    private final FileProperties fileProperties;

    private final ProfilesPictureRepository profilesPictureRepository;

    private final EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ProfilesPicture saveProfilesPicture(Long userId, MultipartFile multipartFile) {
        modifyProfilesPictureEnabledStateOff(userId);
        String diskPath = FileUtils.writeDisk(multipartFile, Paths.get(fileProperties.getProfilesPicturePath()));
        ProfilesPicture profilesPicture = ProfilesPicture.builder()
                .userId(userId)
                .path(diskPath)
                .enabledState(EnabledState.ON)
                .deleteState(DeleteState.UN_DELETE)
                .createdTime(LocalDateTime.now()).build();
        return profilesPictureRepository.save(profilesPicture);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public ProfilesPicture modifyProfilesPictureEnabledStateOn(Long id, Long userId) {
        modifyProfilesPictureEnabledStateOff(userId);
        Optional<ProfilesPicture> profilesPictureOptional = profilesPictureRepository.findById(id);
        ProfilesPicture profilesPicture = profilesPictureOptional.orElseThrow(() -> new RuntimeException("no exits"));
            profilesPicture.setEnabledState(EnabledState.ON);
         return profilesPictureRepository.save(profilesPicture);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public void modifyProfilesPictureEnabledStateOff(Long userId) {
        Optional<ProfilesPicture> profilesPictureOptional = this.findProfilesPictureEnabledStateOn(userId);
        profilesPictureOptional.ifPresent(profilesPicture -> {
            profilesPicture.setEnabledState(EnabledState.OFF);
            profilesPictureRepository.save(profilesPicture);
        });
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteProfilesPicture(Long id, Long userId) {
        Optional<ProfilesPicture> profilesPictureOptional = this.findProfilesPictureEnabledStateOn(id, userId);
        profilesPictureOptional.ifPresent(profilesPicture -> {
            profilesPicture.setDeleteState(DeleteState.UN_DELETE);
            profilesPictureRepository.save(profilesPicture);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfilesPicture> findProfilesPictureEnabledStateOn(Long userId) {
        return profilesPictureRepository.findByUserIdAndEnabledState(userId, EnabledState.ON);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfilesPicture> findProfilesPictureEnabledStateOn(Long id, Long userId) {
        return profilesPictureRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public ProfilesPicture findProfilesPicture(Long id) {
        return profilesPictureRepository.findById(id).orElseThrow(() -> new OperationException("不存在"));
    }

    @Override
    public PagedList<ProfilesPictureDto> findProfilesPicturePage(Long userId, PageDto pageDto) {
        QProfilesPicture qProfilesPicture = QProfilesPicture.profilesPicture;
        BlazeJPAQuery<ProfilesPictureDto> jpaQuery = new BlazeJPAQuery<Tuple>(entityManager, criteriaBuilderFactory)
                .from(qProfilesPicture)
                .select(Projections.bean(
                        ProfilesPictureDto.class,
                        qProfilesPicture.id,
                        qProfilesPicture.path)
                );
        jpaQuery.where(qProfilesPicture.deleteState.eq(DeleteState.UN_DELETE));
        jpaQuery.where(qProfilesPicture.userId.eq(userId));
        jpaQuery.orderBy(qProfilesPicture.createdTime.desc());
        return jpaQuery.fetchPage(PageUtils.getOffset(pageDto.getPageNumber(), pageDto.getPageSize()), pageDto.getPageSize());
    }
}
