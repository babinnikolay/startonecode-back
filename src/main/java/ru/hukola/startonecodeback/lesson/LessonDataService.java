package ru.hukola.startonecodeback.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class LessonDataService {
    private LessonRepository lessonRepository;

    public Collection<Lesson> findAll(int page, int size) {
        return lessonRepository.findAll();
    }
}
