package soya.framework.action.actions.reflect;

import soya.framework.action.*;

@ActionDefinition(domain = "reflect",
        name = "util-resource",
        path = "/util/resource",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Resource",
        description = "Extract resource through resource uri.")
public class ResourceAction extends Action<String> {

    @ActionProperty(
            parameterType = ActionProperty.PropertyType.HEADER_PARAM,
            required = true,
            option = "u",
            description = {
                    "Resource uri, examples:",
                    "- URL resources such as 'http://', 'https://', 'ftp://'...",
                    "- env://<property_name>: from environment property",
                    "- classpath://<resource_path>: from classpath resource",
                    "- base64://<base64_encoded_string>: get string value by decoding the '<base64_encoded_string>'",
                    "- gzip://<gzip_compressed_base64_encoded_string>: get string value by decoding and unzip the '<gzip_compressed_base64_encoded_string>'",
                    "- action://<domain_name>/<action_name>?<query_string>: get value through calling another action"
            }
    )
    private String uri;

    @Override
    public String execute() throws Exception {
        return Resources.getResourceAsString(uri);
    }
}
