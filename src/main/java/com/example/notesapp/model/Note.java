

package com.example.notesapp.model;

import jakarta.persistence.*;

@Entity
public class Note {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String subject;
    private String filePath;
    private Long userId;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
