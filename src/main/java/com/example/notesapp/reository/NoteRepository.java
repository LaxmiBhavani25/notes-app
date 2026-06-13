package com.example.notesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.notesapp.model.Note;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserId(Long userId);

    @Query("SELECT n FROM Note n WHERE n.userId = :userId AND (n.title LIKE %:keyword% OR n.subject LIKE %:keyword%)")
    List<Note> searchNotesByUser(@Param("keyword") String keyword,
                                @Param("userId") Long userId);
}
