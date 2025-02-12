package life.khabanh.usersservices.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode  {
    USER_EXSITED(1001, "User existed");

    private int code;
    private String message;
}
