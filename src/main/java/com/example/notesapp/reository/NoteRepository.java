
package com.example.notesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.notesapp.model.Note;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.title LIKE %:keyword% OR n.subject LIKE %:keyword%")
    List<Note> searchNotes(@Param("keyword") String keyword);
}