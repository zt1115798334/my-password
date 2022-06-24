package com.zt.mypassword.controller;

import com.alibaba.fastjson.annotation.JSONField;
import com.blazebit.persistence.PagedList;
import com.zt.mypassword.aop.SaveLog;
import com.zt.mypassword.base.controller.BaseResultMessage;
import com.zt.mypassword.base.controller.ResultMessage;
import com.zt.mypassword.base.user.CurrentUser;
import com.zt.mypassword.dto.*;
import com.zt.mypassword.mysql.entity.ProfilesPicture;
import com.zt.mypassword.mysql.entity.UserLog;
import com.zt.mypassword.mysql.service.ProfilesPictureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/14 14:15
 * description:
 */
@Api(tags = "头像")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("api/profilesPicture")
public class ProfilesPictureController extends BaseResultMessage implements CurrentUser {

    private final ProfilesPictureService profilesPictureService;

    @ApiOperation(value = "保存用户头像")
    @PostMapping(value = "saveProfilesPicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "保存用户头像", encryption = true)
    public ResultMessage saveProfilesPicture(@RequestParam("file") @JSONField(serialize = false) MultipartFile multipartFile) {
        ProfilesPicture profilesPicture = profilesPictureService.saveProfilesPicture(getCurrentUserId(), multipartFile);
        return success(ProfilesPictureDto.entityChangeDto(profilesPicture));
    }

    @ApiOperation(value = "修改头像开启状态")
    @PostMapping(value = "modifyProfilesPictureEnabledState", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "修改头像开启状态")
    public ResultMessage modifyProfilesPictureEnabledState(@NotNull(message = "id不能为空") @RequestParam Long id) {
        ProfilesPicture profilesPicture = profilesPictureService.modifyProfilesPictureEnabledStateOn(id, getCurrentUserId());
        return success(ProfilesPictureDto.entityChangeDto(profilesPicture));
    }

    @ApiOperation(value = "删除头像")
    @PostMapping(value = "deleteProfilesPicture", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "删除头像")
    public ResultMessage deleteProfilesPicture(@NotNull(message = "id不能为空") @RequestParam Long id) {
        profilesPictureService.deleteProfilesPicture(id,getCurrentUserId());
        return success();
    }


    @ApiOperation(value = "分页查找头像")
    @PostMapping(value = "findUserPage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage findUserPage(@Validated @RequestBody PageDto pageDto) {
        PagedList<ProfilesPictureDto> profilesPictureDtoPagedList = profilesPictureService.findProfilesPicturePage(getCurrentUserId(),pageDto);
        return success(pageDto.getPageNumber(), pageDto.getPageSize(), profilesPictureDtoPagedList.getTotalSize(), profilesPictureDtoPagedList);
    }


}
