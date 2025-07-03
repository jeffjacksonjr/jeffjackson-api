package com.jeffjackson.pdfUpload.service.controller;

import com.jeffjackson.model.MessageModel;
import com.jeffjackson.pdfUpload.service.FileStorageService;
import com.jeffjackson.pdfUpload.service.response.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/pdfUpload")
    public ResponseEntity<?> uploadPdfFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "misc") String folderName,
            @RequestParam("uniqueId") String uniqueId) {

        if(null == uniqueId || uniqueId.trim().isEmpty()){
            MessageModel messageModel = new MessageModel("Failed","Unique ID is required for file upload!");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
        try {
            FileUploadResponse response = fileStorageService.uploadPdfFile(file, folderName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File upload failed: " + e.getMessage());
        }
    }
}