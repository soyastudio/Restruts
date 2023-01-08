package soya.framework.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import soya.framework.bean.DynaBean;
import soya.framework.bean.DynaClassBase;
import soya.framework.bean.DynaProperty;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class XlsxDynaClass extends DynaClassBase {

    protected List<DynaBean> rows = new ArrayList<>();
    protected Map<Integer, String> columnIndexes = new LinkedHashMap<>();

    public XlsxDynaClass(String name, File file, String sheetName, String[] columnNames) {
        setName(name);
        List<DynaProperty> properties = new ArrayList<>();

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
                            CellType cellType = cell.getCellType();
                            if (CellType.STRING.equals(cellType)) {
                                bean.set(propName, cell.getStringCellValue());

                            } else if (CellType.NUMERIC.equals(cellType)) {
                                bean.set(propName, cell.getNumericCellValue());

                            } else if (CellType.BOOLEAN.equals(cellType)) {
                                bean.set(propName, cell.getBooleanCellValue());

                            } else if (CellType.BLANK.equals(cellType)) {

                            } else if (CellType.ERROR.equals(cellType)) {

                            } else if (CellType.FORMULA.equals(cellType)) {

                            } else if (CellType._NONE.equals(cellType)) {

                            }
                        }
                    });

                    rows.add(bean);

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
                            properties.add(new DynaProperty(col, Object.class));

                        }

                        setProperties(properties.toArray(new DynaProperty[properties.size()]));
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
            return "" + cell.getBooleanCellValue();

        } else if (CellType.NUMERIC.equals(cell.getCellType())) {
            return "" + cell.getNumericCellValue();
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

    public List<DynaBean> getRows() {
        return rows;
    }

    @Override
    public DynaBean newInstance() throws IllegalAccessException, InstantiationException {
        return new XlsxDynaBean(this);
    }

    static class XlsxDynaBean extends DynaBeanBase<XlsxDynaClass> {
        protected XlsxDynaBean(XlsxDynaClass dynaClass) {
            super(dynaClass);
        }
    }

}
