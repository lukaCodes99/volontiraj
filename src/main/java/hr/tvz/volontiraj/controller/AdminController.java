package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
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
