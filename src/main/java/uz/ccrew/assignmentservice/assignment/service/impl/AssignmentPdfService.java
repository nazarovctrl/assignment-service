package uz.ccrew.assignmentservice.assignment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import uz.ccrew.assignmentservice.assignment.entity.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.stereotype.Service;
import uz.ccrew.assignmentservice.assignment.enums.Category;
import uz.ccrew.assignmentservice.assignment.repository.*;
import uz.ccrew.assignmentservice.file.FileDTO;
import uz.ccrew.assignmentservice.file.service.FileService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AssignmentPdfService {
    private final TransferAssignmentRepository transferAssignmentRepository;
    private final SwiftTransferAssignmentRepository swiftTransferAssignmentRepository;
    private final CertificateAssignmentRepository certificateAssignmentRepository;
    private final CertificateAssignmentCardRepository certificateAssignmentCardRepository;
    private final CertificateAssignmentAccountRepository certificateAssignmentAccountRepository;
    private final FileService fileService;

    public FileDTO generatePdf(Assignment assignment) {
        // Create a new document
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {


            // Add a page to the document
            PDPage page = new PDPage();
            document.addPage(page);

            // Start a content stream which will be used to write text to the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(25, 750);

                // Write Assignment information
                contentStream.showText("Assignment ID: " + assignment.getAssignmentId());
                contentStream.newLine();
                contentStream.showText("Category: " + assignment.getCategory());
                contentStream.newLine();
                contentStream.showText("Details: " + assignment.getDetails());
                contentStream.newLine();


                if (assignment.getCategory().equals(Category.SWIFT_PHYSICAL)) {
                    TransferAssignment transferAssignment = transferAssignmentRepository.loadById(assignment.getAssignmentId(), "Transfer assignment not found");
                    SwiftTransferAssignment swiftTransferAssignment = swiftTransferAssignmentRepository.loadById(transferAssignment.getAssignmentId(), "not found");
                    contentStream.showText("Receiver Country: " + transferAssignment.getReceiverCountry());
                    contentStream.newLine();
                    contentStream.showText("Account number: " + swiftTransferAssignment.getAccountNumber());
                    contentStream.newLine();
                    contentStream.showText("Swift code: " + swiftTransferAssignment.getSwiftCode());
                    contentStream.newLine();
                    contentStream.showText("transfer amount: " + transferAssignment.getAmount());
                    contentStream.newLine();
                    contentStream.showText("Receiver full name: " + transferAssignment.getReceiverFullName());
                    contentStream.newLine();
                }

                if (assignment.getCategory().equals(Category.SWIFT_LEGAL)) {
                    TransferAssignment transferAssignment = transferAssignmentRepository.loadById(assignment.getAssignmentId(), "Transfer assignment not found");
                    SwiftTransferAssignment swiftTransferAssignment = swiftTransferAssignmentRepository.loadById(transferAssignment.getAssignmentId(), "not found");
                    contentStream.showText("Receiver Country: " + transferAssignment.getReceiverCountry());
                    contentStream.newLine();
                    contentStream.showText("Account number: " + swiftTransferAssignment.getAccountNumber());
                    contentStream.newLine();
                    contentStream.showText("Swift code: " + swiftTransferAssignment.getSwiftCode());
                    contentStream.newLine();
                    contentStream.showText("LegalAddress: " + swiftTransferAssignment.getLegalAddress());
                    contentStream.newLine();
                    contentStream.showText("Receiver organization name: " + swiftTransferAssignment.getReceiverOrganizationName());
                    contentStream.newLine();
                    contentStream.showText("transfer amount: " + transferAssignment.getAmount());
                    contentStream.newLine();
                }

                if (assignment.getCategory().equals(Category.INTERNATIONAL_TRANSFER)) {
                    TransferAssignment transferAssignment = transferAssignmentRepository.loadById(assignment.getAssignmentId(), "Transfer assignment not found");
                    contentStream.showText("Receiver Country: " + transferAssignment.getReceiverCountry());
                    contentStream.newLine();
                    contentStream.showText("Receiver full name: " + transferAssignment.getReceiverFullName());
                    contentStream.newLine();
                    contentStream.showText("Receiver phone number: " + transferAssignment.getReceiverPhoneNumber());
                    contentStream.newLine();
                    contentStream.showText("transfer amount: " + transferAssignment.getAmount());
                    contentStream.newLine();
                    contentStream.showText("transfer type: " + transferAssignment.getType());
                    contentStream.newLine();
                }

                if (assignment.getCategory().equals(Category.CERTIFICATES)) {
                    CertificateAssignment certificateAssignment = certificateAssignmentRepository.loadById(assignment.getAssignmentId(), "not found");
                    contentStream.showText("Begin date: " + certificateAssignment.getBeginDate());
                    contentStream.newLine();
                    contentStream.showText("End date: " + certificateAssignment.getEndDate());
                    contentStream.newLine();

                    List<String> accounts = certificateAssignmentAccountRepository.findAllByCertificateAssignment_AssignmentId(assignment.getAssignmentId())
                            .stream().map(account -> account.getId().getAccountNumber()).toList();
                    contentStream.showText("Accounts: " + accounts);
                    contentStream.newLine();

                    List<String> cards = certificateAssignmentCardRepository.findAllByCertificateAssignment_AssignmentId(assignment.getAssignmentId())
                            .stream().map(card -> card.getId().getCardNumber()).toList();
                    contentStream.showText("Cards: " + cards);
                    contentStream.newLine();
                }

                // Close the content stream
                contentStream.endText();
            }

            // Save the document to a file
            document.save(outputStream);

            // Convert ByteArrayOutputStream to MultipartFile
            MultipartFile multipartFile = new MockMultipartFile(
                    "assignment.pdf",                        // File name
                    "assignment.pdf",                        // Original file name
                    "application/pdf",                       // Content type
                    new ByteArrayInputStream(outputStream.toByteArray())  // InputStream
            );

            return fileService.upload(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
