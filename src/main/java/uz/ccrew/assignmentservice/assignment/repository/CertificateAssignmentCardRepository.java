package uz.ccrew.assignmentservice.assignment.repository;

import uz.ccrew.assignmentservice.base.BasicRepository;
import uz.ccrew.assignmentservice.assignment.entity.CertificateAssignmentCard;

import java.util.List;

public interface CertificateAssignmentCardRepository extends BasicRepository<CertificateAssignmentCard, CertificateAssignmentCard.CertificateAssignmentCardId> {
    List<CertificateAssignmentCard> findAllByCertificateAssignment_AssignmentId(Long assignmentId);
}
