package com.jeffjackson.agreement.model;

import org.springframework.web.multipart.MultipartFile;

public class AgreementRequest {
    private String uniqueId;
    private String clientEmail;
    private MultipartFile file;

    public AgreementRequest() {
    }

    public AgreementRequest(String uniqueId, String clientEmail, MultipartFile file) {
        this.uniqueId = uniqueId;
        this.clientEmail = clientEmail;
        this.file = file;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
