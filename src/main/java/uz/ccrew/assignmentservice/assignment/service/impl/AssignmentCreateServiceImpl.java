package uz.ccrew.assignmentservice.assignment.service.impl;

import uz.ccrew.assignmentservice.chat.entity.Chat;
import uz.ccrew.assignmentservice.exp.BasicException;
import uz.ccrew.assignmentservice.assignment.entity.*;
import uz.ccrew.assignmentservice.payment.PaymentType;
import uz.ccrew.assignmentservice.assignment.repository.*;
import uz.ccrew.assignmentservice.exp.BadRequestException;
import uz.ccrew.assignmentservice.assignment.ValidatorUtil;
import uz.ccrew.assignmentservice.assignment.enums.Category;
import uz.ccrew.assignmentservice.assignment.entity.Assignment;
import uz.ccrew.assignmentservice.assignment.enums.TransferType;
import uz.ccrew.assignmentservice.assignment.enums.AssignmentStatus;
import uz.ccrew.assignmentservice.assignment.dto.AssignmentCreateDTO;
import uz.ccrew.assignmentservice.assignment.enums.SwiftReceiverType;
import uz.ccrew.assignmentservice.assignment.service.AssignmentCreateService;
import uz.ccrew.assignmentservice.assignmentchat.service.AssignmentChatService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssignmentCreateServiceImpl implements AssignmentCreateService {
    private final AssignmentRepository assignmentRepository;
    private final AssignmentChatService assignmentChatService;
    private final AssignmentValidateService assignmentValidateService;
    private final TransferAssignmentRepository transferAssignmentRepository;
    private final RequisiteAssignmentRepository requisiteAssignmentRepository;
    private final CardRefreshAssignmentRepository cardRefreshAssignmentRepository;
    private final CertificateAssignmentRepository certificateAssignmentRepository;
    private final DisputeAssignmentPhotoRepository disputeAssignmentPhotoRepository;
    private final SwiftTransferAssignmentRepository swiftTransferAssignmentRepository;
    private final CertificateAssignmentCardRepository certificateAssignmentCardRepository;
    private final CertificateAssignmentAccountRepository certificateAssignmentAccountRepository;

    @Override
    public RequisiteAssignment create(AssignmentCreateDTO dto) {
        try {
            Category category = Category.fullFormOf(dto.category());
            if (category == null) {
                throw new BadRequestException("Invalid category");
            }

            PaymentType paymentType = getPaymentType(dto.cardNumberToPay(), dto.accountNumberToPay());
            Chat chat = assignmentChatService.create(String.format("%s %s", dto.category(), LocalDateTime.now()));

            Assignment assignment = Assignment.builder()
                    .fileId(UUID.fromString(dto.fileId()))
                    .category(category)
                    .status(AssignmentStatus.IN_REVIEW)
                    .details(dto.details())
                    .chatId(chat.getChatId())
                    .build();
            assignmentRepository.save(assignment);

            Long paymentAmount = 230000L;

            switch (category) {
                case SWIFT_PHYSICAL -> paymentAmount += createSwiftPhysicalAssignment(assignment, dto);
                case SWIFT_LEGAL -> paymentAmount += createSwiftLegalAssignment(assignment, dto);
                case INTERNATIONAL_TRANSFER -> paymentAmount += createTransferAssignment(assignment, dto);
                case CERTIFICATES -> createCertificatesAssignment(assignment, dto);
                case CARD_REFRESH -> createCardRefreshAssignment(assignment, dto);
                case DISPUTE -> createDisputeAssignment(assignment, dto);
            }

            RequisiteAssignment requisiteAssignment = RequisiteAssignment.builder()
                    .assignment(assignment)
                    .paymentAmount(paymentAmount)
                    .paymentType(paymentType)
                    .accountNumber(dto.accountNumberToPay())
                    .cardNumber(dto.cardNumberToPay())
                    .build();
            requisiteAssignmentRepository.save(requisiteAssignment);

            return requisiteAssignment;
        } catch (BasicException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Обязательные поля не заполнены");
        }
    }

    @Override
    public PaymentType getPaymentType(String cardNumber, String accountNumber) {
        PaymentType paymentType;
        if ((cardNumber == null || ValidatorUtil.isNotValidCardNumber(cardNumber))
                && (accountNumber == null || ValidatorUtil.isNotValidAccountNumber(accountNumber))) {
            throw new BadRequestException("Invalid cardNumber or accountNumber");
        } else {
            if (cardNumber != null && !ValidatorUtil.isNotValidCardNumber(cardNumber)) {
                paymentType = PaymentType.CARD;
            } else {
                paymentType = PaymentType.ACCOUNT;
            }
        }
        return paymentType;
    }

    private void createDisputeAssignment(Assignment assignment, AssignmentCreateDTO dto) {
        assignmentValidateService.disputeValidate(dto);

        List<DisputeAssignmentPhoto> disputeAssignmentPhotoList = new ArrayList<>();
        for (String photoId : dto.photoIds()) {
            DisputeAssignmentPhoto disputeAssignmentPhoto = DisputeAssignmentPhoto.builder()
                    .id(new DisputeAssignmentPhoto.DisputeAssignmentPhotoId(assignment.getAssignmentId(), UUID.fromString(photoId)))
                    .assignment(assignment)
                    .build();
            disputeAssignmentPhotoList.add(disputeAssignmentPhoto);
        }
        disputeAssignmentPhotoRepository.saveAll(disputeAssignmentPhotoList);
    }

    private void createCertificatesAssignment(Assignment assignment, AssignmentCreateDTO dto) {
        assignmentValidateService.certificatesValidate(dto);

        CertificateAssignment certificateAssignment = CertificateAssignment.builder()
                .assignment(assignment)
                .beginDate(dto.beginDate())
                .endDate(dto.endDate())
                .build();
        certificateAssignmentRepository.save(certificateAssignment);

        List<CertificateAssignmentCard> cards = new ArrayList<>();
        for (String cardNumber : dto.cardNumbers()) {
            CertificateAssignmentCard card = CertificateAssignmentCard.builder()
                    .id(new CertificateAssignmentCard.CertificateAssignmentCardId(assignment.getAssignmentId(), cardNumber))
                    .certificateAssignment(certificateAssignment)
                    .build();
            cards.add(card);
        }
        certificateAssignmentCardRepository.saveAll(cards);

        List<CertificateAssignmentAccount> accounts = new ArrayList<>();
        for (String accountNumber : dto.accountNumbers()) {
            CertificateAssignmentAccount account = CertificateAssignmentAccount.builder()
                    .id(new CertificateAssignmentAccount.CertificateAssignmentAccountId(assignment.getAssignmentId(), accountNumber))
                    .certificateAssignment(certificateAssignment)
                    .build();
            accounts.add(account);
        }
        certificateAssignmentAccountRepository.saveAll(accounts);
    }

    private void createCardRefreshAssignment(Assignment assignment, AssignmentCreateDTO dto) {
        assignmentValidateService.cardRefreshValidate(dto);

        CardRefreshAssignment cardRefreshAssignment = CardRefreshAssignment.builder()
                .assignment(assignment)
                .identityFileId(UUID.fromString(dto.identityFileId()))
                .build();
        cardRefreshAssignmentRepository.save(cardRefreshAssignment);
    }

    private Long createTransferAssignment(Assignment assignment, AssignmentCreateDTO dto) {
        assignmentValidateService.internationalTransferValidate(dto);

        TransferAssignment transferAssignment = TransferAssignment.builder()
                .assignment(assignment)
                .type(dto.transferType())
                .receiverCountry(dto.receiverCountry())
                .receiverFullName(dto.receiverFullName())
                .receiverPhoneNumber(dto.receiverPhoneNumber())
                .build();
        transferAssignmentRepository.save(transferAssignment);

        return transferAssignment.getAmount();
    }

    private Long createSwiftLegalAssignment(Assignment assignment, AssignmentCreateDTO dto) {
        assignmentValidateService.swiftLegalValidate(dto);

        TransferAssignment transferAssignment = TransferAssignment.builder()
                .assignment(assignment)
                .type(TransferType.SWIFT)
                .receiverCountry(dto.receiverCountry())
                .amount(dto.amount())
                .build();
        transferAssignmentRepository.save(transferAssignment);

        SwiftTransferAssignment swiftTransfer = SwiftTransferAssignment.builder()
                .transferAssignment(transferAssignment)
                .receiverType(SwiftReceiverType.LEGAL)
                .accountNumber(dto.accountNumber())
                .swiftCode(dto.swiftCode())
                .legalAddress(dto.legalAddress())
                .receiverOrganizationName(dto.receiverOrganizationName())
                .build();
        swiftTransferAssignmentRepository.save(swiftTransfer);

        return transferAssignment.getAmount();
    }

    private Long createSwiftPhysicalAssignment(Assignment assignment, AssignmentCreateDTO dto) {
        assignmentValidateService.swiftPhysicalValidate(dto);

        TransferAssignment transferAssignment = TransferAssignment.builder()
                .assignment(assignment)
                .type(TransferType.SWIFT)
                .receiverCountry(dto.receiverCountry())
                .amount(dto.amount())
                .receiverFullName(dto.receiverFullName())
                .build();
        transferAssignmentRepository.save(transferAssignment);

        SwiftTransferAssignment swiftTransfer = SwiftTransferAssignment.builder()
                .transferAssignment(transferAssignment)
                .receiverType(SwiftReceiverType.PHYSICAL)
                .accountNumber(dto.accountNumber())
                .swiftCode(dto.swiftCode())
                .build();
        swiftTransferAssignmentRepository.save(swiftTransfer);

        return transferAssignment.getAmount();
    }
}
