package com.example.dependencyexporter.storage;

import com.example.dependencyexporter.model.DependencyEntry;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelDependencyStorage implements DependencyStorage {
    private final String filePath;

    public ExcelDependencyStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Map<String, DependencyEntry> load() {
        Map<String, DependencyEntry> dependencies = new LinkedHashMap<>();
        try (Workbook workbook = WorkbookFactory.create(new File(filePath))) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return dependencies;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String dependency = row.getCell(0).getStringCellValue();
                String version = row.getCell(1).getStringCellValue();
                String projects = row.getCell(2).getStringCellValue();
                dependencies.put(dependency + ":" + version, new DependencyEntry(dependency, version, projects));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return dependencies;
    }

    @Override
    public void save(List<DependencyEntry> dependencies) {
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath)) {
            Sheet sheet = workbook.createSheet("Dependencies");
            createHeaderRow(sheet);
            int rowNum = 1;
            for (DependencyEntry entry : dependencies) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getDependency());
                row.createCell(1).setCellValue(entry.getVersion());
                row.createCell(2).setCellValue(entry.getProjects());
            }
            autoSizeColumns(sheet);
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Зависимость", "Версия", "Проект"};
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        int numberOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
