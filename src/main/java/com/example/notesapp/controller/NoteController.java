
package com.example.notesapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.notesapp.model.Note;
import com.example.notesapp.repository.NoteRepository;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/notes")
@CrossOrigin
public class NoteController {

    @Autowired
    private NoteRepository repo;

    // Upload note
 @PostMapping("/upload")
public String upload(@RequestParam("file") MultipartFile file,
                     @RequestParam String title,
                     @RequestParam String subject) {

    try {
        System.out.println("API HIT");

        // ✅ Use absolute path (IMPORTANT FIX)
        String folder = System.getProperty("user.dir") + "/uploads/";

        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String path = folder + fileName;

        // ✅ Save file
        file.transferTo(new File(path));

        // ✅ Save to DB
        Note note = new Note();
        note.setTitle(title);
        note.setSubject(subject);
        note.setFilePath(path);

        repo.save(note);

        return "Uploaded Successfully";

    } catch (Exception e) {
        e.printStackTrace(); // 🔴 shows real error if any
        return "Error: " + e.getMessage();
    }
}

    // Get all notes
    @GetMapping
    public List<Note> getAll() {
        return repo.findAll();
    }

    // Search notes
    @GetMapping("/search")
    public List<Note> search(@RequestParam String keyword) {
        return repo.searchNotes(keyword);
    }

    // Download note
    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long id) throws Exception {

        Note note = repo.findById(id).orElseThrow();

        File file = new File(note.getFilePath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + file.getName())
                .body(resource);
    }
}