package uz.ccrew.assignmentservice.exp.unauthorized;

public class TokenExpiredException extends Unauthorized {
    public TokenExpiredException(String message) {
        super(message);
    }
}