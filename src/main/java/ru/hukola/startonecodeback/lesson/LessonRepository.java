package ru.hukola.startonecodeback.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface LessonRepository extends PagingAndSortingRepository<Lesson, Integer>, JpaRepository<Lesson, Integer> {

}
