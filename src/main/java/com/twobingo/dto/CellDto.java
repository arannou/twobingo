package com.twobingo.dto;

import com.google.gson.annotations.Expose;

public class CellDto {
    @Expose
    private String content;

    @Expose
    private Boolean checked = false;

    @Expose
    private Integer position;

    public CellDto() {
    }

    public CellDto(String content, Boolean checked, Integer position) {
        this.content = content;
        this.checked = checked;
        this.position = position;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "CellDto{" +
                "content='" + content + '\'' +
                ", checked=" + checked +
                ", position=" + position +
                '}';
    }
}