package com.zt.mypassword.mysql.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zt.mypassword.dto.SearchUserDto;
import com.zt.mypassword.dto.UserDto;
import com.zt.mypassword.enums.AccountType;
import com.zt.mypassword.enums.DeleteState;
import com.zt.mypassword.enums.EnabledState;
import com.zt.mypassword.exception.custom.OperationException;
import com.zt.mypassword.mysql.entity.QProfilesPicture;
import com.zt.mypassword.mysql.entity.QUser;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.mysql.repo.UserRepository;
import com.zt.mypassword.mysql.service.UserService;
import com.zt.mypassword.mysql.utils.PageUtils;
import com.zt.mypassword.shiro.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 16:33
 * description:
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JPAQueryFactory jpaQueryFactory;

    private final EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public User saveUser(User user) {
        Long id = user.getId();
        if (id != null && id != 0) {
            Optional<User> userOptional = userRepository.findById(id);
            User userDb = userOptional.orElseThrow(() -> new OperationException("已删除"));
            if (StringUtils.isNotBlank(user.getPassword())) {
                String pawDES = UserUtils.getEncryptPassword(userDb.getAccount(), user.getPassword(), userDb.getSalt());
                userDb.setPassword(pawDES);
            }
            userDb.setUserName(user.getUserName());
            userDb.setPhone(user.getPhone());
            userDb.setUpdatedTime(LocalDateTime.now());
            return userRepository.save(userDb);
        } else {
            Optional<User> userOpt = userRepository.findByAccountAndDeleteState(user.getAccount(), DeleteState.UN_DELETE);
            if (userOpt.isPresent()) {
                throw new OperationException("当前用户已存在");
            }
            String salt = Base64.encode(RandomUtil.randomBytes(8));
            String pawDES = UserUtils.getEncryptPassword(user.getAccount(), user.getPassword(), salt);
            user.setPassword(pawDES);
            user.setSalt(salt);
            RSA rsa = SecureUtil.rsa();
            user.setRsaPrivateKey(rsa.getPrivateKeyBase64());
            user.setRsaPublishKey(rsa.getPublicKeyBase64());
            user.setAccountType(AccountType.ORDINARY);
            user.setEnabledState(EnabledState.ON);
            user.setCreatedTime(LocalDateTime.now());
            user.setDeleteState(DeleteState.UN_DELETE);
            return userRepository.save(user);
        }
    }


    @Override
    public void updateLastLoginTime(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    @Override
    public void modifyUserEnabledState(Long id, EnabledState enabledState) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            user.setEnabledState(enabledState);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            user.setDeleteState(DeleteState.DELETE);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Optional<User> findByIdNotDel(Long id) {
        return userRepository.findByIdAndDeleteState(id, DeleteState.UN_DELETE);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Optional<User> findByAccountNotDel(String account) {
        return userRepository.findByAccountAndDeleteState(account, DeleteState.UN_DELETE);
    }

    @Override
    public UserDto findUser(Long id) {
        QUser qUser = QUser.user;
        QProfilesPicture qProfilesPicture = QProfilesPicture.profilesPicture;
        JPAQuery<UserDto> jpaQuery = jpaQueryFactory
                .select(Projections.bean(
                        UserDto.class,
                        qUser.id,
                        qUser.account,
                        qUser.password,
                        qUser.userName,
                        qUser.phone,
                        qUser.accountType,
                        qUser.enabledState,
                        qProfilesPicture.path.as("profilesPicturePath"),
                        qUser.lastLoginTime
                )).from(qUser)
                .innerJoin(qProfilesPicture).on(qUser.id.eq(qProfilesPicture.userId))
                .where(qUser.deleteState.eq(DeleteState.UN_DELETE))
                .where(qProfilesPicture.enabledState.eq(EnabledState.ON))
                .where(qUser.id.eq(id));
        return jpaQuery.fetchOne();
    }

    @Override
    public PagedList<UserDto> findUserPage(SearchUserDto searchUserDto) {
        QUser qUser = QUser.user;
        BlazeJPAQuery<UserDto> jpaQuery = new BlazeJPAQuery<Tuple>(entityManager, criteriaBuilderFactory)
                .from(qUser)
                .select(Projections.bean(
                        UserDto.class,
                        qUser.id,
                        qUser.account,
                        qUser.userName,
                        qUser.phone,
                        qUser.accountType,
                        qUser.enabledState,
                        qUser.lastLoginTime)
                );
        jpaQuery.where(qUser.deleteState.eq(DeleteState.UN_DELETE));
        jpaQuery.where(qUser.accountType.eq(AccountType.ORDINARY));
        if (StringUtils.isNotEmpty(searchUserDto.getSearchValue())) {
            jpaQuery.where(qUser.userName.like("%" + searchUserDto.getSearchValue() + "%"));
        }
        if (searchUserDto.getEnabledState() != null) {
            jpaQuery.where(qUser.enabledState.eq(searchUserDto.getEnabledState()));
        }
        jpaQuery.orderBy(qUser.createdTime.desc());
        jpaQuery.orderBy(qUser.id.asc());
        return jpaQuery.fetchPage(PageUtils.getOffset(searchUserDto.getPageNumber(), searchUserDto.getPageSize()), searchUserDto.getPageSize());
    }
}
