package uz.ccrew.assignmentservice.assignment;

import uz.ccrew.assignmentservice.entity.Assignment;
import uz.ccrew.assignmentservice.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.notifcation.NotificationService;
import uz.ccrew.assignmentservice.repository.AssignmentRepository;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;
import java.time.LocalDate;

@Slf4j
@EnableAsync
@Service
@RequiredArgsConstructor
public class JobService {
    private final NotificationService notificationService;
    private final AssignmentRepository assignmentRepository;

    @Scheduled(cron = "${job.cron}", zone = "${job.zone}")
    public void cancelAssignments() {
        String note = "Дорогой заявщик, Капиталбанк вынужден Вам отказать в поручении по причине несвоевременной предоставленой информации";

        List<Assignment> assignments = assignmentRepository.findAllByStatusAndModifiedOnLessThan(AssignmentStatus.CANCELLED, LocalDate.now().minusDays(10).atStartOfDay());
        assignments.forEach(a -> {
            a.setStatus(AssignmentStatus.CANCELLED);
            a.setNote(note);
            notificationService.sendNotification(a.getCreatedBy().getLogin(), a.getNote());
        });

        assignmentRepository.saveAll(assignments);
    }
}