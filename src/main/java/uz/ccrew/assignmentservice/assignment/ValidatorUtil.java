package uz.ccrew.assignmentservice.assignment;

public class ValidatorUtil {
    public static boolean isNotValidAccountNumber(String accountNumber) {
        String regex = "^[0-9]{10}$";
        return !accountNumber.matches(regex);
    }

    public static boolean isNotValidCardNumber(String cardNumber) {
        String regex = "^[0-9]{16}$";
        return !cardNumber.matches(regex);
    }

    public static boolean isNotValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+?[0-9]{10,15}$";
        return !phoneNumber.matches(regex);
    }

    public static boolean isNotValidSwiftCode(String swiftCode) {
        String regex = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$";
        return swiftCode.matches(regex);
    }
}
