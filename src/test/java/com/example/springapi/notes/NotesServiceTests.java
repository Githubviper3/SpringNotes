package com.example.springapi.notes;

import com.example.springapi.notes.models.Note;
import com.example.springapi.notes.services.NoteFileReaderService;
import com.example.springapi.notes.services.NotesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotesServiceTests {
    ArrayList<Note> fakeNotes = new ArrayList<>();

    @Mock
    NoteFileReaderService fileReaderService;

    @InjectMocks
    NotesService notesHandler;

    @BeforeEach
    void init() throws Exception {

        fakeNotes.add(new Note(
                1L,
                "first",
                "Content",
                "Work",
                "Now",
                "Now"
        ));
        fakeNotes.add(new Note(
                2L,
                "Test",
                "Content",
                "Work",
                "Now",
                "Now"
        ));
        fakeNotes.add(new Note(
                3L,
                "First Note",
                "This is the first note",
                "general",
                "2026-04-17T10:00:00",
                "2026-04-17T10:00:00"
        ));
        when(fileReaderService.loadNotesJson("test.json"))
                .thenReturn(fakeNotes);

        notesHandler = new NotesService(fileReaderService, "test.json");
    }

    @Test
    void testAddNoteTitleExists() {
        Note returnedNote = notesHandler.addNote(
                "Test",
                "second",
                "third");
        assertEquals("Invalid", returnedNote.title());
        assertEquals("Invalid Title: Note must have unique title", returnedNote.content());
    }

    @Test
    void testAddNoteTitleNull() {
        Note returnedNote = notesHandler.addNote(
                null,
                "second",
                "third");
        assertEquals("Invalid", returnedNote.title());
        assertEquals("Invalid Title: Note must have title provided", returnedNote.content());
    }

    @Test
    void testAddNoteContentNull() {
        Note returnedNote = notesHandler.addNote(
                "null",
                "",
                "third");
        assertEquals("Invalid", returnedNote.title());
        assertEquals("Invalid Content: Note must have content", returnedNote.content());
    }

    @Test
    void testAddNotePass() {
        Note returnedNote = notesHandler.addNote(
                "second",
                "second",
                "third");
        assertEquals("third", returnedNote.category());
        assertEquals(4L, returnedNote.id());
    }

    @Test
    void testGetAllNotes() {
        ArrayList<Note> storedNotes = notesHandler.getAllNotes();
        assertEquals(storedNotes.size(), fakeNotes.size());
    }


    @Test
    void testGetNotesById() {
        Optional<Note> firstNote = notesHandler.getNotesById(1);
        assertTrue(firstNote.isPresent());
        Note noteObject = firstNote.get();
        assertEquals(1, noteObject.id());
        assertEquals("first", noteObject.title());
        assertEquals("Content", noteObject.content());
        assertEquals("Work", noteObject.category());
        assertEquals("Now", noteObject.createdAt());
        assertEquals("Now", noteObject.updatedAt());
    }

    @Test
    void testGetNotesByGeneralCategory() {
        ArrayList<Note> generalNotes = notesHandler.getNotesByCategory("general");
        assertEquals(1, generalNotes.size());
        Note noteObject = generalNotes.get(0);
        assertEquals("First Note", noteObject.title());
        assertEquals(3L, noteObject.id());
    }

    @Test
    void testGetNotesByWorkCategory() {
        ArrayList<Note> generalNotes = notesHandler.getNotesByCategory("Work");
        assertEquals(2, generalNotes.size());
        Note firstNoteObject = generalNotes.get(0);
        Note secondNoteObject = generalNotes.get(1);
        assertEquals("first", firstNoteObject.title());
        assertEquals("Test", secondNoteObject.title());
        assertEquals(1L, firstNoteObject.id());
        assertEquals(2L, secondNoteObject.id());
    }


    @Test
    void testRemoveNotebyIdFail() {
        Optional<Note> removedNote = notesHandler.removeNotebyID(4);
        assertTrue(removedNote.isEmpty());
    }

    @Test
    void testRemoveNotebyIDPass() {
        Optional<Note> removedNote = notesHandler.removeNotebyID(3);
        assertTrue(removedNote.isPresent());

        ArrayList<Note> currentNotes = notesHandler.getAllNotes();
        assertEquals(2,currentNotes.size());
    }

    @Test
    void testRemoveNotebyTitleFail() {
        Optional<Note> removedNote = notesHandler.removeNotebyTitle("Some Title");
        assertTrue(removedNote.isEmpty());
    }

    @Test
    void testRemoveNotebyTitlePass() {
        Optional<Note> removedNote = notesHandler.removeNotebyTitle("First Note");
        assertTrue(removedNote.isPresent());

        assertEquals(3L, removedNote.get().id());
        ArrayList<Note> currentNotes = notesHandler.getAllNotes();
        assertEquals(2,currentNotes.size());
    }





}