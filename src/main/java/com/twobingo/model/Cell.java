package com.twobingo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cell")
public class Cell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // foreign key to grid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_id")
    private Long grid_id;

    @Column(length = 1000)
    private String content;

    @Column(name = "is_checked")
    private Boolean isChecked = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Cell() {
    }

    public Cell(String content) {
        this.content = content;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGrid_id() {
        return grid_id;
    }

    public void setGrid_id(Long grid_id) {
        this.grid_id = grid_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
