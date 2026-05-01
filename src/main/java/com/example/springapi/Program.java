package com.example.springapi;

import com.example.springapi.notes.services.NoteFileReaderService;
import com.example.springapi.notes.services.NotesService;

public class Program {
    public static void main(String[] args) throws Exception {
        NotesService notesHandler = new NotesService(new NoteFileReaderService());
        notesHandler.removeNotebyTitle("First Note");
        notesHandler.save();
    }
}