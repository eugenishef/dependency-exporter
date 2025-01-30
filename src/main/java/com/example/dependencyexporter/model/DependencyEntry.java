package com.example.dependencyexporter.model;

public class DependencyEntry {
    private final String dependency;
    private final String version;
    private final String projects;

    public DependencyEntry(String dependency, String version, String projects) {
        this.dependency = dependency;
        this.version = version;
        this.projects = projects;
    }

    public String getDependency() {
        return dependency;
    }

    public String getVersion() {
        return version;
    }

    public String getProjects() {
        return projects;
    }

    @Override
    public String toString() {
        return "DependencyEntry{" +
                "dependency='" + dependency + '\'' +
                ", version='" + version + '\'' +
                ", projects='" + projects + '\'' +
                '}';
    }
}
