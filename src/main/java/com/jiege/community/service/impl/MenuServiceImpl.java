package com.jiege.community.service.impl;

import com.jiege.community.dao.MenuDao;
import com.jiege.community.dto.MenuCreateRequestBody;
import com.jiege.community.dto.MenuUpdateRequestBody;
import com.jiege.community.entity.Menu;
import com.jiege.community.enums.MenuStatus;
import com.jiege.community.common.ResponseCode;
import com.jiege.community.common.exception.BusinessException;
import com.jiege.community.service.MenuService;
import com.jiege.community.vo.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: 19599
 * @Date: 2026/8/16 1:40
 * @Description: 菜单服务实现
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuDao menuDao;

    public MenuServiceImpl(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    public MenuVO addMenu(MenuCreateRequestBody request) {
        // 菜单名称是否重复
        if (menuDao.selectByMenuName(request.getMenuName()) != null) {
            throw new BusinessException(ResponseCode.MENU_NAME_EXISTS);
        }
        // 权限标识是否重复（仅按钮/菜单会携带 perms）
        if (request.getPerms() != null && menuDao.selectByPerms(request.getPerms()) != null) {
            throw new BusinessException(ResponseCode.MENU_PERMS_EXISTS);
        }
        // 父菜单必须存在
        if (request.getParentId() != null && menuDao.selectByMenuId(request.getParentId()) == null) {
            throw new BusinessException(ResponseCode.MENU_PARENT_NOT_EXISTS);
        }
        Menu menu = buildMenu(request);
        menuDao.insert(menu);
        return new MenuVO(menu);
    }

    @Override
    public MenuVO updateMenu(MenuUpdateRequestBody request) {
        Menu menu = menuDao.selectByMenuId(request.getMenuId());
        if (menu == null) {
            throw new BusinessException(ResponseCode.MENU_NOT_EXISTS);
        }
        // 名称被其他菜单占用则不允许
        Menu byName = menuDao.selectByMenuName(request.getMenuName());
        if (byName != null && !byName.getMenuId().equals(request.getMenuId())) {
            throw new BusinessException(ResponseCode.MENU_NAME_EXISTS);
        }
        // 权限标识被其他菜单占用则不允许
        if (request.getPerms() != null) {
            Menu byPerms = menuDao.selectByPerms(request.getPerms());
            if (byPerms != null && !byPerms.getMenuId().equals(request.getMenuId())) {
                throw new BusinessException(ResponseCode.MENU_PERMS_EXISTS);
            }
        }
        // 父菜单校验：必须存在且不能是自己（防循环引用）
        if (request.getParentId() != null) {
            if (request.getParentId().equals(request.getMenuId())) {
                throw new BusinessException(ResponseCode.MENU_PARENT_NOT_EXISTS);
            }
            if (menuDao.selectByMenuId(request.getParentId()) == null) {
                throw new BusinessException(ResponseCode.MENU_PARENT_NOT_EXISTS);
            }
        }
        menu.setParentId(request.getParentId());
        menu.setMenuName(request.getMenuName());
        menu.setPath(request.getPath());
        menu.setPerms(request.getPerms());
        menu.setType(request.getType());
        menu.setIcon(request.getIcon());
        menu.setSort(request.getSort() == null ? 0 : request.getSort());
        menu.setUpdateTime(LocalDateTime.now());
        menuDao.updateMenu(menu);
        return new MenuVO(menu);
    }

    @Override
    public void deleteMenu(String menuId) {
        Menu menu = menuDao.selectByMenuId(menuId);
        if (menu == null) {
            throw new BusinessException(ResponseCode.MENU_NOT_EXISTS);
        }
        // 有子菜单禁止删除
        if (menuDao.countChildMenu(menuId) > 0) {
            throw new BusinessException(ResponseCode.MENU_HAS_CHILD);
        }
        // 被角色引用禁止删除
        if (menuDao.countRoleMenu(menuId) > 0) {
            throw new BusinessException(ResponseCode.MENU_HAS_ROLE);
        }
        menuDao.deleteMenu(menuId);
    }

    @Override
    public List<MenuVO> getMenuTree() {
        List<Menu> menus = menuDao.selectAll();
        List<MenuVO> vos = menus.stream().map(MenuVO::new).toList();
        // menuId -> VO 映射，用于按 parentId 挂载子节点
        Map<String, MenuVO> map = vos.stream()
                .collect(Collectors.toMap(MenuVO::getMenuId, Function.identity()));
        List<MenuVO> roots = new ArrayList<>();
        for (MenuVO vo : vos) {
            if (vo.getParentId() == null) {
                roots.add(vo);
            } else {
                MenuVO parent = map.get(vo.getParentId());
                if (parent != null) {
                    parent.getChildren().add(vo);
                } else {
                    // 父菜单不存在（脏数据），挂为根节点避免丢失
                    roots.add(vo);
                }
            }
        }
        return roots;
    }

    private Menu buildMenu(MenuCreateRequestBody request) {
        return Menu.builder()
                .menuId(UUID.randomUUID().toString())
                .parentId(request.getParentId())
                .menuName(request.getMenuName())
                .path(request.getPath())
                .perms(request.getPerms())
                .type(request.getType())
                .icon(request.getIcon())
                .sort(request.getSort() == null ? 0 : request.getSort())
                .status(MenuStatus.NORMAL.getStatus())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
