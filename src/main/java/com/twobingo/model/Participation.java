package com.twobingo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation")
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_id")
    private Grid grid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User useraccount;

    @Column(length = 100)
    private String pseudo;

    @Column(length = 50)
    private String color;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Participation() {
    }

    public Participation(Grid grid, User user, String pseudo, String color) {
        this.grid = grid;
        this.useraccount = user;
        this.pseudo = pseudo;
        this.color = color;
    }

    public Participation(Grid grid, String pseudo, String color) {
        this.grid = grid;
        this.pseudo = pseudo;
        this.color = color;
    }

    public Participation(Grid grid, User user, String color) {
        this.grid = grid;
        this.useraccount = user;
        this.pseudo = user.getUsername();
        this.color = color;
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

    public User getUser() {
        return useraccount;
    }

    public void setUser(User user) {
        this.useraccount = user;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
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
