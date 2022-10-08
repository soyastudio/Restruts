package soya.framework.action.actions.jmx;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

import java.lang.management.ManagementFactory;

@ActionDefinition(domain = "jmx",
        name = "jmx-domain",
        path = "/domains",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "JMX Domains",
        description = "Print as markdown format.")
public class JmxDomainsAction extends Action<String[]> {

    @Override
    public String[] execute() throws Exception {
        return ManagementFactory.getPlatformMBeanServer().getDomains();
    }
}
