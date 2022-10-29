package soya.framework.azure.actions;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.commons.util.StreamUtils;

import java.io.InputStream;

@ActionDefinition(
        domain = "azure-blob-storage",
        name = "blob-download",
        path = "/blob/download",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_OCTET_STREAM
)
public class AzureBlobDownloadAction extends AzureBlobAction<byte[]> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String container;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String filename;

    @Override
    public byte[] execute() throws Exception {
        BlobContainerClient containerClient = getBlobContainerClient(container);
        BlockBlobClient blob = containerClient.getBlobClient(filename).getBlockBlobClient();
        InputStream input = blob.openInputStream();

        return StreamUtils.copyToByteArray(input);
    }
}
