package com.zt.mypassword.mysql.service.impl;

import com.zt.mypassword.dto.PageDto;
import com.zt.mypassword.mysql.entity.UserLog;
import com.zt.mypassword.mysql.repo.UserLogRepository;
import com.zt.mypassword.mysql.service.UserLogService;
import com.zt.mypassword.mysql.utils.PageUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/08/20 13:34
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserLogServiceImpl implements UserLogService {

    private final UserLogRepository userLogRepository;

    @Override
    public void saveUserLog(UserLog userLog) {
        userLogRepository.save(userLog);
    }

    @Override
    public Page<UserLog> findUserLogPage(PageDto pageDto) {
        PageRequest pageRequest = PageUtils.buildPageRequest(pageDto, Sort.by("time").descending());
        return userLogRepository.findAll(pageRequest);
    }
}
