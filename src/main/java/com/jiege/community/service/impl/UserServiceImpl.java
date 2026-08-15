package com.jiege.community.service.impl;

import com.jiege.community.dao.UserDao;
import com.jiege.community.dto.LoginRequestBody;
import com.jiege.community.dto.PageInfo;
import com.jiege.community.dto.UserCreateRequestBody;
import com.jiege.community.dto.UserUpdateRequestBody;
import com.jiege.community.entity.User;
import com.jiege.community.enums.ResponseCode;
import com.jiege.community.enums.UserStatus;
import com.jiege.community.exception.BusinessException;
import com.jiege.community.service.UserService;
import com.jiege.community.vo.LoginVO;
import com.jiege.community.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @Author: 19599
 * @Date: 2026/8/15 4:53
 * @Description:
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao,
                           PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserVO addUser(UserCreateRequestBody userCreateRequestBody) {
        // 用户名是否重复
        User user = userDao.selectByUsername(userCreateRequestBody.getUsername());
        if (user != null) {
            throw new BusinessException(ResponseCode.USERNAME_EXISTS);
        }
        // 邮箱是否重复
        user = userDao.selectByEmail(userCreateRequestBody.getEmail());
        if (user != null) {
            throw new BusinessException(ResponseCode.EMAIL_EXISTS);
        }
        // 手机号是否重复
        user = userDao.selectByPhone(userCreateRequestBody.getPhone());
        if (user != null) {
            throw new BusinessException(ResponseCode.PHONE_EXISTS);
        }
        // 构造User
        User newUser = buildNewUser(userCreateRequestBody);
        // 保存
        userDao.insert(newUser);
        // 返回VO
        return new UserVO(newUser);
    }


    @Override
    public UserVO getUserById(String userId) {
        User user = userDao.selectByUserId(userId);
        if (user == null) {
            throw new BusinessException(ResponseCode.USER_NOT_EXISTS);
        }
        if (user.getStatus().equals(UserStatus.DELETE.getStatus())) {
            throw new BusinessException(ResponseCode.USER_NOT_EXISTS);
        }
        return new UserVO(user);
    }

    @Override
    public UserVO updateUser(UserUpdateRequestBody request) {
        User user = userDao.selectByUserId(request.getUserId());
        if (user == null) {
            throw new BusinessException(ResponseCode.USER_NOT_EXISTS);
        }
        updateUserAttribute(request, user);
        userDao.updateUser(user);
        return new UserVO(user);
    }


    @Override
    public void deleteUser(String userId) {
        User user = userDao.selectByUserId(userId);
        if (user == null) {
            throw new BusinessException(ResponseCode.USER_NOT_EXISTS);
        }
        userDao.deleteUser(userId);
    }

    @Override
    public List<UserVO> getUserList(PageInfo pageInfo) {
        List<User> users;
        if (pageInfo.getPage() == 1) {
            // 第一页：无游标
            users = userDao.getUserList(pageInfo.getSize());
        } else {
            // 第 N 页：必须携带上一页最后一条记录的 id 作为游标
            if (pageInfo.getLastId() == null) {
                throw new BusinessException(ResponseCode.PARAM_ERROR);
            }
            users = userDao.getUserListByCursor(pageInfo.getLastId(), pageInfo.getSize());
        }
        return users.stream().map(UserVO::new).toList();
    }

    @Override
    public UserVO register(UserCreateRequestBody request) {
        return addUser(request);
    }

    @Override
    public UserVO getUserByUsername(String username) {
        User user = userDao.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(ResponseCode.USER_NOT_EXISTS);
        }
        return new UserVO(user);
    }


    private User buildNewUser(UserCreateRequestBody userCreateRequestBody) {
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .username(userCreateRequestBody.getUsername())
                .password(passwordEncoder.encode(userCreateRequestBody.getPassword()))
                .email(userCreateRequestBody.getEmail())
                .phone(userCreateRequestBody.getPhone())
                .status(UserStatus.NORMAL.getStatus())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }

    private void updateUserAttribute(UserUpdateRequestBody request, User user) {
        user.setUserId(request.getUserId());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUpdateTime(LocalDateTime.now());
    }
}
