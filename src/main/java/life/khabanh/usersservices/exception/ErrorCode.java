package life.khabanh.usersservices.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode  {

    // GENERAL ERRORS
    UNCATEGORIZED_EXCEPTION(5000, "Uncategorized error. Details in the result section"),

    // VALIDATION ERRORS
    INVALID_REQUEST(4001, "Invalid request data"),
    INVALID_EMAIL(4002, "Invalid email format"),
    INVALID_PASSWORD(4003, "Password must be at least 8 characters"),
    INVALID_PHONE_NUMBER(4004, "Invalid phone number"),
    INVALID_DATE(4005, "Invalid date"),
    INVALID_INVITE_CODE(4006, "Invalid key"),

    // USER & AUTHENTICATION ERRORS
    USER_EXISTED(4100, "User already exists"),
    USER_NOT_EXISTED(4101, "User does not exist"),
    INCORRECT_PASSWORD(4102, "Incorrect password");

    final int code;
    final String message;
}
