package com.easipass.business.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by LH on 2017/5/18.
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "GCC_GDWS_CONFIG", catalog = "")
public class GccGdwsConfig {
    private String paraName;
    private String paraDesc;
    private String paraValue;

    @Id
    @Column(name = "PARA_NAME", nullable = false, length = 20)
    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    @Basic
    @Column(name = "PARA_DESC", nullable = true, length = 100)
    public String getParaDesc() {
        return paraDesc;
    }

    public void setParaDesc(String paraDesc) {
        this.paraDesc = paraDesc;
    }

    @Basic
    @Column(name = "PARA_VALUE", nullable = false, length = 50)
    public String getParaValue() {
        return paraValue;
    }

    public void setParaValue(String paraValue) {
        this.paraValue = paraValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GccGdwsConfig that = (GccGdwsConfig) o;

        if (paraName != null ? !paraName.equals(that.paraName) : that.paraName != null) return false;
        if (paraDesc != null ? !paraDesc.equals(that.paraDesc) : that.paraDesc != null) return false;
        if (paraValue != null ? !paraValue.equals(that.paraValue) : that.paraValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = paraName != null ? paraName.hashCode() : 0;
        result = 31 * result + (paraDesc != null ? paraDesc.hashCode() : 0);
        result = 31 * result + (paraValue != null ? paraValue.hashCode() : 0);
        return result;
    }
}
