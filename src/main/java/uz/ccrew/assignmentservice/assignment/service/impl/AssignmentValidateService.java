package uz.ccrew.assignmentservice.assignment.service.impl;

import uz.ccrew.assignmentservice.util.ValidatorUtil;
import uz.ccrew.assignmentservice.enums.TransferType;
import uz.ccrew.assignmentservice.file.FileRepository;
import uz.ccrew.assignmentservice.exp.BadRequestException;
import uz.ccrew.assignmentservice.dto.assignment.AssignmentCreateDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentValidateService {
    public final FileRepository fileRepository;

    public void disputeValidate(AssignmentCreateDTO dto) {
        if (!fileRepository.existsById(UUID.fromString(dto.fileId()))) {
            throw new BadRequestException("invalid fileId");
        }
        if (dto.photoIds() == null || dto.photoIds().isEmpty()) {
            throw new BadRequestException("Invalid photoIds");
        }
        for (String photoId : dto.photoIds()) {
            if (!fileRepository.existsById(UUID.fromString(photoId))) {
                throw new BadRequestException("Invalid photoId");
            }
        }
    }

    public void cardRefreshValidate(AssignmentCreateDTO dto) {
        if (!fileRepository.existsById(UUID.fromString(dto.fileId()))) {
            throw new BadRequestException("invalid fileId");
        }
        if (dto.identityFileId() == null || dto.identityFileId().isBlank() || !fileRepository.existsById(UUID.fromString(dto.identityFileId()))) {
            throw new BadRequestException("Invalid identityFileId");
        }
    }

    public void certificatesValidate(AssignmentCreateDTO dto) {
        if ((dto.accountNumbers() == null || dto.accountNumbers().isEmpty())
                && (dto.cardNumbers() == null || dto.cardNumbers().isEmpty())) {
            throw new BadRequestException("Invalid cardNumbers or accountNumbers");
        }

        if (dto.accountNumbers() != null) {
            for (String accountNumber : dto.accountNumbers()) {
                if (ValidatorUtil.isNotValidAccountNumber(accountNumber)) {
                    throw new BadRequestException("Invalid accountNumber");
                }
            }
        } else {
            for (String cardNumber : dto.cardNumbers()) {
                if (ValidatorUtil.isNotValidCardNumber(cardNumber)) {
                    throw new BadRequestException("Invalid cardNumber");
                }
            }
        }

        if (dto.beginDate() == null || dto.endDate() == null || dto.beginDate().isAfter(dto.endDate())) {
            throw new BadRequestException("Invalid beginDate/endDate");
        }
    }

    public void internationalTransferValidate(AssignmentCreateDTO dto) {
        if (dto.transferType() == null || dto.transferType().equals(TransferType.SWIFT)) {
            throw new BadRequestException("Invalid transferType");
        }
        if (dto.receiverCountry() == null || dto.receiverCountry().isBlank()) {
            throw new BadRequestException("Invalid receiverCountry");
        }
        if (dto.receiverFullName() == null || dto.receiverFullName().isBlank()) {
            throw new BadRequestException("Invalid receiverFullName");
        }
        if (dto.receiverPhoneNumber() == null || dto.receiverPhoneNumber().isBlank() || ValidatorUtil.isNotValidPhoneNumber(dto.receiverPhoneNumber())) {
            throw new BadRequestException("Invalid receiverPhoneNumber");
        }
        if (!fileRepository.existsById(UUID.fromString(dto.fileId()))) {
            throw new BadRequestException("invalid fileId");
        }
    }

    public void swiftLegalValidate(AssignmentCreateDTO dto) {
        validateSwift(dto);
        if (dto.legalAddress() == null || dto.legalAddress().isBlank()) {
            throw new BadRequestException("Invalid legalAddress");
        }
        if (dto.receiverOrganizationName() == null || dto.receiverOrganizationName().isBlank()) {
            throw new BadRequestException("Invalid receiverOrganizationName");
        }
    }

    public void swiftPhysicalValidate(AssignmentCreateDTO dto) {
        validateSwift(dto);
        if (dto.receiverFullName() == null || dto.receiverFullName().isBlank()) {
            throw new BadRequestException("Invalid receiverFullName");
        }
    }

    public void validateSwift(AssignmentCreateDTO dto) {
        if (dto.receiverCountry() == null || dto.receiverCountry().isBlank()) {
            throw new BadRequestException("Invalid receiverCountry");
        }
        if (dto.accountNumber() == null || dto.accountNumber().isBlank() || ValidatorUtil.isNotValidAccountNumber(dto.accountNumber())) {
            throw new BadRequestException("Invalid accountNumber");
        }
        if (dto.swiftCode() == null || dto.swiftCode().isBlank() || ValidatorUtil.isNotValidSwiftCode(dto.swiftCode())) {
            throw new BadRequestException("Invalid swiftCode");
        }
        if (dto.amount() == null || dto.amount() <= 0) {
            throw new BadRequestException("Invalid amount");
        }
        if (!fileRepository.existsById(UUID.fromString(dto.fileId()))) {
            throw new BadRequestException("invalid fileId");
        }
    }
}
