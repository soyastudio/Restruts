package soya.framework.azure.actions;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;

@ActionDefinition(domain = "azure-blob-storage", name = "delete-blob", path = "/azure-blob-storage/delete-blob",
        method = ActionDefinition.HttpMethod.DELETE,
        produces = MediaType.APPLICATION_JSON)
public class AzureBlobDeleteBlobAction extends AzureBlobAction<Boolean> {


    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String container;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String filename;

    @Override
    public Boolean execute() throws Exception {
        BlobContainerClient containerClient = getBlobContainerClient(container);
        BlockBlobClient blockBlobClient = containerClient.getBlobClient(filename).getBlockBlobClient();
        return blockBlobClient.deleteIfExists();
    }
}
