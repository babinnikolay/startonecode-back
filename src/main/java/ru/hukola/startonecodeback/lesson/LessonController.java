package ru.hukola.startonecodeback.lesson;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/lessons")
@AllArgsConstructor
public class LessonController {
    private final LessonDataService lessonDataService;

    @GetMapping
    public ResponseEntity<Collection<Lesson>> findAllLessons(@RequestParam(required = false, defaultValue = "0") int page,
                                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(lessonDataService.findAll(page, size));
    }
}
