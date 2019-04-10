package com.easipass.sys.config;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Slf4j
@Configuration
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
public class DataSourceConfiguration {
    @Value("${dbClient.password}")
    private String password;

    @Value("${dbClient.publicKey:easipass}")
    private String publicKey;

    @Value("${dbClient.decrypt:false}")
    private boolean decrypt;

    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.druid")
    @Qualifier("dataSource")
    public DataSource dataSource() throws Exception {
        log.info("-------------------- dataSource初始化 ---------------------");
        return createDataSource(publicKey, password);
    }

    private DruidDataSource createDataSource(String publicKey, String password) throws Exception {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        if (decrypt) {
            dataSource.setPassword(ConfigTools.decrypt(publicKey, password));
        } else {
            dataSource.setPassword(password);
        }
        return dataSource;
    }
}
