package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.model.EventCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTotalEvents() throws Exception {
        mockMvc.perform(get("/api/admin/total-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEventCountByCategory() throws Exception {
        mockMvc.perform(get("/api/admin/events-by-category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.PETS").isNumber())
                .andExpect(jsonPath("$.PEOPLE").isNumber())
                .andExpect(jsonPath("$.COMMUNITY").isNumber())
                .andExpect(jsonPath("$.OTHER").isNumber());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEventCountBySpecificCategory() throws Exception {
        mockMvc.perform(get("/api/admin/events-by-category/PETS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTotalUsers() throws Exception {
        mockMvc.perform(get("/api/admin/total-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTop5EventsByUpvotes() throws Exception {
        mockMvc.perform(get("/api/admin/top5-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAdminPage() throws Exception {
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("user"));
    }

    @Test
    void testAccessDeniedForUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/api/admin/total-events"))
                .andExpect(status().isForbidden());
    }
}
