package com.easipass.sys.entity;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * Created by Merjiezo on 2017/6/23.
 */
@Entity
@Table(name = "GCC_GDWS_SYS_MENU", schema = "GCC", catalog = "")
@SequenceGenerator(name = "SEQ_GCC_GDWS_SYS_MENU_ID", sequenceName = "SEQ_GCC_GDWS_SYS_MENU_ID", allocationSize = 1)
public class GccGdwsSysMenuEntity {
    private long id;
    private String menuCode;
    private String parentCode;
    private String menuName;
    private String menuUrl;
    private String menuIcon;
    private int status;
    private String menuAlter;
    private int menuOrder;
    private String notes;
    private String menuImg;
    private String menuNameEn;
    private Date createTime;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GCC_GDWS_SYS_MENU_ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "MENU_CODE", nullable = false, length = 20)
    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    @Basic
    @Column(name = "PARENT_CODE", nullable = false, length = 20)
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Basic
    @Column(name = "MENU_NAME", nullable = true, length = 100)
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Basic
    @Column(name = "MENU_URL", nullable = true, length = 200)
    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    @Basic
    @Column(name = "MENU_ICON", nullable = true, length = 50)
    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    @Basic
    @Column(name = "STATUS", nullable = true, precision = 0)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "MENU_ALTER", nullable = true, length = 100)
    public String getMenuAlter() {
        return menuAlter;
    }

    public void setMenuAlter(String menuAlter) {
        this.menuAlter = menuAlter;
    }

    @Basic
    @Column(name = "MENU_ORDER", nullable = true, precision = 0)
    public int getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(int menuOrder) {
        this.menuOrder = menuOrder;
    }

    @Basic
    @Column(name = "NOTES", nullable = true, length = 100)
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Basic
    @Column(name = "MENU_IMG", nullable = true, length = 20)
    public String getMenuImg() {
        return menuImg;
    }

    public void setMenuImg(String menuImg) {
        this.menuImg = menuImg;
    }

    @Basic
    @Column(name = "MENU_NAME_EN", nullable = true, length = 100)
    public String getMenuNameEn() {
        return menuNameEn;
    }

    public void setMenuNameEn(String menuNameEn) {
        this.menuNameEn = menuNameEn;
    }

    @Basic
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GccGdwsSysMenuEntity that = (GccGdwsSysMenuEntity) o;

        if (id != that.id) return false;
        if (menuCode != null ? !menuCode.equals(that.menuCode) : that.menuCode != null) return false;
        if (parentCode != null ? !parentCode.equals(that.parentCode) : that.parentCode != null) return false;
        if (menuName != null ? !menuName.equals(that.menuName) : that.menuName != null) return false;
        if (menuUrl != null ? !menuUrl.equals(that.menuUrl) : that.menuUrl != null) return false;
        if (menuIcon != null ? !menuIcon.equals(that.menuIcon) : that.menuIcon != null) return false;
        if (status != that.status) return false;
        if (menuAlter != null ? !menuAlter.equals(that.menuAlter) : that.menuAlter != null) return false;
        if (menuOrder != that.menuOrder) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        if (menuImg != null ? !menuImg.equals(that.menuImg) : that.menuImg != null) return false;
        if (menuNameEn != null ? !menuNameEn.equals(that.menuNameEn) : that.menuNameEn != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (menuCode != null ? menuCode.hashCode() : 0);
        result = 31 * result + (parentCode != null ? parentCode.hashCode() : 0);
        result = 31 * result + (menuName != null ? menuName.hashCode() : 0);
        result = 31 * result + (menuUrl != null ? menuUrl.hashCode() : 0);
        result = 31 * result + (menuIcon != null ? menuIcon.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (menuAlter != null ? menuAlter.hashCode() : 0);
        result = 31 * result + menuOrder;
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (menuImg != null ? menuImg.hashCode() : 0);
        result = 31 * result + (menuNameEn != null ? menuNameEn.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
