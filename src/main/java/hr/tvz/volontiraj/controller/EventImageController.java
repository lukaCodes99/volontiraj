package hr.tvz.volontiraj.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event-image")
@AllArgsConstructor
@CrossOrigin("http://localhost:4200/")
public class EventImageController {
}
