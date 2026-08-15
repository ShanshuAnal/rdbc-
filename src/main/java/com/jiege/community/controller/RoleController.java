package com.jiege.community.controller;

import com.jiege.community.dto.PageInfo;
import com.jiege.community.dto.RoleCreateRequestBody;
import com.jiege.community.dto.RoleStatusUpdateRequestBody;
import com.jiege.community.dto.RoleUpdateRequestBody;
import com.jiege.community.entity.HttpResponse;
import com.jiege.community.service.RoleService;
import com.jiege.community.vo.RoleVO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/15 23:45
 * @Description: 角色管理
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 新增角色
     *
     * @param requestBody 角色信息
     * @return 响应
     */
    @PostMapping
    public ResponseEntity<HttpResponse<RoleVO>> addRole(@RequestBody @Valid RoleCreateRequestBody requestBody) {
        RoleVO roleVO = roleService.addRole(requestBody);
        return ResponseEntity.ok(HttpResponse.success(roleVO));
    }

    /**
     * 修改角色
     *
     * @param requestBody 修改信息
     * @return 响应
     */
    @PutMapping
    public ResponseEntity<HttpResponse<RoleVO>> updateRole(@RequestBody @Valid RoleUpdateRequestBody requestBody) {
        RoleVO roleVO = roleService.updateRole(requestBody);
        return ResponseEntity.ok(HttpResponse.success(roleVO));
    }

    /**
     * 删除角色
     *
     * @param roleId roleId
     * @return 响应
     */
    @DeleteMapping("/{roleId}")
    public ResponseEntity<HttpResponse<Void>> deleteRole(@PathVariable String roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok(HttpResponse.success());
    }

    /**
     * 查询角色
     *
     * @param roleId roleId
     * @return 响应
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<HttpResponse<RoleVO>> getRole(@PathVariable String roleId) {
        RoleVO roleVO = roleService.getRoleById(roleId);
        return ResponseEntity.ok(HttpResponse.success(roleVO));
    }

    /**
     * 启用/禁用角色
     *
     * @param roleId      roleId
     * @param requestBody 状态(0-禁用 1-启用)
     * @return 响应
     */
    @PutMapping("/{roleId}/status")
    public ResponseEntity<HttpResponse<Void>> updateStatus(@PathVariable String roleId,
                                                           @RequestBody @Valid RoleStatusUpdateRequestBody requestBody) {
        roleService.updateStatus(roleId, requestBody.getStatus());
        return ResponseEntity.ok(HttpResponse.success());
    }

    /**
     * 分页查询角色列表（分页参数走 query string：GET 请求不能带 body，Tomcat 会丢弃）
     *
     * @param page   页码，从 1 开始
     * @param size   每页条数，默认 10
     * @param lastId 游标：上一页最后一条记录的 id，page > 1 时必传
     * @return 角色列表
     */
    @GetMapping("/list")
    public ResponseEntity<HttpResponse<List<RoleVO>>> listRoles(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(required = false) Long lastId) {
        PageInfo pageInfo = new PageInfo(page, size, lastId);
        List<RoleVO> roles = roleService.getRoleList(pageInfo);
        return ResponseEntity.ok(HttpResponse.success(roles));
    }
}
