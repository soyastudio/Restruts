package soya.framework.action.actions.reflect;

import soya.framework.action.*;
import soya.framework.common.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

@ActionDefinition(domain = "reflect",
        name = "domains",
        path = "/domains",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON,
        displayName = "Domains",
        description = "Domains.")
public class DomainsAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        List<DomainModel> domainModelList = new ArrayList<>();
        String[] domains = ActionContext.getInstance().getActionMappings().domains();
        for (String domain : domains) {
            Class<?> type = ActionContext.getInstance().getActionMappings().domainType(domain);
            domainModelList.add(DomainModel.newInstance(type));
        }
        return JsonUtils.toJson(domainModelList);
    }

    static class DomainModel {
        private String name;
        private String path;
        private String title;
        private String description;

        static DomainModel newInstance(Class<?> cls) {
            Domain domain = cls.getAnnotation(Domain.class);
            DomainModel model = new DomainModel();
            model.name = domain.name();
            model.path = domain.path();
            model.title = domain.title();
            model.description = domain.description();

            return model;
        }
    }


}
