package com.example.jointperchasesnew.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime updated;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID getId() {
        return id;
    }

    @NotNull(message = "The creation time can't be null")
    public LocalDateTime getCreated() {
        return created;
    }

    @NotNull(message = "The update time can't be null")
    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}