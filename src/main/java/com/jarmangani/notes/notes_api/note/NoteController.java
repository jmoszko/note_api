package com.jarmangani.notes.notes_api.note;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collector;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
class NoteInput {
    private Set<String> tags;
    private String topic;
    private String content;
}

@Controller
@Slf4j
@RequestMapping(path="/notes")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;

    @PostMapping(path="/add")
    public ResponseEntity addNote(@RequestBody NoteInput noteInput) {
        String userEmail = "jaroslaw.moszkowski@motorolasolutions.com";
        User author = userRepository.findByEmail(userEmail);
        List<String> missingTags = findMissingTags(noteInput);
        if(!missingTags.isEmpty()) {
            String missingTagsString = missingTags.stream().collect(Collectors.joining(","));
            log.warn("Unable to create note due to missing tags: " + missingTagsString);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Unknown tags: " + missingTagsString);
        }
        Note note = Note.builder()
            .tags(noteInput.getTags().stream().map(tagName -> new Tag(tagName)).collect(Collectors.toSet()))
            .author(author)
            .creationTime(new java.sql.Time(System.currentTimeMillis()))
            .topic(noteInput.getTopic())
            .content(noteInput.getContent())
            .build();
        noteRepository.save(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    private List<String> findMissingTags(NoteInput noteInput) {
        Map<String, Boolean> tagExistence = new TreeMap<>();
        for(String tag: noteInput.getTags()) {
            tagExistence.put(tag, tagRepository.existsByTag(tag));
        }
        if(tagExistence.values().stream().filter(v -> !v).findAny().isPresent()) {
            return tagExistence.entrySet().stream().filter(e -> !e.getValue()).map(e -> e.getKey()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
