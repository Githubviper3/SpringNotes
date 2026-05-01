package com.example.springapi.notes.models;

public record NoteReqObj(
        String title,
        String content,
        String category
) {
}
