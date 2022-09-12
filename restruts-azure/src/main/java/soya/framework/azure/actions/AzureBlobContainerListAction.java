package soya.framework.azure.actions;

import soya.framework.action.MediaType;
import soya.framework.action.OperationMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@OperationMapping(domain = "azure-blob-storage",
        name = "container-list",
        path = "/azure-blob-storage/containers",
        method = OperationMapping.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON)
public class AzureBlobContainerListAction extends AzureBlobAction<String[]> {

    @Override
    public String[] execute() throws Exception {

        List<String> list = new ArrayList<>();
        blobServiceClient().listBlobContainers().forEach(e -> {
            list.add(e.getName());
        });
        Collections.sort(list);

        return list.toArray(new String[list.size()]);
    }
}
