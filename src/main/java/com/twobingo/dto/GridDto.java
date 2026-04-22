package com.twobingo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import java.time.LocalDateTime;
import java.util.List;

import com.twobingo.model.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GridDto {

    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private Integer size;
    @Expose
    private String theme;
    @Expose
    private String color;
    @Expose
    private Boolean isArchived = false;
    @Expose
    private LocalDateTime createdAt;
    @Expose
    private User creator;
    @Expose
    private List<CellDto> cells;

    public GridDto() {
    }

    public GridDto(String name, String description, Integer size, String theme, String color, List<CellDto> cells) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.theme = theme;
        this.color = color;
        this.cells = cells;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<CellDto> getCells() {
        return cells;
    }

    public void setCells(List<CellDto> cells) {
        this.cells = cells;
    }

    // toString method
    @Override
    public String toString() {
        return "GridDto{" +
                ", name='" + name + '\'' +
                ", description=" + description +
                ", size=" + size +
                ", theme=" + theme +
                ", color=" + color +
                ", creator=" + (creator != null ? creator.getUsername() : "Creator not found") +
                '}';
    }
}