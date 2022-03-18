package com.zt.mypassword.mysql.service.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.zt.mypassword.dto.PageDto;
import com.zt.mypassword.dto.SafeDepositBoxDto;
import com.zt.mypassword.dto.SearchSafeDepositBoxDto;
import com.zt.mypassword.enums.DeleteState;
import com.zt.mypassword.exception.custom.OperationException;
import com.zt.mypassword.mysql.entity.QSafeDepositBox;
import com.zt.mypassword.mysql.entity.SafeDepositBox;
import com.zt.mypassword.mysql.repo.SafeDepositBoxRepository;
import com.zt.mypassword.mysql.service.SafeDepositBoxService;
import com.zt.mypassword.mysql.utils.PageUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2022/2/16
 * description:
 */
@AllArgsConstructor
@Service
public class SafeDepositBoxServiceImpl implements SafeDepositBoxService {

    private SafeDepositBoxRepository safeDepositBoxRepository;

    private final EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public SafeDepositBox saveSafeDepositBox(SafeDepositBox safeDepositBox) {
        Long id = safeDepositBox.getId();
        if (id != null && id != 0L) {
            Optional<SafeDepositBox> safeDepositBoxOptional = safeDepositBoxRepository.findById(id);
            SafeDepositBox safeDepositBoxDb = safeDepositBoxOptional.orElseThrow(() -> new OperationException("已删除"));
            safeDepositBoxDb.setSafeName(safeDepositBox.getSafeName());
            safeDepositBoxDb.setSafeAccount(safeDepositBox.getSafeAccount());
            safeDepositBoxDb.setSafePassword(safeDepositBox.getSafePassword());
            return safeDepositBoxRepository.save(safeDepositBoxDb);
        } else {
            safeDepositBox.setCreatedTime(LocalDateTime.now());
            safeDepositBox.setDeleteState(DeleteState.UN_DELETE);
            return safeDepositBoxRepository.save(safeDepositBox);
        }
    }

    @Override
    public void deleteSafeDepositBox(Long id, Long userId) {
        Optional<SafeDepositBox> safeDepositBoxOptional = safeDepositBoxRepository.findById(id);
        safeDepositBoxOptional
                .filter(safeDepositBox -> safeDepositBox.getUserId().equals(userId))
                .ifPresent(safeDepositBox -> {
                    safeDepositBox.setDeleteState(DeleteState.DELETE);
                    safeDepositBoxRepository.save(safeDepositBox);
                });
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Optional<SafeDepositBox> findByIdNotDel(Long id) {
        return safeDepositBoxRepository.findByIdAndDeleteState(id, DeleteState.UN_DELETE);
    }

    @Override
    public SafeDepositBox findSafeDepositBox(Long id, Long userId) {
        return this.findByIdNotDel(id)
                .filter(safeDepositBox -> safeDepositBox.getUserId().equals(userId))
                .orElseThrow(() -> new OperationException("不存在"));

    }

    @Override
    public PagedList<SafeDepositBox> findSafeDepositBoxPage(Long userId,SearchSafeDepositBoxDto searchSafeDepositBoxDto) {
        QSafeDepositBox qSafeDepositBox = QSafeDepositBox.safeDepositBox;
        BlazeJPAQuery<SafeDepositBox> jpaQuery = new BlazeJPAQuery<Tuple>(entityManager, criteriaBuilderFactory)
                .from(qSafeDepositBox)
                .select(Projections.bean(
                        SafeDepositBox.class,
                        qSafeDepositBox.id,
                        qSafeDepositBox.safeName,
                        qSafeDepositBox.safeAccount,
                        qSafeDepositBox.safePassword,
                        qSafeDepositBox.depositType,
                        qSafeDepositBox.updatedTime)
                );
        jpaQuery.where(qSafeDepositBox.deleteState.eq(DeleteState.UN_DELETE));
        jpaQuery.where(qSafeDepositBox.userId.eq(userId));
        jpaQuery.orderBy(qSafeDepositBox.createdTime.desc());
        jpaQuery.orderBy(qSafeDepositBox.id.asc());
        return jpaQuery.fetchPage(PageUtils.getOffset(searchSafeDepositBoxDto.getPageNumber(), searchSafeDepositBoxDto.getPageSize()), searchSafeDepositBoxDto.getPageSize());
    }
}
