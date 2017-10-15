package lftc.edu.util;

import lombok.Getter;
public class CustomException extends Throwable {

    @Getter
    private String msg;

    public CustomException(String s) {
        msg = s;
    }

}
