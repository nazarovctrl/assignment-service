package uz.ccrew.assignmentservice.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum Category {
    SWIFT_PHYSICAL(List.of("receiverCountry", "accountNumber", "swiftCode", "amount", "receiverFullName", "details", "fileId","chat_id"),
            "swiftPhysical", "SWIFT transfers for physical"),
    SWIFT_LEGAL(List.of("receiverCountry", "accountNumber", "swiftCode", "legalPersonAddress", "receiverOrganizationName", "fileId", "amount", "details"),
            "swiftForLegalEntities", "SWIFT transfers for legal entities"),
    INTERNATIONAL_TRANSFER(List.of("type", "receiverCountry", "receiverFullName", "phoneNumber", "details", "fileId"),
            "internationalTransfers", "International transfers"),
    CERTIFICATES(List.of("accountNumber", "cardNumber", "beginDate", "endDate"),
            "certificates", "Certificates transfers"),
    CARD_REFRESH(List.of("identityFileId", "fileId", "details"),
            "cardRefresh", "Card reissue"),
    DISPUTE(List.of("fileId", "screenFileId", "details"),
            "dispute", "Dispute transfers"),
    OTHERS(List.of("others"), "others", "others");

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
