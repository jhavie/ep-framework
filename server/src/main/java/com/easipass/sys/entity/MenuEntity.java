package com.easipass.sys.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Merjiezo on 2017/6/23.
 */
public class MenuEntity {

    private Long index;

    private String router;

    private String name;

    private String icon;

    private String menuCode;

    private List children;

    public MenuEntity() {}

    public MenuEntity(Long index, String router, String name, String icon, String menuCode) {
        this.index = index;
        this.router = router;
        this.name = name;
        this.icon = icon;
        this.menuCode = menuCode;
        this.children = new ArrayList<MenuEntity>();
    }

    public MenuEntity(Long index, String router, String name, String icon, String menuCode, List children) {
        this.index = index;
        this.router = router;
        this.name = name;
        this.icon = icon;
        this.menuCode = menuCode;
        this.children = children;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getRouter() {
        return router;
    }

    public void setRoute(String router) {
        this.router = router;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
}
