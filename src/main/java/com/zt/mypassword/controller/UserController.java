package com.zt.mypassword.controller;

import com.blazebit.persistence.PagedList;
import com.zt.mypassword.aop.SaveLog;
import com.zt.mypassword.base.controller.BaseResultMessage;
import com.zt.mypassword.base.controller.ResultMessage;
import com.zt.mypassword.base.user.CurrentUser;
import com.zt.mypassword.dto.PageDto;
import com.zt.mypassword.dto.SearchUserDto;
import com.zt.mypassword.dto.UserDto;
import com.zt.mypassword.dto.UserLogDto;
import com.zt.mypassword.enums.EnabledState;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.mysql.entity.UserLog;
import com.zt.mypassword.mysql.service.UserLogService;
import com.zt.mypassword.mysql.service.UserService;
import com.zt.mypassword.service.InheritService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/14 14:15
 * description:
 */
@Api(tags = "用户")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("api/user")
public class UserController extends BaseResultMessage implements CurrentUser {

    private final UserService userService;

    private final UserLogService userLogService;

    private final InheritService inheritService;

    @ApiOperation(value = "查看当前用户")
    @PostMapping(value = "findCurrentUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage findCurrentUser() {
        UserDto userDto = userService.findUser(getCurrentUserId());
        return success(userDto);
    }

    @ApiOperation(value = "保存用户")
    @PostMapping(value = "saveUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "保存用户", encryption = true)
    public ResultMessage saveUser(@RequestBody UserDto userDto) {
        User user = userService.saveUser(UserDto.dtoChangeEntity(userDto));
        return success(UserDto.entityChangeDto(user));
    }

    @ApiOperation(value = "修改用户开启状态")
    @PostMapping(value = "modifyUserEnabledState", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "修改用户开启状态")
    public ResultMessage modifyUserEnabledState(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                @NotNull(message = "enabledState不能为空") @RequestParam EnabledState enabledState) {
        userService.modifyUserEnabledState(id, enabledState);
        return success();
    }

    @ApiOperation(value = "删除用户")
    @PostMapping(value = "deleteUser", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "删除用户")
    public ResultMessage deleteUser(@NotNull(message = "id不能为空") @RequestParam Long id) {
        userService.deleteUserById(id);
        return success();
    }

    @ApiOperation(value = "查找用户")
    @PostMapping(value = "findUser", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage findUser(@NotNull(message = "id不能为空") @RequestParam Long id) {
        UserDto userDto = userService.findUser(id);
        return success(userDto);
    }

    @ApiOperation(value = "分页查找用户")
    @PostMapping(value = "findUserPage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage findUserPage(@Validated @RequestBody SearchUserDto searchUserDto) {
        PagedList<UserDto> userDtoPagedList = userService.findUserPage(searchUserDto);
        return success(searchUserDto.getPageNumber(), searchUserDto.getPageSize(), userDtoPagedList.getTotalSize(), userDtoPagedList);
    }

    @ApiOperation(value = "分页查找用户日志")
    @PostMapping(value = "findUserLogPage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage findUserLogPage(@Validated @RequestBody PageDto pageDto) {
        Page<UserLog> userLogPage = userLogService.findUserLogPage(pageDto);
        return success(pageDto.getPageNumber(), pageDto.getPageSize(), userLogPage.getTotalElements(), UserLogDto.entityChangeListDto(userLogPage.getContent()));
    }

}
