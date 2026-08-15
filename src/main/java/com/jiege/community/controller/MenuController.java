package com.jiege.community.controller;

import com.jiege.community.dto.MenuCreateRequestBody;
import com.jiege.community.dto.MenuUpdateRequestBody;
import com.jiege.community.entity.HttpResponse;
import com.jiege.community.service.MenuService;
import com.jiege.community.vo.MenuVO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/16 1:45
 * @Description: 菜单管理
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 新增菜单/按钮
     *
     * @param requestBody 菜单信息
     * @return 响应
     */
    @PostMapping
    public ResponseEntity<HttpResponse<MenuVO>> addMenu(@RequestBody @Valid MenuCreateRequestBody requestBody) {
        MenuVO menuVO = menuService.addMenu(requestBody);
        return ResponseEntity.ok(HttpResponse.success(menuVO));
    }

    /**
     * 修改菜单
     *
     * @param requestBody 修改信息
     * @return 响应
     */
    @PutMapping
    public ResponseEntity<HttpResponse<MenuVO>> updateMenu(@RequestBody @Valid MenuUpdateRequestBody requestBody) {
        MenuVO menuVO = menuService.updateMenu(requestBody);
        return ResponseEntity.ok(HttpResponse.success(menuVO));
    }

    /**
     * 删除菜单（有子菜单或被角色引用时禁止）
     *
     * @param menuId menuId
     * @return 响应
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<HttpResponse<Void>> deleteMenu(@PathVariable String menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(HttpResponse.success());
    }

    /**
     * 全部菜单树（含禁用，管理员需要完整视图）
     *
     * @return 菜单树
     */
    @GetMapping("/tree")
    public ResponseEntity<HttpResponse<List<MenuVO>>> getMenuTree() {
        List<MenuVO> tree = menuService.getMenuTree();
        return ResponseEntity.ok(HttpResponse.success(tree));
    }
}
