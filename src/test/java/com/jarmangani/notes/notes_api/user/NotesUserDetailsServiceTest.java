package com.jarmangani.notes.notes_api.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class NotesUserDetailsServiceTest {
    
    @Mock private UserRepository userRepository;
    @InjectMocks private NotesUserDetailsService detailsService;
    private String initialUserName = "admin";
    private User initialUser = User.builder().email(initialUserName).password("admin_passwd").id(10000).build();
    private User user = User.builder().email("user@mail.pl").password("user_passwd").id(10000).build();

    @Test
    void loginAsAdmin_successIfNoOtherUsers() {
        when(userRepository.findByEmail(eq(initialUserName))).thenReturn(initialUser);
        when(userRepository.count()).thenReturn(Long.valueOf(1));
        detailsService.loadUserByUsername(initialUserName);
    }

    @Test
    void loginAsAdmin_failIfOtherUsers() {
        when(userRepository.findByEmail(eq(initialUserName))).thenReturn(initialUser);
        when(userRepository.count()).thenReturn(Long.valueOf(7));
        assertThrows(DisabledException.class, () -> detailsService.loadUserByUsername(initialUser.getEmail()));
    }

    @Test
    void loginAsUser_success() {
        when(userRepository.findByEmail(eq(user.getEmail()))).thenReturn(user);
        detailsService.loadUserByUsername(user.getEmail());
    }

    @Test
    void loginAsUser_failIfNoSuchUser() {
        when(userRepository.findByEmail(eq(user.getEmail()))).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> detailsService.loadUserByUsername(user.getEmail()));
    }
}
