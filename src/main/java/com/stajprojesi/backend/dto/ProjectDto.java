package com.stajprojesi.backend.dto;

import java.util.Set;

public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private String status;
    private OwnerDto owner;
    private Set<FileDto> files;

    public static class OwnerDto {
        private String username;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    public static class FileDto {
        private Long id;
        private String fileName;
        private String fileType;
        private String filePath;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        public String getFileType() { return fileType; }
        public void setFileType(String fileType) { this.fileType = fileType; }
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public OwnerDto getOwner() { return owner; }
    public void setOwner(OwnerDto owner) { this.owner = owner; }
    public Set<FileDto> getFiles() { return files; }
    public void setFiles(Set<FileDto> files) { this.files = files; }
}
