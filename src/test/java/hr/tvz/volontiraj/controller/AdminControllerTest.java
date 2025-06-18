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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;
    @MockitoBean
    private UserService userService;

    //@InjectMocks
    //private AdminController adminController;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTotalEvents() throws Exception {
        when(adminService.getTotalEvents()).thenReturn(42L);
        mockMvc.perform(get("/api/admin/total-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(42L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEventCountByCategory() throws Exception {
        Map<EventCategory, Long> counts = new EnumMap<>(EventCategory.class);
        counts.put(EventCategory.PETS, 10L);
        counts.put(EventCategory.PEOPLE, 5L);
        counts.put(EventCategory.COMMUNITY, 5L);
        counts.put(EventCategory.OTHER, 2L);

        when(adminService.getEventCountByCategory()).thenReturn(counts);

        mockMvc.perform(get("/api/admin/events-by-category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.PETS").value(10L))
                .andExpect(jsonPath("$.PEOPLE").value(5L))
                .andExpect(jsonPath("$.COMMUNITY").value(5L))
                .andExpect(jsonPath("$.OTHER").value(2L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEventCountBySpecificCategory() throws Exception {
        when(adminService.getEventCountByCategory(EventCategory.PETS)).thenReturn(10L);

        mockMvc.perform(get("/api/admin/events-by-category/PETS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10L));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTotalUsers() throws Exception {
        when(adminService.getTotalUsers()).thenReturn(100L);

        mockMvc.perform(get("/api/admin/total-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100L));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTop5EventsByUpvotes() throws Exception {
        when(adminService.getTop5EventsByUpvotes()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/admin/top5-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(new ArrayList<>()));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAdminPage() throws Exception {
        when(userService.currentUserEmail()).thenReturn("admin@example.com");

        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("admin@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAdminPage_failure() throws Exception {
        when(userService.currentUserEmail()).thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while accessing the admin page."));

    }

    @Test
    void testAccessDeniedForUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/api/admin/total-events"))
                .andExpect(status().isForbidden());
    }


//    @Test
//    void getTotalEvents_success() {
//        when(adminService.getTotalEvents()).thenReturn(42L);
//
//        ResponseEntity<Long> response = adminController.getTotalEvents();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(42L, response.getBody());
//    }
//
//    @Test
//    void testGetEventCountByCategory_success() {
//        Map<EventCategory, Long> mockMap = new EnumMap<>(EventCategory.class);
//        for (EventCategory category : EventCategory.values()) {
//            mockMap.put(category, 5L);
//        }
//
//        when(adminService.getEventCountByCategory()).thenReturn(mockMap);
//
//        ResponseEntity<Map<EventCategory, Long>> response = adminController.getEventCountByCategory();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockMap, response.getBody());
//    }
//
//    @Test
//    void testGetEventCountBySpecificCategory_success() {
//        when(adminService.getEventCountByCategory(EventCategory.PETS)).thenReturn(10L);
//
//        ResponseEntity<Long> response = adminController.getEventCountByCategory(EventCategory.PETS);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(10L, response.getBody());
//    }
//
//    @Test
//    void testGetTotalUsers_success() {
//        when(adminService.getTotalUsers()).thenReturn(42L);
//
//        ResponseEntity<Long> response = adminController.getTotalUsers();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(42L, response.getBody());
//    }
//
//    @Test
//    void testGetTop5EventsByUpvotes_success() {
//        List<EventSummaryDto> mockList = List.of(new EventSummaryDto(1L, "Event 1", 100, EventCategory.PETS.name()));
//        when(adminService.getTop5EventsByUpvotes()).thenReturn(mockList);
//
//        ResponseEntity<List<EventSummaryDto>> response = adminController.getTop5EventsByUpvotes();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockList, response.getBody());
//    }
//
//    @Test
//    void testGetAdminPage_success() {
//        when(userService.currentUserEmail()).thenReturn("admin@example.com");
//
//        ResponseEntity<Object> response = adminController.getAdminPage();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("admin@example.com", response.getBody());
//    }
//
//    @Test
//    void testGetAdminPage_failure() {
//        when(userService.currentUserEmail()).thenThrow(new RuntimeException("DB Down"));
//
//        ResponseEntity<Object> response = adminController.getAdminPage();
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("An error occurred while accessing the admin page.", response.getBody());
//    }
}
