package uz.ccrew.assignmentservice.file;

import lombok.Builder;

@Builder
public record FileDTO(String fileId,
                      String url) {
}