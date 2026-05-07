package com.twobingo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "cell")
public class Cell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_id")
    private Grid grid;

    @Column(length = 1000)
    private String content;

    @Column(name = "is_checked")
    private Boolean isChecked = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "position")
    private Integer position;

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

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // overwrite toString for better logging
    @Override
    public String toString() {
        return "Cell{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", isChecked=" + isChecked +
                ", createdAt=" + createdAt +
                ", gridId=" + (grid != null ? grid.getId() : "No Grid") +
                ", position=" + position +
                '}';
    }

    public Map<String, Object> toResponseMap() {
        System.out.println("Converting Cell to response map: " + this.toString());
        return Map.of(
                "id", id,
                "content", content != null ? content : "",
                "isChecked", isChecked,
                "createdAt", createdAt,
                "position", position != null ? position : -1);
    }

}
