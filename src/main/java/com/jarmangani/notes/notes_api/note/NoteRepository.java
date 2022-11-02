package com.jarmangani.notes.notes_api.note;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.jarmangani.notes.notes_api.user.User;

public interface NoteRepository extends CrudRepository<Note, Integer>{
    Set<Note> findByAuthor(User author);
}
