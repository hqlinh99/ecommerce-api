package com.hqlinh.ecom.file;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IFileUploadRepository extends JpaRepository<FileUpload, Long> {
    boolean existsByName(String name);
}
