package soya.framework.restruts.actions.albertsons.iib;

import soya.framework.commons.util.StreamUtils;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.PayloadMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@OperationMapping(domain = "albertsons",
        name = "iib-update-readme",
        path = "/workshop/iib/readme",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        description = "Update README file.")
public class IIBDevUpdateReadmeAction extends IIBDevAction<String> {

    @PayloadMapping(description = "README.md contents", consumes = MediaType.TEXT_PLAIN)
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
