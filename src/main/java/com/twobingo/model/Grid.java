package com.twobingo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "grid")
public class Grid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer size;

    @Column(length = 100)
    private String theme;

    @Column(length = 50)
    private String color;

    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User creator;

    @OneToMany(mappedBy = "grid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cell> cells;

    @OneToMany(mappedBy = "grid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations;

    public Grid() {
    }

    public Grid(String name, String description, Integer size, String color, String theme) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.theme = theme;
        this.color = color;
    }

    public Grid(String name, String description, Integer size, String theme, String color, List<Cell> cells) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.theme = theme;
        this.color = color;
        this.cells = cells;
    }

    public Grid(String name, String description, Integer size, String color, String theme, User creator) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.theme = theme;
        this.color = color;
        this.creator = creator;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // overwrite toString for better logging
    @Override
    public String toString() {
        return "Grid{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description=" + description +
                ", createdAt=" + createdAt +
                ", size=" + size +
                ", theme=" + theme +
                ", color=" + color +
                ", creator=" + (creator != null ? creator.getUsername() : "Creator not found") +
                '}';
    }

    public Map<String, Object> toResponseMap() {
        return Map.of(
                "id", id,
                "name", name,
                "description", description,
                "size", size,
                "theme", theme,
                "color", color,
                "isArchived", isArchived,
                "creatorUsername", creator != null ? creator.getUsername() : "Creator not found",
                "cells", cells.stream().map(cell -> Map.of(
                        "id", cell.getId(),
                        "content", cell.getContent(),
                        "isChecked", cell.getIsChecked(),
                        "position", cell.getPosition())).toList());
    }
}
