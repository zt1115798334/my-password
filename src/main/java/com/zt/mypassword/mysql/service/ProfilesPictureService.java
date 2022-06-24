package com.zt.mypassword.mysql.service;

import com.blazebit.persistence.PagedList;
import com.zt.mypassword.dto.PageDto;
import com.zt.mypassword.dto.ProfilesPictureDto;
import com.zt.mypassword.mysql.entity.ProfilesPicture;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProfilesPictureService {

    ///////////////////////////////////////////////////////////////////////////
    // 增加
    ///////////////////////////////////////////////////////////////////////////
    ProfilesPicture saveProfilesPicture(Long userId, MultipartFile multipartFile);

    ///////////////////////////////////////////////////////////////////////////
    // 修改
    ///////////////////////////////////////////////////////////////////////////
    ProfilesPicture modifyProfilesPictureEnabledStateOn(Long id, Long userId);

    void modifyProfilesPictureEnabledStateOff(Long userId);

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////
    void deleteProfilesPicture(Long id, Long userId);
    ///////////////////////////////////////////////////////////////////////////
    // 查询
    ///////////////////////////////////////////////////////////////////////////

    Optional<ProfilesPicture> findProfilesPictureEnabledStateOn(Long userId);

    Optional<ProfilesPicture> findProfilesPictureEnabledStateOn(Long id, Long userId);

    ProfilesPicture findProfilesPicture(Long id);

    PagedList<ProfilesPictureDto> findProfilesPicturePage(Long userId, PageDto pageDto);
}
