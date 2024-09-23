package uz.ccrew.assignmentservice.file.service.impl;

import uz.ccrew.assignmentservice.file.File;
import uz.ccrew.assignmentservice.file.FileDTO;
import uz.ccrew.assignmentservice.file.FileMapper;
import uz.ccrew.assignmentservice.file.FileRepository;
import uz.ccrew.assignmentservice.file.service.FileService;
import uz.ccrew.assignmentservice.file.service.StorageService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileMapper fileMapper;
    private final StorageService storageService;
    private final FileRepository fileRepository;

    @Value("${aws.s3.url}")
    private String s3Url;

    @Override
    public FileDTO upload(MultipartFile multipartFile) {
        UUID id = UUID.randomUUID();
        String url = s3Url + id;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            storageService.upload(String.valueOf(id), multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File file = new File(id, url);
        fileRepository.save(file);

        return fileMapper.toDTO(file);

    }
}