package soya.framework.azure.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ActionParameterType;

@ActionDefinition(
        domain = "azure-blob-storage",
        name = "container-delete",
        path = "/container",
        method = ActionDefinition.HttpMethod.DELETE,
        produces = MediaType.APPLICATION_JSON
)
public class AzureBlobContainerDeleteAction extends AzureBlobAction<Boolean> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM, required = true)
    private String name;

    @Override
    public Boolean execute() throws Exception {
        blobServiceClient().deleteBlobContainerIfExists(name);
        return !blobServiceClient().getBlobContainerClient(name).exists();
    }
}
