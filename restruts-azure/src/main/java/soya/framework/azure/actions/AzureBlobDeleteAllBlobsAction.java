package soya.framework.azure.actions;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.ListBlobsOptions;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.ParameterType;

@ActionDefinition(
        domain = "azure-blob-storage",
        name = "delete-all-blobs",
        path = "/delete-all-blobs",
        method = ActionDefinition.HttpMethod.DELETE,
        produces = MediaType.APPLICATION_JSON
)
public class AzureBlobDeleteAllBlobsAction extends AzureBlobAction<Integer> {

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true)
    private String container;

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true)
    private String prefix;

    private int count;

    @Override
    public Integer execute() throws Exception {
        BlobContainerClient containerClient = getBlobContainerClient(container);
        containerClient.listBlobs(new ListBlobsOptions().setPrefix(prefix), null).forEach(e -> {
            containerClient.getBlobClient(e.getName()).getBlockBlobClient().deleteIfExists();
            count++;
        });

        return count;
    }
}
