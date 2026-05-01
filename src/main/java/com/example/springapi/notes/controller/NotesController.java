package com.example.springapi.notes.controller;

import com.example.springapi.notes.models.Note;
import com.example.springapi.notes.models.NoteReqObj;
import com.example.springapi.notes.services.NoteFileReaderService;
import com.example.springapi.notes.services.NotesService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/notes")
public class NotesController {
    NotesService notesHandler;

    public NotesController() throws Exception {
        // Rewrite this all to use a service to deal with buisiness logic i.e. this model stuff
        this.notesHandler = new NotesService(new NoteFileReaderService());
    }

    @GetMapping("/{id}")
    Optional<Note> getGroupById(@PathVariable long id) {
        return this.notesHandler.getNotesById(id);
    }

    @GetMapping("/")
    ArrayList<Note> getNotes(@RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            return this.notesHandler.getNotesByCategory(category);
        }
        return this.notesHandler.getAllNotes();
    }

    @GetMapping("/title/{title}")
    Optional<Note> getByTitle(@PathVariable String title) {
        return notesHandler.getNoteByTitle(title);
    }


    @PostMapping("/add")
    Note addNote(@RequestBody NoteReqObj body) throws Exception {
        Note created = this.notesHandler.addNote(body.title(),body.content(),body.category());
        if (created.title().equals(body.title())) {
            System.out.println("test");
            this.notesHandler.save("src/data/notes.json");
            this.notesHandler.save();
        }
        return created;
    }

    @PostMapping("/removebytitle")
    Boolean removeNote(@RequestBody String title) throws Exception {
        Optional<Note> removed = this.notesHandler.removeNotebyTitle(title);
        this.notesHandler.save();
        return removed.isPresent();
    }

}