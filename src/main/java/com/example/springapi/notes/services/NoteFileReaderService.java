package com.example.springapi.notes.services;

import com.example.springapi.notes.models.Note;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Service
public class NoteFileReaderService  {
    public ArrayList<Note> loadNotesJson(String file_location) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File(file_location);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        return mapper.readValue(file, new TypeReference<ArrayList<Note>>() {});
    }

    public Boolean saveNotes(String file_location,ArrayList<Note> notes) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(file_location), notes);
        } catch (Exception e){
            final String errorMessage = "Error type: " + e.getClass().getName();
            System.out.println(errorMessage);
            e.printStackTrace();
            throw new Exception(e);
        }
        return true;
    }

}
