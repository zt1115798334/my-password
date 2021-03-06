package com.zt.mypassword.mysql.service;

import com.blazebit.persistence.PagedList;
import com.zt.mypassword.dto.PageDto;
import com.zt.mypassword.dto.SafeDepositBoxDto;
import com.zt.mypassword.dto.SearchSafeDepositBoxDto;
import com.zt.mypassword.mysql.entity.SafeDepositBox;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2022/2/16
 * description:
 */
public interface SafeDepositBoxService {

    ///////////////////////////////////////////////////////////////////////////
    // 增加
    ///////////////////////////////////////////////////////////////////////////
    SafeDepositBox saveSafeDepositBox(SafeDepositBox safeDepositBox);
    ///////////////////////////////////////////////////////////////////////////
    // 修改
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////
    void deleteSafeDepositBox(Long id, Long userId);
    ///////////////////////////////////////////////////////////////////////////
    // 查询
    ///////////////////////////////////////////////////////////////////////////
    Optional<SafeDepositBox> findByIdNotDel(Long id);

    SafeDepositBox findSafeDepositBox(Long id, Long userId);

    PagedList<SafeDepositBox> findSafeDepositBoxPage(Long userId, SearchSafeDepositBoxDto searchSafeDepositBoxDto);
}
