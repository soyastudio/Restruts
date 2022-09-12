package soya.framework.azure.actions;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;
import soya.framework.action.ParameterMapping;
import soya.framework.common.util.StreamUtils;

import java.io.InputStream;

@OperationMapping(domain = "azure-blob-storage",
        name = "blob-download",
        path = "/azure-blob-storage/blob/download",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.APPLICATION_OCTET_STREAM)
public class AzureBlobDownloadAction extends AzureBlobAction<byte[]> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String container;

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    private String filename;

    @Override
    public byte[] execute() throws Exception {
        BlobContainerClient containerClient = getBlobContainerClient(container);
        BlockBlobClient blob = containerClient.getBlobClient(filename).getBlockBlobClient();
        InputStream input = blob.openInputStream();

        return StreamUtils.copyToByteArray(input);
    }
}
