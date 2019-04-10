package com.easipass.sys.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;

/**
 * @Description: 系统基础JPA工厂类
 * @author: mlzhang
 * @date: 2016/10/12 19:43
 * @version: V1.0
 */
public class EpRepositoryFactory extends JpaRepositoryFactory {

    public EpRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

        return EpJpaRepository.class;
    }
}
