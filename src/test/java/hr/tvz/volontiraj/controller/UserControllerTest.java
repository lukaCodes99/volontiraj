package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.dto.EventDto;
import hr.tvz.volontiraj.dto.UserDto;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_ShouldReturnUserDto_WhenUserExists() {
        UserDto userDto = new UserDto();
        when(userService.findById(1L)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userService.findById(1L)).thenThrow(new EntityNotFoundException());

        ResponseEntity<UserDto> response = userController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        UserEntity userEntity = new UserEntity();
        UserDto savedUser = new UserDto();
        when(userService.save(userEntity)).thenReturn(savedUser);

        ResponseEntity<UserDto> response = userController.createUser(userEntity);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedUser, response.getBody());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserExists() {
        UserEntity updateUser = new UserEntity();
        UserDto updatedUser = new UserDto();
        when(userService.update(1L, updateUser)).thenReturn(updatedUser);

        ResponseEntity<UserDto> response = userController.updateUser(1L, updateUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void updateUser_ShouldReturnNotFound_WhenUserDoesNotExist() {
        UserEntity updateUser = new UserEntity();
        when(userService.update(1L, updateUser)).thenThrow(new EntityNotFoundException());

        ResponseEntity<UserDto> response = userController.updateUser(1L, updateUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserDeleted() {
        doNothing().when(userService).deleteById(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() {
        doThrow(new EntityNotFoundException()).when(userService).deleteById(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getUserVolunteerHistory_ShouldReturnEventDto()
    {
        when(userService.getUserVolunteerHistory(1L)).thenReturn(List.of(new EventDto()));

        ResponseEntity<List<EventDto>> response = userController.getUserVolunteerHistory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(new EventDto()), response.getBody());
    }

}
