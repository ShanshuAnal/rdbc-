package com.jiege.community.service.impl;

import com.jiege.community.dao.RoleDao;
import com.jiege.community.dto.PageInfo;
import com.jiege.community.dto.RoleCreateRequestBody;
import com.jiege.community.dto.RoleUpdateRequestBody;
import com.jiege.community.entity.Role;
import com.jiege.community.common.ResponseCode;
import com.jiege.community.enums.RoleStatus;
import com.jiege.community.common.exception.BusinessException;
import com.jiege.community.service.RoleService;
import com.jiege.community.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @Author: 19599
 * @Date: 2026/8/15 23:44
 * @Description: 角色服务实现
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public RoleVO addRole(RoleCreateRequestBody request) {
        // 角色标识是否重复
        if (roleDao.selectByRoleKey(request.getRoleKey()) != null) {
            throw new BusinessException(ResponseCode.ROLE_KEY_EXISTS);
        }
        // 角色名称是否重复
        if (roleDao.selectByRoleName(request.getRoleName()) != null) {
            throw new BusinessException(ResponseCode.ROLE_NAME_EXISTS);
        }
        Role role = buildRole(request);
        roleDao.insert(role);
        return new RoleVO(role);
    }


    @Override
    public RoleVO getRoleById(String roleId) {
        Role role = roleDao.selectByRoleId(roleId);
        if (role == null) {
            throw new BusinessException(ResponseCode.ROLE_NOT_EXISTS);
        }
        return new RoleVO(role);
    }

    @Override
    public RoleVO updateRole(RoleUpdateRequestBody request) {
        Role role = roleDao.selectByRoleId(request.getRoleId());
        if (role == null) {
            throw new BusinessException(ResponseCode.ROLE_NOT_EXISTS);
        }
        // 名称/标识被其他角色占用则不允许
        Role byKey = roleDao.selectByRoleKey(request.getRoleKey());
        if (byKey != null && !byKey.getRoleId().equals(request.getRoleId())) {
            throw new BusinessException(ResponseCode.ROLE_KEY_EXISTS);
        }
        Role byName = roleDao.selectByRoleName(request.getRoleName());
        if (byName != null && !byName.getRoleId().equals(request.getRoleId())) {
            throw new BusinessException(ResponseCode.ROLE_NAME_EXISTS);
        }
        role.setRoleName(request.getRoleName());
        role.setRoleKey(request.getRoleKey());
        role.setDescription(request.getDescription());
        role.setUpdateTime(LocalDateTime.now());
        roleDao.updateRole(role);
        return new RoleVO(role);
    }

    @Override
    public void updateStatus(String roleId, Integer status) {
        Role role = roleDao.selectByRoleId(roleId);
        if (role == null) {
            throw new BusinessException(ResponseCode.ROLE_NOT_EXISTS);
        }
        roleDao.updateStatus(roleId, status);
    }

    @Override
    public void deleteRole(String roleId) {
        Role role = roleDao.selectByRoleId(roleId);
        if (role == null) {
            throw new BusinessException(ResponseCode.ROLE_NOT_EXISTS);
        }
        // 有用户绑定该角色时禁止删除
        if (roleDao.countUserRole(roleId) > 0) {
            throw new BusinessException(ResponseCode.ROLE_HAS_USER);
        }
        roleDao.deleteRole(roleId);
    }

    @Override
    public List<RoleVO> getRoleList(PageInfo pageInfo) {
        List<Role> roles;
        if (pageInfo.getPage() == 1) {
            // 第一页：无游标
            roles = roleDao.getRoleList(pageInfo.getSize());
        } else {
            // 第 N 页：必须携带上一页最后一条记录的 id 作为游标
            if (pageInfo.getLastId() == null) {
                throw new BusinessException(ResponseCode.PARAM_ERROR);
            }
            roles = roleDao.getRoleListByCursor(pageInfo.getLastId(), pageInfo.getSize());
        }
        return roles.stream().map(RoleVO::new).toList();
    }


    private Role buildRole(RoleCreateRequestBody request) {
        return Role.builder()
                .roleId(UUID.randomUUID().toString())
                .roleName(request.getRoleName())
                .roleKey(request.getRoleKey())
                .description(request.getDescription())
                .status(RoleStatus.ENABLED.getStatus())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
