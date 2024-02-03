package com.hqlinh.sachapi.file;

import com.hqlinh.sachapi.util.DTOUtil;
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
import java.nio.file.Path;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {
    private final IFileUploadRepository fileUploadRepository;

    @Value("${application.upload-dir}")
    private String UPLOAD_DIR;

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
            fileUpload.setUrl(UPLOAD_DIR + "/" + fileName);
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            fileUpload.setDimension(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));

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
            throw new FileUploadException.FileUploadServiceBusinessException("Exception occurred while persisting a new fileUpload!");
        }

        log.info("FileUploadService::create execution ended...");
        return fileUploadResponseDTO;
    }

//    public List<FileUploadDTO.ProductResponseDTO> getProducts() {
//        List<FileUploadDTO.ProductResponseDTO> productResponseDTOS;
//        try {
//            log.info("ProductService::getProducts execution started...");
//
//            List<FileUpload> productList = productRepository.findAll();
//            productResponseDTOS = productList.isEmpty() ? Collections.emptyList() : DTOUtil.mapList(productList, FileUploadDTO.ProductResponseDTO.class);
//        } catch (FileException.ProductServiceBusinessException ex) {
//            log.error("Exception occurred while retrieving products from database , Exception message {}", ex.getMessage());
//            throw new FileException.ProductServiceBusinessException("Exception occurred while fetch products from Database");
//        }
//
//        log.info("ProductService::getProductById execution ended...");
//        return productResponseDTOS;
//    }
//
//    public FileUploadDTO.ProductResponseDTO getProductById(Long productId) {
//        FileUploadDTO.ProductResponseDTO productResponseDTO;
//        try {
//            log.info("ProductService::getProductById execution started...");
//
//            FileUpload product = productRepository.findById(productId).orElseThrow(() -> new NoResultException("Product not found with id " + productId));
//            productResponseDTO = DTOUtil.map(product, FileUploadDTO.ProductResponseDTO.class);
//        } catch (FileException.ProductServiceBusinessException ex) {
//            log.error("Exception occurred while retrieving product {} from database , Exception message {}", productId, ex.getMessage());
//            throw new FileException.ProductServiceBusinessException("Exception occurred while fetch product from Database " + productId);
//        }
//
//        log.info("ProductService::getProductById execution ended...");
//        return productResponseDTO;
//    }
//
//    public FileUploadDTO.ProductResponseDTO updateProductById(Long productId, Map<String, Object> fields) {
//        FileUploadDTO.ProductResponseDTO productResponseDTO;
//        try {
//            log.info("ProductService::updateProductById execution started...");
//
//            //CHECK EXISTED
//            FileUpload existProduct = DTOUtil.map(getProductById(productId), FileUpload.class);
//
//            //EXECUTE
//            fields.forEach((key, value) -> {
//                Field field = ReflectionUtils.findField(FileUpload.class, key);
//                field.setAccessible(true);
//                ReflectionUtils.setField(field, existProduct, value);
//            });
//
//            FileUpload productResult = productRepository.save(DTOUtil.map(existProduct, FileUpload.class));
//            productResponseDTO = DTOUtil.map(productResult, FileUploadDTO.ProductResponseDTO.class);
//        } catch (FileException.ProductServiceBusinessException ex) {
//            log.error("Exception occurred while persisting product to database, Exception message {}", ex.getMessage());
//            throw new FileException.ProductServiceBusinessException("Exception occurred while create a new product!");
//        }
//
//        log.info("ProductService::updateProductById execution ended...");
//        return productResponseDTO;
//    }
//
//    //deleteProductById
//    public void deleteProductById(Long productId) {
//        try {
//            log.info("ProductService::deleteProductById execution started...");
//
//            //CHECK EXIST
//            FileUpload existProduct = DTOUtil.map(getProductById(productId), FileUpload.class);
//
//            //EXECUTE
//            productRepository.delete(existProduct);
//        } catch (FileException.ProductServiceBusinessException ex) {
//            log.error("Exception occurred while deleting product {} from database, Exception message {}", productId, ex.getMessage());
//            throw new FileException.ProductServiceBusinessException("Exception occurred while deleting product from Database " + productId);
//        }
//
//        log.info("ProductService::deleteProductById execution ended...");
//    }
}
