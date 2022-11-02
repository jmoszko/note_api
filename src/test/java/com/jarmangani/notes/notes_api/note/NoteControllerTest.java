package com.jarmangani.notes.notes_api.note;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Time;

import com.jarmangani.notes.notes_api.tag.TagRepository;
import com.jarmangani.notes.notes_api.user.User;
import com.jarmangani.notes.notes_api.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class NoteControllerTest {

    @Mock private NoteRepository noteRepository;
    @Mock private UserRepository userRepository;
    @Mock private TagRepository tagRepository;

    @InjectMocks private NoteController noteController;

    private final String email = "test.user@notes.pl";
    User user = new User(1, email, "pass");

    @BeforeEach
    void setUpSecurityContext() {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(
                email, null));
    }

    @Test
    void getAllNotesForUser_success() {
        Note note = Note.builder().author(user).content("Content").creationTime(new Time(166738623)).id(1)
                .tags(new HashSet<>()).topic("Topic").build();
        when(noteRepository.findByAuthor(eq(user))).thenReturn(Collections.singleton(note));
        when(userRepository.findByEmail(eq(email))).thenReturn(user);

        ResponseEntity<Set<Note>> notesResponse = noteController.getNotes();
        
        assertEquals(note, notesResponse.getBody().iterator().next());
    }

    @Test
    void createNote_success() {
        NoteInput noteInput = new NoteInput(Collections.singleton("Tag"), "Topic", "Content");
        when(userRepository.findByEmail(eq(email))).thenReturn(user);
        when(tagRepository.existsByTag(eq("Tag"))).thenReturn(true);

        ResponseEntity<Note> noteResponse = noteController.addNote(noteInput);

        ArgumentCaptor<Note> argumentCaptor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).save(argumentCaptor.capture());
        assertEquals(user, argumentCaptor.getValue().getAuthor());
        assertEquals(noteInput.getContent(), argumentCaptor.getValue().getContent());
        assertEquals(noteInput.getTopic(), argumentCaptor.getValue().getTopic());
        assertEquals(noteInput.getTags(),
                argumentCaptor.getValue().getTags().stream().map(t -> t.getTag()).collect(Collectors.toSet()));

        assertEquals(HttpStatus.valueOf(201), noteResponse.getStatusCode());
        assertEquals(argumentCaptor.getValue(), noteResponse.getBody());
    }

    @Test
    void createNote_failIfMissingTag() {
        NoteInput noteInput = new NoteInput(Collections.singleton("another-tag"), "Topic", "Content");
        when(userRepository.findByEmail(eq(email))).thenReturn(user);
        when(tagRepository.existsByTag(eq("another-tag"))).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            noteController.addNote(noteInput);
        });
        assertEquals(404, exception.getRawStatusCode());

    }
}
