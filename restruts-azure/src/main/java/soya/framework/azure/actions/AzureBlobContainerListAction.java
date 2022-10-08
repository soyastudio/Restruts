package soya.framework.azure.actions;

import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ActionDefinition(
        domain = "azure-blob-storage",
        name = "container-list",
        path = "/containers",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.APPLICATION_JSON
)
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
