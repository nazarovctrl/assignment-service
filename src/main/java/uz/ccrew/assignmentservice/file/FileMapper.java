package uz.ccrew.assignmentservice.file;

import uz.ccrew.assignmentservice.base.Mapper;

import org.springframework.stereotype.Component;

@Component
public class FileMapper implements Mapper<Object, FileDTO, File> {

    @Override
    public File toEntity(Object o) {
        return null;
    }

    @Override
    public FileDTO toDTO(File file) {
        return FileDTO.builder()
                .fileId(file.getFileId().toString())
                .url(file.getUrl())
                .build();
    }
}