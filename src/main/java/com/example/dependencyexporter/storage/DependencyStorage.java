package com.example.dependencyexporter.storage;

import com.example.dependencyexporter.model.DependencyEntry;

import java.util.List;
import java.util.Map;

interface DependencyStorage {
    Map<String, DependencyEntry> load();
    void save(List<DependencyEntry> dependencies);
}
