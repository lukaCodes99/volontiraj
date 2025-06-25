package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.dto.EventDto;
import hr.tvz.volontiraj.dto.UserDto;
import hr.tvz.volontiraj.model.Event;
import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void findById_ShouldReturnUserDto() {
        Long id = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));

        UserDto result = userServiceImpl.findById(id);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findById(id);
    }

    @Test
    void findById_ShouldThrowEntityNotFoundException() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userServiceImpl.findById(id));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void save_ShouldSaveUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserDto result = userServiceImpl.save(userEntity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).save(userEntity);
    }

    @Test
    void update_ShouldUpdateUser() {
        Long id = 1L;
        UserEntity existingUser = new UserEntity();
        existingUser.setId(id);
        existingUser.setUsername("oldUser");
        existingUser.setProfilePicturePath("oldPath");

        UserEntity userEntityToUpdate = new UserEntity();
        userEntityToUpdate.setUsername("newUser");
        userEntityToUpdate.setProfilePicturePath("newPath");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserDto updatedUser = userServiceImpl.update(id, userEntityToUpdate);

        assertEquals("newUser", updatedUser.getUsername());
        assertEquals("newPath", updatedUser.getProfilePicturePath());
        verify(userRepository).save(existingUser);
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        Long id = 1L;
        UserEntity updateInfo = new UserEntity();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userServiceImpl.update(id, updateInfo);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldDeleteUser() {
        Long id = 1L;

        userServiceImpl.deleteById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        String email = "email@email.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        UserEntity result = userServiceImpl.findByEmail(email);

        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void currentUserEmail_ShouldReturnUser() {
        String expectedEmail = "user@example.com";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(expectedEmail);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        String result = userServiceImpl.currentUserEmail();

        assertEquals(expectedEmail, result);

        SecurityContextHolder.clearContext();
    }

    @Test
    void getUserVolunteerHistory_ShouldReturnUserVolunteerHistory() {
        Long id = 1L;

        Event event1 = new Event();
        event1.setId(4L);
        event1.setCategory(EventCategory.PETS);
        List<Event> events = List.of(event1);

        when(userRepository.findAttendingEventsByUserId(id)).thenReturn(events);

        List<EventDto> result = userServiceImpl.getUserVolunteerHistory(id);

        assertNotNull(result);
        verify(userRepository).findAttendingEventsByUserId(id);
    }

    @Test
    void getCurrentUser_ShouldReturnCurrentUser() {
        UserServiceImpl spyService = Mockito.spy(userServiceImpl);
        String currentUserEmail = "currentUser@example.com";

        UserEntity user = new UserEntity();
        user.setId(1L);

        doReturn(currentUserEmail).when(spyService).currentUserEmail();
        when(userRepository.findByEmail(currentUserEmail)).thenReturn(user);

        UserEntity result = spyService.getCurrentUser();

        assertEquals(result.getId(), user.getId());
    }

}
