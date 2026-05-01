package com.example.springapi.notes.services;

import com.example.springapi.notes.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NotesService {
    private ArrayList<Note> savedNotes = new ArrayList<>();
    private AtomicLong idCounter;
    private final NoteFileReaderService readerService;

    public NotesService(NoteFileReaderService reader, String file_location) throws Exception {
        this.readerService = reader;
        this.load(file_location);
    }

    @Autowired
    public NotesService(NoteFileReaderService reader) throws Exception {
        this.readerService = reader;
        this.load("src/data/notes.json");
    }

    public void load(String file_location) throws Exception {
        savedNotes = this.readerService.loadNotesJson(file_location);
        idCounter = new AtomicLong(savedNotes.size() + 1);
    }

    public Boolean save(String file_location) throws Exception {
        return this.readerService.saveNotes(file_location, this.savedNotes);
    }

    public Boolean save() throws Exception {
        return this.save("src/data/output.json");
    }

    public Boolean updateNote(String title, String newTitle, String content, String category) throws Exception {
        Optional<Note> currentNote = this.getNoteByTitle(title);
        if (currentNote.isPresent()) {
            Note currentNoteObject = currentNote.get();
            this.savedNotes.replaceAll(n ->
                    n.id().equals(currentNoteObject.id())
                            ? new Note(n.id(), newTitle, content, category, n.createdAt(),LocalDate.now().toString())
                            : n
            );
            return true;
        }
        return false;
    }

    static public void toPrettyString(Note note) {
        System.out.printf(
                "{%n\tid: %s,%n\ttitle: %s, %n\tcontent: %s, %n\tcategory: %s, %n\tcreatedAt: %s, %n\tupdatedAt: %s%n}%n",
                note.id(),
                note.title(),
                note.content(),
                note.category(),
                note.createdAt(),
                note.updatedAt()
        );
    }

    static public void toPrettyString(ArrayList<Note> inputNotes) {
        for (Note note : inputNotes) {
            toPrettyString(note);
        }
    }


    //Get all
    public ArrayList<Note> getAllNotes() {
        return savedNotes;
    }


    //get by ID
    public Optional<Note> getNotesById(long id) {
        return savedNotes.stream()
                .filter(g -> g.id().equals(id))
                .findFirst();

    }

    //filter by category
    public ArrayList<Note> getNotesByCategory(String category) {
        return new ArrayList<>(
                savedNotes.stream()
                        .filter(g -> g.category().equals(category))
                        .toList()
        );
    }

    //get by title
    public Optional<Note> getNoteByTitle(String title) {
        return savedNotes.stream()
                .filter(g -> g.title().equals(title))
                .findFirst();
    }

    public HashMap<String, String> isNoteValid(String title, String content) {
        HashMap<String, String> messageData = new HashMap<>();
        String messageTitle = "Valid";
        String messageContent = "Valid";
        Optional<Note> checkNote = this.getNoteByTitle(title);
        if (checkNote.isPresent()) {
            messageTitle = "Invalid";
            messageContent = "Invalid Title: Note must have unique title";
        } else if (title == null || title.isBlank()) {
            messageTitle = "Invalid";
            messageContent = "Invalid Title: Note must have title provided";
        } else if (content == null || content.isBlank()) {
            messageTitle = "Invalid";
            messageContent = "Invalid Content: Note must have content";
        }
        messageData.put("title", messageTitle);
        messageData.put("content", messageContent);
        return messageData;
    }

    //create note
    public Note addNote(String title, String content, String category) {
        HashMap<String, String> validationResult = this.isNoteValid(title, content);
        if (validationResult.get("title").equals("Invalid")) {
            return new Note((long) 1, validationResult.get("title"), validationResult.get("content"), "Invalid", "Now", "Now");
        }

        String createdAt = LocalDate.now().toString();
        long newId = idCounter.getAndIncrement();
        Note newNote = new Note(
                newId,
                title,
                content,
                category,
                createdAt,
                createdAt
        );

        savedNotes.add(newNote);
        return newNote;
    }

    //remove note
    public Optional<Note> removeNotebyID(long id) {
        Optional<Note> note = getNotesById(id);
        if (note.isPresent()) {
            this.savedNotes.removeIf(n -> n.id().equals(id));
            savedNotes.replaceAll(n -> {
                int index = savedNotes.indexOf(n) + 1;
                return new Note(
                        (long) index,
                        n.title(),
                        n.content(),
                        n.category(),
                        n.createdAt(),
                        n.updatedAt()
                );
            });
        }
        return note;
    }


    public Optional<Note> removeNotebyTitle(String title) {
        Optional<Note> note = getNoteByTitle(title);
        note.ifPresent(value -> this.removeNotebyID(value.id()));
        return note;
    }


}
