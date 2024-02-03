package com.hqlinh.sachapi.file;

public class FileUploadException {
    public static class FileUploadServiceBusinessException extends RuntimeException {
        public FileUploadServiceBusinessException(String message) {
            super(message);
        }
    }
}
