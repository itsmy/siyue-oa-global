package com.oa.common.exception;

public class UserNotLoginException extends BusinessException {
    private static final long serialVersionUID = 4526905211549905291L;
    public UserNotLoginException() {
        super();
    }

    public UserNotLoginException(String msg) {
        super(msg);
    }

    public UserNotLoginException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

}
