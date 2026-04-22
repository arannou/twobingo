package com.twobingo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CellDto {
    @Expose
    private String content;

    @Expose
    @SerializedName("checked")
    @JsonProperty("checked")
    private Boolean isChecked = false;

    @Expose
    private Integer position;

    public CellDto() {
    }

    public CellDto(String content, Boolean isChecked, Integer position) {
        this.content = content;
        this.isChecked = isChecked;
        this.position = position;
    }

    // Getters and Setters
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
                ", isChecked=" + isChecked +
                ", position=" + position +
                '}';
    }
}