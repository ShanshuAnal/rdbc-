package com.jiege.community.dao;

import com.jiege.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/15 4:54
 * @Description:
 */
@Mapper
public interface UserDao {

    int insert(User user);

    User selectById(@Param("id") Long id);

    User selectByUserId(@Param("userId") String userId);

    User selectByUsername(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    User selectByPhone(@Param("phone") String phone);

    List<User> getUserList(@Param("pageSize") Integer pageSize);

    List<User> getUserListByCursor(@Param("lastId") Long lastId, @Param("pageSize") Integer pageSize);

    int updateUser(User user);

    int deleteUser(@Param("userId") String userId);
}