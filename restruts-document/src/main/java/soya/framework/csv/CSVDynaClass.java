package soya.framework.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import soya.framework.bean.DynaBean;
import soya.framework.bean.DynaClassBase;
import soya.framework.bean.DynaProperty;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVDynaClass extends DynaClassBase {

    protected List<DynaBean> rows = new ArrayList<>();

    public CSVDynaClass(String name, byte[] data) {
        setName(name);

        try {
            List<DynaProperty> properties = new ArrayList<>();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));

            String[] cols = csvReader.readNext();
            while (cols != null) {
                if (properties.isEmpty()) {
                    for (String col : cols) {
                        String token = col.trim();
                        properties.add(new DynaProperty(token, String.class));
                    }

                    setProperties(properties.toArray(new DynaProperty[properties.size()]));

                } else {
                    DynaBean bean = newInstance();

                    int size = Math.min(properties.size(), cols.length);
                    for (int i = 0; i < size; i++) {
                        String value = cols[i];
                        bean.set(properties.get(i).getName(), value);
                    }

                    rows.add(bean);
                }

                cols = csvReader.readNext();
            }
        } catch (IOException | CsvValidationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public CSVDynaClass(String name, Reader reader) {
        setName(name);

        try {
            List<DynaProperty> properties = new ArrayList<>();
            CSVReader csvReader = new CSVReader(reader);

            String[] cols = csvReader.readNext();
            while (cols != null) {
                if (properties.isEmpty()) {
                    for (String col : cols) {
                        String token = col.trim();
                        properties.add(new DynaProperty(token, String.class));
                    }

                    setProperties(properties.toArray(new DynaProperty[properties.size()]));

                } else {
                    DynaBean bean = newInstance();

                    int size = Math.min(properties.size(), cols.length);
                    for (int i = 0; i < size; i++) {
                        String value = cols[i];
                        bean.set(properties.get(i).getName(), value);
                    }

                    rows.add(bean);
                }

                cols = csvReader.readNext();
            }
        } catch (IOException | CsvValidationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<DynaBean> getRows() {
        return rows;
    }

    @Override
    public DynaBean newInstance() {
        return new CSVDynaBean(this);
    }

    public String toCSV() {
        DynaProperty[] properties = getDynaProperties();
        StringBuilder builder = new StringBuilder();
        for (DynaProperty property : properties) {
            builder.append("\"").append(property.getName()).append("\"").append(",");
        }
        builder.deleteCharAt(builder.length() - 1).append("\n");

        rows.forEach(e -> {
            for (DynaProperty property : properties) {
                String value = (String) e.get(property.getName());
                if (value == null) {
                    value = "";
                }

                builder.append("\"").append(value).append("\"").append(",");
            }

            builder.deleteCharAt(builder.length() - 1).append("\n");
        });

        return builder.toString();
    }

    static class CSVDynaBean extends DynaBeanBase<CSVDynaClass> {
        CSVDynaBean(CSVDynaClass dynaClass) {
            super(dynaClass);
        }
    }
}
