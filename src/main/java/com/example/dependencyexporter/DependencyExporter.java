package com.example.dependencyexporter;

import com.example.dependencyexporter.model.DependencyEntry;
import com.example.dependencyexporter.parser.DependencyParser;
import com.example.dependencyexporter.storage.ExcelDependencyStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DependencyExporter {

    public static void main(String[] args) {
        String inputFilePath = "mvn-dependency-output.txt";
        String outputFilePath = "dependencies.xlsx";

        DependencyParser parser = new DependencyParser();
        ExcelDependencyStorage storage = new ExcelDependencyStorage(outputFilePath);

        Map<String, DependencyEntry> existingDependencies = storage.load();
        List<DependencyEntry> newDependencies = parser.parse(inputFilePath);

        if (newDependencies.isEmpty()) {
            System.out.println("Файл пуст или не содержит зависимостей. Excel не обновлен.");
            return;
        }

        updateDependencies(existingDependencies, newDependencies);
        storage.save(new ArrayList<>(existingDependencies.values()));

        System.out.println("Файл " + outputFilePath + " успешно обновлен!");
    }


    private static void updateDependencies(Map<String, DependencyEntry> existing, List<DependencyEntry> newDeps) {
        for (DependencyEntry newDep : newDeps) {
            String key = newDep.getDependency() + ":" + newDep.getVersion();
            if (existing.containsKey(key)) {
                DependencyEntry existingDep = existing.get(key);
                String updatedProjects = existingDep.getProjects() + ", " + newDep.getProjects();
                existing.put(key, new DependencyEntry(newDep.getDependency(), newDep.getVersion(), updatedProjects));
            } else {
                existing.put(key, newDep);
            }
        }
    }
}
