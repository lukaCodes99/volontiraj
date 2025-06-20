package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.service.AdminService;
import hr.tvz.volontiraj.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;
    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTotalEvents() throws Exception {
        when(adminService.getTotalEvents()).thenReturn(42L);
        mockMvc.perform(get("/api/admin/total-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(42L));

        verify(adminService).getTotalEvents();
        verifyNoMoreInteractions(adminService);
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

        verify(adminService).getEventCountByCategory();
        verifyNoMoreInteractions(adminService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEventCountBySpecificCategory() throws Exception {
        when(adminService.getEventCountByCategory(EventCategory.PETS)).thenReturn(10L);

        mockMvc.perform(get("/api/admin/events-by-category/PETS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10L));

        verify(adminService).getEventCountByCategory(EventCategory.PETS);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTotalUsers() throws Exception {
        when(adminService.getTotalUsers()).thenReturn(100L);

        mockMvc.perform(get("/api/admin/total-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100L));

        verify(adminService).getTotalUsers();
        verifyNoMoreInteractions(adminService);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTop5EventsByUpvotes() throws Exception {
        when(adminService.getTop5EventsByUpvotes()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/admin/top5-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(new ArrayList<>()));

        verify(adminService).getTop5EventsByUpvotes();
        verifyNoMoreInteractions(adminService);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAdminPage() throws Exception {
        when(userService.currentUserEmail()).thenReturn("admin@example.com");

        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("admin@example.com"));

        verify(userService).currentUserEmail();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAdminPage_failure() throws Exception {
        when(userService.currentUserEmail()).thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while accessing the admin page."));

        verify(userService).currentUserEmail();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testAccessDeniedForUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/api/admin/total-events"))
                .andExpect(status().isForbidden());
    }
}
