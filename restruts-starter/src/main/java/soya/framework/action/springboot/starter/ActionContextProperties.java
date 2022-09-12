package soya.framework.action.springboot.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "soya.framework.action")
public class ActionContextProperties {

    private boolean enableProxy;

    private String proxyPackages;



}
