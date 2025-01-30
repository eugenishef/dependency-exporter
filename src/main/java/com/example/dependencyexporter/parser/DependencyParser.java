package com.example.dependencyexporter.parser;

import com.example.dependencyexporter.model.DependencyEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DependencyParser {

    public List<DependencyEntry> parse(String filePath) {
        List<DependencyEntry> dependencyList = new ArrayList<>();
        String currentProject = null;

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (!line.startsWith("+-") && !line.startsWith("|")) {
                    currentProject = extractProjectName(line);
                } else {
                    DependencyEntry dependencyEntry = createDependencyEntry(line, currentProject);
                    if (dependencyEntry != null) {
                        dependencyList.add(dependencyEntry);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dependencyList;
    }

    private String extractProjectName(String line) {
        try {
            return line.split(":jar:")[0].split(":")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Invalid project line format: " + line);
            return null;
        }

    }

    private DependencyEntry createDependencyEntry(String line, String currentProject) {
        String dependencyLine = line.replaceAll("[|+\\-]", "").trim();
        String[] parts = dependencyLine.split(":");
        if (parts.length >= 4) {
            String dependency = String.join(":", Arrays.copyOfRange(parts, 0, 2));
            String version = parts[3];
            return new DependencyEntry(dependency, version, currentProject);
        }
        return null;
    }
}
