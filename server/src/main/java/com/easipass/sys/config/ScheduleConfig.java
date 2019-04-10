package com.easipass.sys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName: ScheduleConfig
 * @Description: 定时任务配置类
 * @author: xufeng
 * @date: 2017/11/29 15:21  
 */

@Configuration
//@ComponentScan("com.easipass.business.task")
@EnableScheduling
public class ScheduleConfig {
}
