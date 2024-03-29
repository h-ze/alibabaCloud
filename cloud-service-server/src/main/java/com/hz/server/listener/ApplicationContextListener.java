package com.hz.server.listener;

import com.hz.server.annotation.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * spring容器初始化完成事件
 * Created by shuzheng on 2017/1/7.
 */
@Component
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // root application context
        if(null == contextRefreshedEvent.getApplicationContext().getParent()) {
            LOGGER.debug(">>>>> spring初始化完毕 <<<<<");
            // spring初始化完毕后，通过反射调用所有使用BaseService注解的initMapper方法
            Map<String, Object> baseServices = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(BaseService.class);
            for(Object service : baseServices.values()) {
                LOGGER.debug(">>>>> {}.initMapper()", service.getClass().getName());
                /*try {
                    Method initMapper = hz.service.getClass().getMethod("initMapper");
                    initMapper.invoke(hz.service);
                } catch (Exception e) {
                    LOGGER.error("初始化BaseService的initMapper方法异常", e);
                    e.printStackTrace();
                }*/
            }

            // 系统入口初始化
            /*Map<String, BaseInterface> baseInterfaceBeans = contextRefreshedEvent.getApplicationContext().getBeansOfType(BaseInterface.class);
            for(Object hz.service : baseInterfaceBeans.values()) {
                LOGGER.debug(">>>>> {}.init()", hz.service.getClass().getName());
                try {
                    Method init = hz.service.getClass().getMethod("init");
                    init.invoke(hz.service);
                } catch (Exception e) {
                    LOGGER.error("初始化BaseInterface的init方法异常", e);
                    e.printStackTrace();
                }
            }*/

        }
    }

}
