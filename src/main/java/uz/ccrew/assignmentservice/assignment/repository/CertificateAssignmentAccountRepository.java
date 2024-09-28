package uz.ccrew.assignmentservice.assignment.repository;

import uz.ccrew.assignmentservice.base.BasicRepository;
import uz.ccrew.assignmentservice.assignment.entity.CertificateAssignmentAccount;

import java.util.List;

public interface CertificateAssignmentAccountRepository extends BasicRepository<CertificateAssignmentAccount, CertificateAssignmentAccount.CertificateAssignmentAccountId> {

    List<CertificateAssignmentAccount> findAllByCertificateAssignment_AssignmentId(Long assignmentId);
}
