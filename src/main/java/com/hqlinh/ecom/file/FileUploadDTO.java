package com.hqlinh.ecom.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.awt.*;
import java.util.Map;

public abstract class FileUploadDTO {
    @NotBlank(message = "file name shouldn't be NULL OR EMPTY")
    private String name;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NameFileUploadRequest {
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FileUploadResponseDTO {
        private Long id;
        private String name;
        private String extension;
        private String contentType;
        private Long size;
        private String url;
        private Dimension dimension;
    }
}

