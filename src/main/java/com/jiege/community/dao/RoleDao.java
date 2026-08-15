package com.jiege.community.dao;

import com.jiege.community.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/15 23:42
 * @Description: 角色 DAO
 */
@Mapper
public interface RoleDao {

    int insert(Role role);

    Role selectByRoleId(@Param("roleId") String roleId);

    Role selectByRoleKey(@Param("roleKey") String roleKey);

    Role selectByRoleName(@Param("roleName") String roleName);

    List<Role> getRoleList(@Param("pageSize") Integer pageSize);

    List<Role> getRoleListByCursor(@Param("lastId") Long lastId, @Param("pageSize") Integer pageSize);

    int updateRole(Role role);

    int updateStatus(@Param("roleId") String roleId, @Param("status") Integer status);

    int deleteRole(@Param("roleId") String roleId);

    int countUserRole(@Param("roleId") String roleId);
}
