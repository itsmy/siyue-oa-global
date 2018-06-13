package com.oa.common.exception;

import com.oa.common.enums.ResultCode;

public class DataConflictException extends BusinessException {
    private static final long serialVersionUID = 4779335569056115889L;

    public DataConflictException() {
        super();
    }

    public DataConflictException(Object data) {
        super.data = data;
    }

    public DataConflictException(ResultCode resultCode) {
        super(resultCode);
    }

    public DataConflictException(String message) {
        super(message);
    }

    public DataConflictException(ResultCode resultCode, Object data) {
        super(resultCode, data);
    }

    public DataConflictException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }
}
