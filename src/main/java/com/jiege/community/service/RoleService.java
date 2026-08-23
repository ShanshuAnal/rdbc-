package com.jiege.community.service;

import com.jiege.community.dto.PageInfo;
import com.jiege.community.dto.RoleCreateRequestBody;
import com.jiege.community.dto.RoleUpdateRequestBody;
import com.jiege.community.vo.RoleVO;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/15 23:43
 * @Description: 角色服务
 */
public interface RoleService {

    /**
     * 新增角色
     *
     * @param request 新增参数
     * @return 角色
     */
    RoleVO addRole(RoleCreateRequestBody request);

    /**
     * 根据 roleId 查询角色
     *
     * @param roleId roleId
     * @return 角色
     */
    RoleVO getRoleById(String roleId);

    /**
     * 修改角色
     *
     * @param request 修改参数
     * @return 角色
     */
    RoleVO updateRole(RoleUpdateRequestBody request);

    /**
     * 启用/禁用角色
     *
     * @param roleId roleId
     * @param status 0-禁用 1-启用
     */
    void updateStatus(String roleId, Integer status);

    /**
     * 删除角色（有用户绑定时禁止）
     *
     * @param roleId roleId
     */
    void deleteRole(String roleId);

    /**
     * 分页查询角色列表
     *
     * @param pageInfo 分页信息
     * @return 角色列表
     */
    List<RoleVO> getRoleList(PageInfo pageInfo);

    /**
     * 根据角色ID列表查询已存在的角色
     *
     * @param roleIds 角色ID列表
     * @return 角色ID列表
     */
    List<String> selectExistingRole(List<String> roleIds);
}
