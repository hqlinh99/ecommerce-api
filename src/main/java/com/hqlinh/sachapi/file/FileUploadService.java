package com.hqlinh.sachapi.file;

import com.hqlinh.sachapi.util.DTOUtil;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {
    private final IFileUploadRepository fileUploadRepository;

    @Value("${application.upload-dir}")
    private String UPLOAD_DIR;
    @Value("${spring.mvc.static-path-pattern}")
    private String PATH;

    public FileUploadDTO.FileUploadResponseDTO uploadSingleFile(MultipartFile multipartFile) throws IOException {
        FileUploadDTO.FileUploadResponseDTO fileUploadResponseDTO;
        try {
            log.info("FileUploadService::upload single file execution started...");

            //CHECK FILE NAME
            String originalFileName = multipartFile.getOriginalFilename();
            String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));

            int i = 1;
            StringBuilder newFileName = new StringBuilder(fileName);
            while (fileUploadRepository.existsByName(newFileName.toString() + extension)) {
                newFileName.setLength(0);
                newFileName.append(fileName).append("-").append(i++);
            }
            newFileName.append(extension);
            fileName = newFileName.toString();

            //SET FILE_UPLOAD
            FileUpload fileUpload = new FileUpload();
            fileUpload.setName(fileName);
            fileUpload.setContentType(multipartFile.getContentType());
            fileUpload.setSize(multipartFile.getSize());
            fileUpload.setUrl(PATH.replace("**", "") + fileName);
            if (multipartFile.getContentType().startsWith("image/")) {
                BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
                fileUpload.setDimension(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
            }

            //CHECK DIRECTORY EXISTED
            File directory = new File(System.getProperty("user.dir") + UPLOAD_DIR);
            if (!directory.exists())
                directory.mkdirs();

            //SAVE FILE TO DIRECTORY
            multipartFile.transferTo(new File(directory, fileName));

            //SAVE FILE_UPLOAD TO DATABASE
            FileUpload fileResult = fileUploadRepository.save(fileUpload);
            fileUploadResponseDTO = DTOUtil.map(fileResult, FileUploadDTO.FileUploadResponseDTO.class);
        } catch (FileUploadException.FileUploadServiceBusinessException ex) {
            log.error("Exception occurred while persisting a new fileUpload to database , Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("FileUploadService::create execution ended...");
        return fileUploadResponseDTO;
    }

    public List<FileUploadDTO.FileUploadResponseDTO> getFileUploads() {
        List<FileUploadDTO.FileUploadResponseDTO> fileUploadResponseDTOS;
        try {
            log.info("FileUploadService::getFileUploads execution started...");

            List<FileUpload> fileUploadList = fileUploadRepository.findAll();
            fileUploadResponseDTOS = fileUploadList.isEmpty() ? Collections.emptyList() : DTOUtil.mapList(fileUploadList, FileUploadDTO.FileUploadResponseDTO.class);
        } catch (FileUploadException.FileUploadServiceBusinessException ex) {
            log.error("Exception occurred while retrieving fileUploads from database , Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("FileUploadService::getFileUploadById execution ended...");
        return fileUploadResponseDTOS;
    }

    public FileUploadDTO.FileUploadResponseDTO getFileUploadById(Long fileUploadId) {
        FileUploadDTO.FileUploadResponseDTO fileUploadResponseDTO;
        try {
            log.info("FileUploadService::getFileUploadById execution started...");

            FileUpload fileUpload = fileUploadRepository.findById(fileUploadId).orElseThrow(() -> new NoResultException("FileUpload not found with id " + fileUploadId));
            fileUploadResponseDTO = DTOUtil.map(fileUpload, FileUploadDTO.FileUploadResponseDTO.class);
        } catch (FileUploadException.FileUploadServiceBusinessException ex) {
            log.error("Exception occurred while retrieving fileUpload {} from database , Exception message {}", fileUploadId, ex.getMessage());
            throw ex;
        }

        log.info("FileUploadService::getFileUploadById execution ended...");
        return fileUploadResponseDTO;
    }
//
//    public FileUploadDTO.FileUploadResponseDTO updateFileUploadById(Long fileUploadId, Map<String, Object> fields) {
//        FileUploadDTO.FileUploadResponseDTO fileUploadResponseDTO;
//        try {
//            log.info("FileUploadService::updateFileUploadById execution started...");
//
//            //CHECK EXISTED
//            FileUpload existFileUpload = DTOUtil.map(getFileUploadById(fileUploadId), FileUpload.class);
//
//            //EXECUTE
//            fields.forEach((key, value) -> {
//                Field field = ReflectionUtils.findField(FileUpload.class, key);
//                field.setAccessible(true);
//                ReflectionUtils.setField(field, existFileUpload, value);
//            });
//
//            FileUpload fileUploadResult = fileUploadRepository.save(DTOUtil.map(existFileUpload, FileUpload.class));
//            fileUploadResponseDTO = DTOUtil.map(fileUploadResult, FileUploadDTO.FileUploadResponseDTO.class);
//        } catch (FileException.FileUploadServiceBusinessException ex) {
//            log.error("Exception occurred while persisting fileUpload to database, Exception message {}", ex.getMessage());
//            throw ex;
//        }
//
//        log.info("FileUploadService::updateFileUploadById execution ended...");
//        return fileUploadResponseDTO;
//    }

    public void deleteFileUploadById(Long fileUploadId) {
        try {
            log.info("FileUploadService::deleteFileUploadById execution started...");

            //CHECK EXIST
            FileUpload existFileUpload = DTOUtil.map(getFileUploadById(fileUploadId), FileUpload.class);

            //EXECUTE
            fileUploadRepository.delete(existFileUpload);
            File file = new File(System.getProperty("user.dir") + UPLOAD_DIR + "/" + existFileUpload.getName());
            file.delete();
        } catch (FileUploadException.FileUploadServiceBusinessException ex) {
            log.error("Exception occurred while deleting fileUpload {} from database, Exception message {}", fileUploadId, ex.getMessage());
            throw ex;
        }

        log.info("FileUploadService::deleteFileUploadById execution ended...");
    }
}
