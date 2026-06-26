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

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam String title,
                         @RequestParam String subject,
                         @RequestParam Long userId) {

        try {
            String folder = System.getProperty("user.dir") + "/uploads/";

            File dir = new File(folder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String path = folder + fileName;

            file.transferTo(new File(path));

            Note note = new Note();
            note.setTitle(title);
            note.setSubject(subject);
            note.setFilePath(path);
            note.setUserId(userId); 

            repo.save(note);

            return "Uploaded Successfully";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping
    public List<Note> getAll(@RequestParam Long userId) {
        return repo.findByUserId(userId); 
    }

    @GetMapping("/search")
    public List<Note> search(@RequestParam String keyword,
                             @RequestParam Long userId) {
        return repo.searchNotesByUser(keyword, userId);
    }
    @GetMapping("/view/{id}")
    public ResponseEntity<InputStreamResource> view(@PathVariable Long id) throws Exception {

        Note note = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Note not found"));

        File file = new File(note.getFilePath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        String contentType = java.nio.file.Files.probeContentType(file.toPath());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=" + file.getName())
            .header(HttpHeaders.CONTENT_TYPE,
                    contentType != null ? contentType : "application/octet-stream")
            .body(resource);
}
    

}
