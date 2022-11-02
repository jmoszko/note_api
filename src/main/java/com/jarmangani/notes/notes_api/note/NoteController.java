package com.jarmangani.notes.notes_api.note;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.jarmangani.notes.notes_api.tag.Tag;
import com.jarmangani.notes.notes_api.tag.TagRepository;
import com.jarmangani.notes.notes_api.user.User;
import com.jarmangani.notes.notes_api.user.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
class NoteInput {
    private Set<String> tags;
    private String topic;
    private String content;
}

@Controller
@Slf4j
@RequestMapping(path="/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @GetMapping(path="") 
    public ResponseEntity<Set<Note>> getNotes() {
        User author = userRepository.findByEmail(getUserEmail());
        Set<Note> notes = noteRepository.findByAuthor(author);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(notes);
    }

    @PostMapping(path="/add")
    public ResponseEntity<Note> addNote(@RequestBody NoteInput noteInput) {
        User author = userRepository.findByEmail(getUserEmail());
        List<String> missingTags = findMissingTags(noteInput);
        if(!missingTags.isEmpty()) {
            String missingTagsString = missingTags.stream().collect(Collectors.joining(","));
            log.warn("Unable to create note due to missing tags: " + missingTagsString);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown tags: " + missingTagsString);
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

    private String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userEmail;
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
