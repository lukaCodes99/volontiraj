package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.dto.EventSummaryDto;
import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.service.AdminService;
import hr.tvz.volontiraj.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private AdminService adminService;
    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;


    @Test
    void getTotalEvents_success() {
        when(adminService.getTotalEvents()).thenReturn(42L);

        ResponseEntity<Long> response = adminController.getTotalEvents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(42L, response.getBody());
    }

    @Test
    void testGetEventCountByCategory_success() {
        Map<EventCategory, Long> mockMap = new EnumMap<>(EventCategory.class);
        for (EventCategory category : EventCategory.values()) {
            mockMap.put(category, 5L);
        }

        when(adminService.getEventCountByCategory()).thenReturn(mockMap);

        ResponseEntity<Map<EventCategory, Long>> response = adminController.getEventCountByCategory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMap, response.getBody());
    }

    @Test
    void testGetEventCountBySpecificCategory_success() {
        when(adminService.getEventCountByCategory(EventCategory.PETS)).thenReturn(10L);

        ResponseEntity<Long> response = adminController.getEventCountByCategory(EventCategory.PETS);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10L, response.getBody());
    }

    @Test
    void testGetTotalUsers_success() {
        when(adminService.getTotalUsers()).thenReturn(42L);

        ResponseEntity<Long> response = adminController.getTotalUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(42L, response.getBody());
    }

    @Test
    void testGetTop5EventsByUpvotes_success() {
        List<EventSummaryDto> mockList = List.of(new EventSummaryDto(1L, "Event 1", 100, EventCategory.PETS.name()));
        when(adminService.getTop5EventsByUpvotes()).thenReturn(mockList);

        ResponseEntity<List<EventSummaryDto>> response = adminController.getTop5EventsByUpvotes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockList, response.getBody());
    }

    @Test
    void testGetAdminPage_success() {
        when(userService.currentUserEmail()).thenReturn("admin@example.com");

        ResponseEntity<Object> response = adminController.getAdminPage();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("admin@example.com", response.getBody());
    }

    @Test
    void testGetAdminPage_failure() {
        when(userService.currentUserEmail()).thenThrow(new RuntimeException("DB Down"));

        ResponseEntity<Object> response = adminController.getAdminPage();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while accessing the admin page.", response.getBody());
    }
}
