package com.oa.common.exception;

import com.oa.common.enums.BusinessExceptionEnum;
import com.oa.common.enums.ResultCode;
import com.oa.common.util.StringUtil;
import lombok.Data;

/**
 * 业务异常
 */
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 5587010659354168203L;
    protected String code;
    protected String message;
    protected ResultCode resultCode;
    protected Object data;

    public BusinessException() {
        BusinessExceptionEnum exceptionEnum = BusinessExceptionEnum.getByEClass(this.getClass());
        if (exceptionEnum != null) {
            resultCode = exceptionEnum.getResultCode();
            code = exceptionEnum.getResultCode().code().toString();
            message = exceptionEnum.getResultCode().message();
        }
    }

    public BusinessException(String message) {
        super();
        this.message = message;
    }

    /**
     * 这里可以传多个不定类型的参数进来
     *
     * @param format
     * @param objects
     */
    public BusinessException(String format, Object... objects) {
        this();
        this.message = StringUtil.formatIfArgs(format, "{}", objects);
    }

    public BusinessException(ResultCode resultCode, Object data) {
        this(resultCode);
        this.data = data;
    }

    public BusinessException(ResultCode resultCode) {
        this.resultCode = resultCode;
        this.code = resultCode.code().toString();
        this.message = resultCode.message();
    }
}
