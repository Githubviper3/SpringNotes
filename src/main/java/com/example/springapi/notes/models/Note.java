package com.example.springapi.notes.models;

public record Note(
        Long id,
        String title,
        String content,
        String category,
        String createdAt,
        String updatedAt
) {
}