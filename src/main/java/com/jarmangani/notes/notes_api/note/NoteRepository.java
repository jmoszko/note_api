package com.jarmangani.notes.notes_api.note;

import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, Integer>{
    
}
