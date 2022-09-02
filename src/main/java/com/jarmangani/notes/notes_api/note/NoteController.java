package com.jarmangani.notes.notes_api.note;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jarmangani.notes.notes_api.tag.Tag;
import com.jarmangani.notes.notes_api.tag.TagRepository;
import com.jarmangani.notes.notes_api.user.User;
import com.jarmangani.notes.notes_api.user.UserRepository;

import lombok.Data;

@Data
class NoteInput {
    private Set<String> tags;
    private String topic;
    private String content;
}

@Controller
@RequestMapping(path="/notes")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;

    @PostMapping(path="/add")
    public ResponseEntity<Note> addNote(@RequestBody NoteInput noteInput) {
        String userEmail = "jaroslaw.moszkowski@motorolasolutions.com";
        User author = userRepository.findByEmail(userEmail);
        // addMissingTags(noteInput);
        Note note = Note.builder()
            // .tags(noteInput.getTags().stream().map(tagName -> new Tag(tagName)).collect(Collectors.toSet()))
            .author(author)
            .creationTime(new java.sql.Time(System.currentTimeMillis()))
            .topic(noteInput.getTopic())
            .content(noteInput.getContent())
            .build();
        noteRepository.save(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    private void addMissingTags(NoteInput noteInput) {
        Set<String> existingTagNames = StreamSupport.stream(tagRepository.findAll().spliterator(), false).map(tag -> tag.getTag()).collect(Collectors.toSet());
        Set<String> missingTagNames = new HashSet<>(noteInput.getTags());
        missingTagNames.removeAll(existingTagNames);
        tagRepository.saveAll(missingTagNames.stream().map(tagName -> new Tag(tagName)).collect(Collectors.toList()));
    }
}
