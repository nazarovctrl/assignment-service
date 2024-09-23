package uz.ccrew.assignmentservice.file;

import uz.ccrew.assignmentservice.dto.Response;
import uz.ccrew.assignmentservice.dto.ResponseMaker;
import uz.ccrew.assignmentservice.file.service.FileService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "File controller")
public class FileController {
    private final FileService fileService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('CUSTOMER','EMPLOYEE','MANAGER')")
    @Operation(summary = "File upload")
    public ResponseEntity<Response<FileDTO>> upload(@RequestParam("file") MultipartFile file) {
        FileDTO result = fileService.upload(file);
        return ResponseMaker.ok(result);
    }
}