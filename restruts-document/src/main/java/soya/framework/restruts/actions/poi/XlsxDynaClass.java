package soya.framework.restruts.actions.poi;

import com.google.common.base.CaseFormat;
import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class XlsxDynaClass implements DynaClass, Serializable {

    private String name;
    protected Map<String, DynaProperty> propertiesMap = new LinkedHashMap<>();
    protected Map<Integer, String> columnIndexes = new LinkedHashMap<>();
    protected DynaProperty[] properties;
    protected List<DynaBean> beans = new ArrayList<>();

    protected XlsxDynaClass(String name) {
        this.name = name;
    }

    public XlsxDynaClass(String name, String[] columnNames, File file, String sheetName) {
        this.name = name;

        XSSFWorkbook workbook = null;
        Sheet sheet = null;
        try {
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheet(sheetName);

            boolean start = false;

            Set<String> columnSet = new HashSet<>(Arrays.asList(columnNames));
            Iterator<Row> sheetIterator = sheet.iterator();
            while (sheetIterator.hasNext()) {
                Row currentRow = sheetIterator.next();
                if (start) {

                    DynaBean bean = newInstance();
                    columnIndexes.entrySet().forEach(e -> {
                        int index = e.getKey();
                        String propName = e.getValue();

                        Cell cell = currentRow.getCell(index);
                        if (cell != null) {
                            String propValue = cell.getStringCellValue();
                            bean.set(propName, propValue);
                        }
                    });

                    beans.add(bean);

                } else {
                    if (isLabelRow(currentRow, columnSet)) {
                        start = true;

                        int first = currentRow.getFirstCellNum();
                        int last = currentRow.getLastCellNum();

                        for (int i = first; i <= last; i++) {
                            Cell cell = currentRow.getCell(i);
                            if (cell != null && cell.getCellType().equals(CellType.STRING)) {
                                String cellValue = cell.getStringCellValue();
                                if (columnSet.contains(cellValue)) {
                                    columnIndexes.put(i, cellValue);
                                }
                            }
                        }

                        for (String col : columnNames) {
                            propertiesMap.put(col, new DynaProperty(col, String.class));

                        }

                        this.properties = propertiesMap.values().toArray(new DynaProperty[propertiesMap.size()]);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public XlsxDynaClass(File file, String sheetName) {

        this.name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, sheetName.replaceAll(" ", "_"));

        XSSFWorkbook workbook = null;
        Sheet sheet = null;
        try {
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheet(sheetName);

            boolean start = false;
            Iterator<Row> sheetIterator = sheet.iterator();

            while (sheetIterator.hasNext()) {
                Row currentRow = sheetIterator.next();
                if (start && !isEmptyRow(currentRow)) {
                    DynaBean bean = newInstance();
                    columnIndexes.entrySet().forEach(e -> {
                        int index = e.getKey();
                        String propName = e.getValue();

                        Cell cell = currentRow.getCell(index);
                        if (cell != null) {
                            String propValue = getCellValue(cell);

                            bean.set(propName, propValue);
                        }
                    });

                    beans.add(bean);

                } else {
                    if (!isEmptyRow(currentRow)) {
                        int first = currentRow.getFirstCellNum();
                        int last = currentRow.getLastCellNum();

                        for (int i = first; i <= last; i++) {
                            Cell cell = currentRow.getCell(i);
                            if (cell != null && cell.getCellType().equals(CellType.STRING)
                                    && cell.getStringCellValue() != null && cell.getStringCellValue().trim().length() > 0) {

                                String col = cell.getStringCellValue().replaceAll(" ", "_").toLowerCase();
                                propertiesMap.put(col, new DynaProperty(col, String.class));
                                columnIndexes.put(i, col);
                            }
                        }

                        properties = propertiesMap.values().toArray(new DynaProperty[propertiesMap.size()]);

                        start = true;
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String getCellValue(Cell cell) {
        if (CellType.STRING.equals(cell.getCellType())) {
            return cell.getStringCellValue();

        } else if (CellType.BOOLEAN.equals(cell.getCellType())) {
            return  "" + cell.getBooleanCellValue();

        } else if (CellType.NUMERIC.equals(cell.getCellType())) {
            return  "" + cell.getNumericCellValue();
        }

        return null;
    }

    private boolean isEmptyRow(Row row) {
        int first = row.getFirstCellNum();
        int last = row.getLastCellNum();

        for (int i = first; i <= last; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType().equals(CellType.STRING)
                    && cell.getStringCellValue() != null && cell.getStringCellValue().trim().length() > 0) {
                return false;
            }
        }

        return true;
    }

    private boolean isLabelRow(Row row, Set<String> columnSet) {
        Set<String> rowSet = new HashSet<>();

        int first = row.getFirstCellNum();
        int last = row.getLastCellNum();

        for (int i = first; i <= last; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType().equals(CellType.STRING)) {
                rowSet.add(cell.getStringCellValue());
            }
        }

        return rowSet.containsAll(columnSet);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DynaProperty getDynaProperty(String s) {
        return propertiesMap.get(s);
    }

    @Override
    public DynaProperty[] getDynaProperties() {
        return this.properties;
    }

    @Override
    public DynaBean newInstance() {
        return new BasicDynaBean(this);
    }

    public List<DynaBean> getBeans() {
        return beans;
    }
}
