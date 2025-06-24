package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.dto.EventSummaryDto;
import hr.tvz.volontiraj.model.EventCategory;
import hr.tvz.volontiraj.service.AdminService;
import hr.tvz.volontiraj.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;

    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/total-events")
    public ResponseEntity<Long> getTotalEvents() {
        return ResponseEntity.ok(adminService.getTotalEvents());
    }

    @GetMapping("/events-by-category")
    public ResponseEntity<Map<EventCategory, Long>> getEventCountByCategory() {
        return ResponseEntity.ok(adminService.getEventCountByCategory());
    }

    @GetMapping("/events-by-category/{category}")
    public ResponseEntity<Long> getEventCountByCategory(@PathVariable EventCategory category) {
        return ResponseEntity.ok(adminService.getEventCountByCategory(category));
    }

    @GetMapping("/total-users")
    public ResponseEntity<Long> getTotalUsers() {
        return ResponseEntity.ok(adminService.getTotalUsers());
    }

    @GetMapping("/top5-events")
    public ResponseEntity<List<EventSummaryDto>> getTop5EventsByUpvotes() {
        return ResponseEntity.ok(adminService.getTop5EventsByUpvotes());
    }

    @GetMapping()
    public ResponseEntity<Object> getAdminPage() {
        try {
            // Assuming you want to return some admin-specific data
            return ResponseEntity.ok(userService.currentUserEmail());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while accessing the admin page.");
        }
    }

}
