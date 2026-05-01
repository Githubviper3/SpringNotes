# Spring Boot Notes Application

## Requirements

- Create a simple Spring Boot project  
- Create a Note model (id, title, content, category, createdAt, updatedAt)

## APIs

- Add POST API to create a note  
- Add GET API to fetch all notes  
- Add GET by id API  
- Add PUT API to update a note  
- Add DELETE API to remove a note  
- Add API to filter notes by category  

## Validation

- Add basic validation (title/content should not be empty)

## Persistence

- Store notes in a JSON file (e.g. `notes.json`)  
- On app start → load notes from file  
- On create/update/delete → write back to file  

## Error Handling

- Handle basic file errors (file missing / empty)

## Project Structure

- Structure code properly (controller → service → model)

## Testing

- Add a few basic tests (create, get by id, not found case)

## File input
- mapper.writeValue(new File(fileloc),notesObject); 
- mapper.readValue(new File(fileloc),new TypeReference<>() {});