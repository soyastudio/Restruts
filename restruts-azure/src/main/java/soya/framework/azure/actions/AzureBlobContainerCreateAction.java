package soya.framework.azure.actions;

import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;
import soya.framework.action.ParameterMapping;

@OperationMapping(domain = "azure-blob-storage", name = "container-create", path = "/azure-blob-storage/container",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON)
public class AzureBlobContainerCreateAction extends AzureBlobAction<Boolean> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String name;

    @Override
    public Boolean execute() throws Exception {
        blobServiceClient().createBlobContainerIfNotExists(name);
        return blobServiceClient().getBlobContainerClient(name).exists();
    }
}
