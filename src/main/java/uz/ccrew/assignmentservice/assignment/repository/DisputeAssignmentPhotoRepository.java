package uz.ccrew.assignmentservice.assignment.repository;

import uz.ccrew.assignmentservice.base.BasicRepository;
import uz.ccrew.assignmentservice.assignment.entity.DisputeAssignmentPhoto;

import java.util.List;

public interface DisputeAssignmentPhotoRepository extends BasicRepository<DisputeAssignmentPhoto, DisputeAssignmentPhoto.DisputeAssignmentPhotoId> {
    List<DisputeAssignmentPhoto> findAllByAssignment_AssignmentId(Long assignmentId);
}
