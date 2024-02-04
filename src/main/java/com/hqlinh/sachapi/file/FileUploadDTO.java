package com.hqlinh.sachapi.file;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FileUploadResponseDTO {
        private long id;
        private String name;
        private String contentType;
        private long size;
        private String url;
        private Dimension dimension;
    }
}

