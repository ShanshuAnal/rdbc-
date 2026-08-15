package com.jiege.community.service;

import com.jiege.community.dto.MenuCreateRequestBody;
import com.jiege.community.dto.MenuUpdateRequestBody;
import com.jiege.community.vo.MenuVO;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/16 1:40
 * @Description: 菜单服务
 */
public interface MenuService {
    /**
     * 添加菜单
     *
     * @param request 菜单信息
     * @return 菜单信息
     */
    MenuVO addMenu(MenuCreateRequestBody request);

    /**
     * 更新菜单
     *
     * @param request 菜单信息
     * @return 菜单信息
     */
    MenuVO updateMenu(MenuUpdateRequestBody request);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return 是否删除成功
     */
    void deleteMenu(String menuId);

    /**
     * 获取菜单树
     *
     * @return 菜单树
     */
    List<MenuVO> getMenuTree();
}
