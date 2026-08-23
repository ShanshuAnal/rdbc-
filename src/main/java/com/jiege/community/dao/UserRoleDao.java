package com.jiege.community.dao;

import com.jiege.community.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/23 22:58
 * @Description:
 */
@Mapper
public interface UserRoleDao {

    /**
     * 批量插入用户角色
     *
     * @param userRoleList 用户角色列表
     * @return 受影响的行数
     */
    int batchInsert(@Param("userRoleList") List<UserRole> userRoleList);

    /**
     * 删除用户角色
     *
     * @param userId 用户ID
     */
    void deleteByUserID(@Param("userId") int userId);


    /**
     * 根据用户ID查询所有角色
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Integer> selectRoles(@Param("userId") int userId);

}
