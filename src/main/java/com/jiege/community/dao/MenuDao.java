package com.jiege.community.dao;

import com.jiege.community.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/16 1:35
 * @Description: 菜单 DAO
 */
@Mapper
public interface MenuDao {
    /**
     * 新增菜单
     *
     * @param menu 菜单对象
     * @return 影响行数
     */
    int insert(Menu menu);

    /**
     * 根据菜单ID查询菜单
     *
     * @param menuId 菜单ID
     * @return 菜单对象
     */
    Menu selectByMenuId(@Param("menuId") String menuId);

    /**
     * 根据菜单名称查询菜单
     *
     * @param menuName 菜单名称
     * @return 菜单对象
     */
    Menu selectByMenuName(@Param("menuName") String menuName);

    /**
     * 根据权限字符串查询菜单
     *
     * @param perms 权限字符串
     * @return 菜单对象
     */
    Menu selectByPerms(@Param("perms") String perms);

    /**
     * 更新菜单
     *
     * @param menu 菜单对象
     * @return 影响行数
     */
    int updateMenu(Menu menu);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return 影响行数
     */
    int deleteMenu(@Param("menuId") String menuId);

    /**
     * 根据父菜单ID统计子菜单数量
     *
     * @param parentId 父菜单ID
     * @return 子菜单数量
     */
    int countChildMenu(@Param("parentId") String parentId);

    /**
     * 根据菜单ID统计角色数量
     *
     * @param menuId 菜单ID
     * @return 角色数量
     */
    int countRoleMenu(@Param("menuId") String menuId);

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    List<Menu> selectAll();
}
