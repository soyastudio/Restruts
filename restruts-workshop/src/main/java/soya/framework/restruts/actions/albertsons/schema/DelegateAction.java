package soya.framework.restruts.actions.albertsons.schema;

import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.actions.about.AboutAction;

@OperationMapping(domain = "albertsons",
        name = "workshop-about",
        path = "/workshop/about",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "About",
        description = "Print as markdown format.")
public class DelegateAction extends AboutAction {
}
