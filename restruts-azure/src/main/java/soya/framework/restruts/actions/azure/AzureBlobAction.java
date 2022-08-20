package soya.framework.restruts.actions.azure;


import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import soya.framework.restruts.action.Action;
import soya.framework.restruts.action.ActionContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public abstract class AzureBlobAction<T> implements Action<T> {

    protected BlobServiceClient blobServiceClient() {
        return ActionContext.getInstance().getService(BlobServiceClient.class);
    }

    protected BlobContainerClient getBlobContainerClient(String containerName) {
        BlobContainerClient client = blobServiceClient().getBlobContainerClient(containerName);
        if (!client.exists()) {
            client.create();
        }

        return client;
    }

    protected byte[] gzip(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(data);

        return byteArrayOutputStream.toByteArray();
    }
}
