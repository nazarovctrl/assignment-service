package uz.ccrew.assignmentservice.assignment.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum Category {
    SWIFT_PHYSICAL(List.of("receiverCountry", "accountNumber", "swiftCode", "amount", "receiverFullName", "details", "fileId"),
            "swiftPhysical", "SWIFT transfers for physical"),
    SWIFT_LEGAL(List.of("receiverCountry", "accountNumber", "swiftCode", "legalAddress", "receiverOrganizationName", "amount", "details", "fileId"),
            "swiftForLegalEntities", "SWIFT transfers for legal entities"),
    INTERNATIONAL_TRANSFER(List.of("transferType", "receiverCountry", "receiverFullName", "receiverPhoneNumber", "details", "fileId"),
            "internationalTransfers", "International transfers"),
    CERTIFICATES(List.of("accountNumbers", "cardNumbers", "beginDate", "endDate", "details"),
            "certificates", "Certificates transfers"),
    CARD_REFRESH(List.of("fileId", "identityFileId", "details"),
            "cardRefresh", "Card reissue"),
    DISPUTE(List.of("fileId", "photoIds", "details"),
            "dispute", "Dispute transfers"),
    OTHERS(List.of("details"), "others", "others");

    private final List<String> columns;
    private final String fullForm;
    private final String description;

    Category(List<String> columns, String fullForm, String description) {
        this.columns = columns;
        this.fullForm = fullForm;
        this.description = description;
    }

    public static Category fullFormOf(String fullForm) {
        for (Category category : Category.values()) {
            if (category.fullForm.equals(fullForm)) {
                return category;
            }
        }
        return null;
    }
}
