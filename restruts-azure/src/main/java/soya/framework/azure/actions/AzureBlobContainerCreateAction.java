package soya.framework.azure.actions;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;

@ActionDefinition(
        domain = "azure-blob-storage",
        name = "container-create",
        path = "/container",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON
)
public class AzureBlobContainerCreateAction extends AzureBlobAction<Boolean> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String name;

    @Override
    public Boolean execute() throws Exception {
        blobServiceClient().createBlobContainerIfNotExists(name);
        return blobServiceClient().getBlobContainerClient(name).exists();
    }
}
