package com.alibaba.csp.sentinel.dashboard.rule.nacos.system;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yanghuaxu
 * @date 2023/3/14 10:28
 */
@Component("systemRuleNacosPublisher")
public class SystemRuleNacosPublisher implements DynamicRulePublisher<List<SystemRuleEntity>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemRuleNacosPublisher.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<SystemRuleEntity>, String> converter;

    @Override
    public void publish(String app, List<SystemRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        String convertedRule = converter.convert(rules);
        LOGGER.info("sentinel dashboard publisher system rule : {}", convertedRule);
        configService.publishConfig(app + NacosConfigUtil.SYS_DATA_ID_POSTFIX,
                NacosConfigUtil.GROUP_ID, convertedRule);
    }
}
