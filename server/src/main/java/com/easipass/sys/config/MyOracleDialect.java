package com.easipass.sys.config;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

public class MyOracleDialect extends Oracle10gDialect {
    public MyOracleDialect() {
        super();
        registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.DECIMAL, StandardBasicTypes.DOUBLE.getName());
        registerHibernateType(Types.NCLOB, StandardBasicTypes.STRING.getName());
        registerHibernateType(-101, StandardBasicTypes.TIMESTAMP.getName());

    }
}