package soya.framework.document.actions.poi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import soya.framework.action.ParameterType;
import soya.framework.document.actions.DocumentAction;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ActionDefinition(domain = "document",
        name = "xlsx-to-json",
        path = "/xlsx/read",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON)
public class SpreadSheetAction extends DocumentAction<String> {

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true, displayOrder = 1)
    private String uri;

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true, displayOrder = 2)
    private String sheet;

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM, required = true, displayOrder = 3)
    private String columns;

    @Override
    public String execute() throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        File file = getFile(uri);
        String[] properties = columns.split(",");
        for (int i = 0; i < properties.length; i++) {
            properties[i] = properties[i].trim();
        }

        XlsxDynaClass xlsxDynaClass = new XlsxDynaClass(file.getName(), properties, file, sheet);
        List<DynaBean> beans = xlsxDynaClass.getBeans();

        List<Map<String, String>> list = new ArrayList();
        beans.forEach(e -> {
            try {
                list.add(BeanUtils.describe(e));

            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }

        });

        return gson.toJson(list);
    }
}
