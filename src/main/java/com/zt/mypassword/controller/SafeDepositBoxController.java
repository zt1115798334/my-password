package com.zt.mypassword.controller;

import com.blazebit.persistence.PagedList;
import com.zt.mypassword.aop.SaveLog;
import com.zt.mypassword.base.controller.BaseResultMessage;
import com.zt.mypassword.base.controller.ResultMessage;
import com.zt.mypassword.base.user.CurrentUser;
import com.zt.mypassword.dto.SafeDepositBoxDto;
import com.zt.mypassword.dto.SearchSafeDepositBoxDto;
import com.zt.mypassword.mysql.entity.SafeDepositBox;
import com.zt.mypassword.mysql.service.SafeDepositBoxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2022/3/17
 * description:
 */
@Api(tags = "用户")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("api/safeDepositBox")
public class SafeDepositBoxController extends BaseResultMessage implements CurrentUser {

    private final SafeDepositBoxService safeDepositBoxService;

    @ApiOperation(value = "保存保险箱")
    @PostMapping(value = "saveSafeDepositBox", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "保存保险箱", encryption = true)
    public ResultMessage saveSafeDepositBox(@RequestBody SafeDepositBoxDto safeDepositBoxDto) {
        SafeDepositBox safeDepositBox = safeDepositBoxService.saveSafeDepositBox(SafeDepositBoxDto.dtoChangeEntity(safeDepositBoxDto, getCurrentUser().getRsaPrivateKey()));
        return success(SafeDepositBoxDto.entityChangeDto(safeDepositBox));
    }

    @ApiOperation(value = "删除保险箱")
    @PostMapping(value = "deleteUser", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "删除保险箱")
    public ResultMessage deleteUser(@NotNull(message = "id不能为空") @RequestParam Long id) {
        safeDepositBoxService.deleteSafeDepositBox(id, getCurrentUserId());
        return success();
    }

    @ApiOperation(value = "查找保险箱")
    @PostMapping(value = "findUser", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage findUser(@NotNull(message = "id不能为空") @RequestParam Long id) {
        SafeDepositBox safeDepositBox = safeDepositBoxService.findSafeDepositBox(id, getCurrentUserId());
        return success(SafeDepositBoxDto.entityChangeDto(safeDepositBox));
    }

    @ApiOperation(value = "分页保险箱")
    @PostMapping(value = "findSafeDepositBoxPage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage findSafeDepositBoxPage(@Validated @RequestBody SearchSafeDepositBoxDto searchSafeDepositBoxDto) {
        PagedList<SafeDepositBox> safeDepositBoxPage = safeDepositBoxService.findSafeDepositBoxPage(getCurrentUserId(), searchSafeDepositBoxDto);
        return success(searchSafeDepositBoxDto.getPageNumber(), searchSafeDepositBoxDto.getPageSize(),
                safeDepositBoxPage.getTotalSize(), SafeDepositBoxDto.entityChangeListDto(safeDepositBoxPage));
    }
}
