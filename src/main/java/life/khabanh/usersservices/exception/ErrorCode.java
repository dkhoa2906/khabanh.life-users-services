package life.khabanh.usersservices.exception;

public enum ErrorCode  {
    USER_EXSITED(1001, "User existed");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
