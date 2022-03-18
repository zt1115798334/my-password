package com.zt.mypassword.mysql.service;

import com.blazebit.persistence.PagedList;
import com.zt.mypassword.dto.SearchUserDto;
import com.zt.mypassword.dto.UserDto;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.enums.EnabledState;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 15:10
 * description:
 */
public interface UserService {

    ///////////////////////////////////////////////////////////////////////////
    // 增加
    ///////////////////////////////////////////////////////////////////////////
    User saveUser(User user,Long departmentId);

    ///////////////////////////////////////////////////////////////////////////
    // 修改
    ///////////////////////////////////////////////////////////////////////////
    void updateLastLoginTime(Long id);

    void modifyUserEnabledState(Long id, EnabledState enabledState);

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////
    void deleteUserById(Long id);

    ///////////////////////////////////////////////////////////////////////////
    // 查询
    ///////////////////////////////////////////////////////////////////////////
    Optional<User> findByIdNotDel(Long id);

    Optional<User> findByAccountNotDel(String account);

    UserDto findUser(Long id);

    PagedList<UserDto> findUserPage(SearchUserDto searchUserDto);

}
