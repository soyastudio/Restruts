package soya.framework.albertsons.actions.iib;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.common.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@ActionDefinition(domain = "albertsons",
        name = "iib-update-readme",
        path = "/workshop/iib/readme",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        description = "Update README file.")
public class IIBDevUpdateReadmeAction extends IIBDevAction<String> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD,
            description = "README.md contents",
            contentType = MediaType.TEXT_PLAIN,
            required = true)
    private String payload;

    @Override
    public String execute() throws Exception {
        File dir = new File(iibDevelopmentDir(), application);
        File readme = new File(dir, "README.md");
        InputStream inputStream = new FileInputStream(readme);
        byte[] contents = StreamUtils.copyToByteArray(inputStream);
        inputStream.close();

        return new String(contents);
    }
}
