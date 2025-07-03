package com.jeffjackson.pdfUpload.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.jeffjackson.pdfUpload.service.response.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
public class FileStorageService {

    private final Cloudinary cloudinary;

    @Autowired
    public FileStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public FileUploadResponse uploadPdfFile(MultipartFile file, String folderName) throws IOException {
        // Create folder path (Cloudinary requires no leading/trailing slashes)
        String folderPath = normalizeFolderName(folderName);

        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "raw",
                        "type", "upload",
                        "folder", folderPath,
                        "filename_override", file.getOriginalFilename(),
                        "use_filename", true,
                        "unique_filename", false,
                        "format", "pdf",
                        "invalidate", true, // Refresh CDN cache
                        "access_mode", "public",  // Ensure public access
                        "transformation", new Transformation()
                                .rawTransformation("f_pdf")
                ));

        return createUploadResponse(file, uploadResult);
    }

    private String normalizeFolderName(String folderName) {
        if (folderName == null || folderName.trim().isEmpty()) {
            return "misc"; // default folder
        }
        // Remove leading/trailing slashes and spaces
        return folderName.trim().replaceAll("^/+|/+$", "");
    }

    private FileUploadResponse createUploadResponse(MultipartFile file, Map<?, ?> uploadResult) {
        FileUploadResponse response = new FileUploadResponse();
        response.setFileUrl(uploadResult.get("secure_url").toString());
        response.setFileName(file.getOriginalFilename());
        response.setFileType(file.getContentType());
        response.setFileSize(file.getSize());
        response.setUploadTime(new Date());
        response.setPublicId(uploadResult.get("public_id").toString());
        return response;
    }
}