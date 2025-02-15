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
    MISSING_REQUEST_BODY(5001, "Request body is missing"),
    MISSING_REQUIRED_FIELDS(5002, "Missing required field(s)"),

    // VALIDATION ERRORS
    INVALID_REQUEST(4001, "Invalid request data"),

    // USER & AUTHENTICATION ERRORS
    USER_EXISTED(4100, "User already exists"),
    USER_NOT_EXISTED(4101, "User does not exist"),
    INCORRECT_PASSWORD(4102, "Incorrect password"),
    UNAUTHENTICATED(4103, "Unauthenticated"),

    // ADMIN ERRORS
    INVITE_CODE_EXISTED(4201, "Invite code already exists");

    final int code;
    final String message;
}
