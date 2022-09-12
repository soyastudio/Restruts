package soya.framework.azure.actions;


import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import soya.framework.action.Action;
import soya.framework.action.ActionContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public abstract class AzureBlobAction<T> extends Action<T> {

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
