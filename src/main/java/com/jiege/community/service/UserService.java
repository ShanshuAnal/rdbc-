package com.jiege.community.service;

import com.jiege.community.dto.PageInfo;
import com.jiege.community.dto.UserCreateRequestBody;
import com.jiege.community.dto.UserUpdateRequestBody;
import com.jiege.community.vo.UserVO;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/15 4:52
 * @Description:
 */
public interface UserService {
    /**
     * 添加用户
     *
     * @param userCreateRequestBody 用户信息
     * @return 添加结果
     */
    UserVO addUser(UserCreateRequestBody userCreateRequestBody);

    /**
     * 根据id查询用户
     *
     * @param userId userId
     * @return 用户
     */
    UserVO getUserById(String userId);

    /**
     * 修改用户
     *
     * @param userUpdateRequestBody 修改信息
     * @return 修改结果
     */
    UserVO updateUser(UserUpdateRequestBody userUpdateRequestBody);

    /**
     * 删除用户
     *
     * @param userId userId
     */
    void deleteUser(String userId);

    /**
     * 查询用户列表
     *
     * @param pageInfo 分页信息
     * @return 用户列表
     */
    List<UserVO> getUserList(PageInfo pageInfo);

    /**
     * 注册
     * todo 以后加默认角色再扩展
     *
     * @param request 注册请求参数
     * @return 注册结果
     */
    UserVO register(UserCreateRequestBody request);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    UserVO getUserByUsername(String username);
}
