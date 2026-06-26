package com.example.notesapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.notesapp.model.Note;
import com.example.notesapp.repository.NoteRepository;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notes")
@CrossOrigin
public class NoteController {

    @Autowired
    private NoteRepository repo;

    @Autowired
    private Cloudinary cloudinary;

    //  Upload file to Cloudinary
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam String title,
                         @RequestParam String subject,
                         @RequestParam Long userId) {

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                   ObjectUtils.asMap(
        "resource_type", "auto",
        "type", "upload"
    )
            );

            String fileUrl = uploadResult.get("secure_url").toString();

            Note note = new Note();
            note.setTitle(title);
            note.setSubject(subject);
            note.setFilePath(fileUrl); //  store cloud URL
            note.setUserId(userId);

            repo.save(note);

            return "Uploaded Successfully";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    //  Get all notes
    @GetMapping
    public List<Note> getAll(@RequestParam Long userId) {
        return repo.findByUserId(userId);
    }

    //  Search notes
    @GetMapping("/search")
    public List<Note> search(@RequestParam String keyword,
                             @RequestParam Long userId) {
        return repo.searchNotesByUser(keyword, userId);
    }

    // View file 
    @GetMapping("/view/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {

        Note note = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        return ResponseEntity
                .status(302)
                .header("Location", note.getFilePath())
                .build();
    }
    @GetMapping("/deleteAll")
public String deleteAll() {
    repo.deleteAll();
    return "Deleted";
}
}
