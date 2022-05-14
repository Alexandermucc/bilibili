package com.alex.domain.exception;

/**
 * @author Alexandermucc
 * @date 2022/05/14 13:49
 **/
public class ConditionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    public ConditionException(String code, String name) {
        super(name);
        this.code = code;
    }

    public ConditionException(String name) {
        super(name);
        code = "500";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

