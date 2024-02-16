package com.hqlinh.ecom.order_item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {
    private final IOrderItemRepository orderItemRepository;

//    public OrderItemDTO.FileUploadResponseDTO uploadSingleFile(MultipartFile multipartFile) throws IOException {
//        OrderItemDTO.FileUploadResponseDTO fileUploadResponseDTO;
//        try {
//            log.info("FileUploadService::upload single file execution started...");
//
//            //CHECK FILE NAME
//            String originalFileName = multipartFile.getOriginalFilename();
//            String fileName = createFileName(originalFileName.substring(0, originalFileName.lastIndexOf('.')));
//            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
//            String fileNameWithExtension = fileName + extension;
//
//            //SET FILE_UPLOAD
//            OrderItem fileUpload = new OrderItem();
//            fileUpload.setName(fileName);
//            fileUpload.setExtension(extension);
//            fileUpload.setContentType(multipartFile.getContentType());
//            fileUpload.setSize(multipartFile.getSize());
//            fileUpload.setUrl(
//                    ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
//                            + PATH.replace("**", "") + fileNameWithExtension);
//            if (multipartFile.getContentType().startsWith("image/")) {
//                BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
//                fileUpload.setDimension(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
//            }
//
//            //CHECK DIRECTORY EXISTED
//            File directory = new File(System.getProperty("user.dir") + UPLOAD_DIR);
//            if (!directory.exists())
//                directory.mkdirs();
//
//            //SAVE FILE TO DIRECTORY
//            multipartFile.transferTo(new File(directory, fileNameWithExtension));
//
//            //SAVE FILE_UPLOAD TO DATABASE
//            OrderItem fileResult = fileUploadRepository.save(fileUpload);
//            fileUploadResponseDTO = DTOUtil.map(fileResult, OrderItemDTO.FileUploadResponseDTO.class);
//        } catch (OrderItemException.FileUploadServiceBusinessException ex) {
//            log.error("Exception occurred while persisting a new fileUpload to database , Exception message {}", ex.getMessage());
//            throw ex;
//        }
//
//        log.info("FileUploadService::create execution ended...");
//        return fileUploadResponseDTO;
//    }
//
//    public List<OrderItemDTO.FileUploadResponseDTO> getFileUploads() {
//        List<OrderItemDTO.FileUploadResponseDTO> fileUploadResponseDTOS;
//        try {
//            log.info("FileUploadService::getFileUploads execution started...");
//
//            List<OrderItem> fileUploadList = fileUploadRepository.findAll();
//            fileUploadResponseDTOS = fileUploadList.isEmpty() ? Collections.emptyList() : DTOUtil.mapList(fileUploadList, OrderItemDTO.FileUploadResponseDTO.class);
//        } catch (OrderItemException.FileUploadServiceBusinessException ex) {
//            log.error("Exception occurred while retrieving fileUploads from database , Exception message {}", ex.getMessage());
//            throw ex;
//        }
//
//        log.info("FileUploadService::getFileUploadById execution ended...");
//        return fileUploadResponseDTOS;
//    }
//
//    public OrderItemDTO.FileUploadResponseDTO getFileUploadById(Long fileUploadId) {
//        OrderItemDTO.FileUploadResponseDTO fileUploadResponseDTO;
//        try {
//            log.info("FileUploadService::getFileUploadById execution started...");
//
//            OrderItem fileUpload = fileUploadRepository.findById(fileUploadId).orElseThrow(() -> new NoResultException("FileUpload not found with id " + fileUploadId));
//            fileUploadResponseDTO = DTOUtil.map(fileUpload, OrderItemDTO.FileUploadResponseDTO.class);
//        } catch (OrderItemException.FileUploadServiceBusinessException ex) {
//            log.error("Exception occurred while retrieving fileUpload {} from database , Exception message {}", fileUploadId, ex.getMessage());
//            throw ex;
//        }
//
//        log.info("FileUploadService::getFileUploadById execution ended...");
//        return fileUploadResponseDTO;
//    }
//
//    //
//    public OrderItemDTO.FileUploadResponseDTO updateNameFileUploadById(Long fileUploadId, OrderItemDTO.NameFileUploadRequest fileName) {
//        OrderItemDTO.FileUploadResponseDTO fileUploadResponseDTO;
//        try {
//            log.info("FileUploadService::updateNameFileUploadById execution started...");
//
//            //CHECK EXISTED
//            OrderItem existFileUpload = DTOUtil.map(getFileUploadById(fileUploadId), OrderItem.class);
//
//            //EXECUTE
//            if (existFileUpload.getName().equals(fileName.getName()))
//                fileUploadResponseDTO = DTOUtil.map(existFileUpload, OrderItemDTO.FileUploadResponseDTO.class);
//            else {
//                //get old file name
//                String oldFileNameWithExtension = existFileUpload.getName() + existFileUpload.getExtension();
//
//                //check existed file name
//                String newFileName = createFileName(fileName.getName());
//                String newFileNameWithExtension = newFileName + existFileUpload.getExtension();
//
//                //update new file name in database
//                existFileUpload.setName(newFileName);
//                existFileUpload.setUrl(
//                        ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
//                                + PATH.replace("**", "") + newFileNameWithExtension);
//                OrderItem fileUploadResult = fileUploadRepository.save(existFileUpload);
//
//                //update file name in directory
//                File oldFile = new File(System.getProperty("user.dir") + UPLOAD_DIR + "/" + oldFileNameWithExtension);
//                File newFile = new File(System.getProperty("user.dir") + UPLOAD_DIR + "/" + newFileNameWithExtension);
//                if (!oldFile.renameTo(newFile))
//                    log.error("Update file name failed");
//
//                fileUploadResponseDTO = DTOUtil.map(fileUploadResult, OrderItemDTO.FileUploadResponseDTO.class);
//            }
//        } catch (OrderItemException.FileUploadServiceBusinessException ex) {
//            log.error("Exception occurred while persisting fileUpload to database, Exception message {}", ex.getMessage());
//            throw ex;
//        }
//
//        log.info("FileUploadService::updateNameFileUploadById execution ended...");
//        return fileUploadResponseDTO;
//    }
//
//    public void deleteFileUploadById(Long fileUploadId) {
//        try {
//            log.info("FileUploadService::deleteFileUploadById execution started...");
//
//            //CHECK EXIST
//            OrderItem existFileUpload = DTOUtil.map(getFileUploadById(fileUploadId), OrderItem.class);
//
//            //EXECUTE
//            fileUploadRepository.delete(existFileUpload);
//            String fileNameWithExtension = existFileUpload.getName() + existFileUpload.getExtension();
//            File file = new File(System.getProperty("user.dir") + UPLOAD_DIR + "/" + fileNameWithExtension);
//            file.delete();
//        } catch (OrderItemException.FileUploadServiceBusinessException ex) {
//            log.error("Exception occurred while deleting fileUpload {} from database, Exception message {}", fileUploadId, ex.getMessage());
//            throw ex;
//        }
//
//        log.info("FileUploadService::deleteFileUploadById execution ended...");
//    }
//
//    private String createFileName(String fileName) {
//        int i = 1;
//        StringBuilder newFileName = new StringBuilder(fileName);
//        while (fileUploadRepository.existsByName(newFileName.toString())) {
//            newFileName.setLength(0);
//            newFileName.append(fileName).append("-").append(i++);
//        }
//        return newFileName.toString();
//    }
}
