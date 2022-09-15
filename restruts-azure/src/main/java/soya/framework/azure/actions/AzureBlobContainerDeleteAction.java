package soya.framework.azure.actions;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;

@ActionDefinition(domain = "azure-blob-storage", name = "container-delete", path = "/azure-blob-storage/container",
        method = ActionDefinition.HttpMethod.DELETE,
        produces = MediaType.APPLICATION_JSON)
public class AzureBlobContainerDeleteAction extends AzureBlobAction<Boolean> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String name;

    @Override
    public Boolean execute() throws Exception {
        blobServiceClient().deleteBlobContainerIfExists(name);
        return !blobServiceClient().getBlobContainerClient(name).exists();
    }
}