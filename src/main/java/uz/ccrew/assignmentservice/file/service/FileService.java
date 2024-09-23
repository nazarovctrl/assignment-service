package uz.ccrew.assignmentservice.file.service;

import uz.ccrew.assignmentservice.file.FileDTO;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileDTO upload(MultipartFile multipartFile);
}