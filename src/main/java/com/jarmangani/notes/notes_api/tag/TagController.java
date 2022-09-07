package com.jarmangani.notes.notes_api.tag;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/tags")
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @PostMapping(path="/add")
    public ResponseEntity<Tag> addTag(@RequestBody Tag tag) {
        tagRepository.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(tag);
    }

    @GetMapping(path="")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = StreamSupport.stream(tagRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tags);
    }
}
