package soya.framework.restruts.actions.albertsons.iib;

import soya.framework.commons.util.StreamUtils;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@OperationMapping(domain = "albertsons",
        name = "iib-create-application",
        path = "/workshop/iib/readme",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN,
        description = "Read the README file.")
public class IIBDevReadmeAction extends IIBDevAction<String> {

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
