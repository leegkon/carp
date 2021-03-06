package com.github.rxyor.common.core.exception;


import lombok.Getter;
import lombok.ToString;

/**
 *<p>
 *
 *</p>
 *
 * @author liuyang
 * @date 2019-05-15 Wed 17:13:00
 * @since 1.0.0
 */
@ToString(callSuper = true)
public class CarpIOException extends RuntimeException {

    static final long serialVersionUID = -4674326467577896888L;

    @Getter
    protected String msg;

    public CarpIOException() {
        this("io exception");
    }

    public CarpIOException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CarpIOException(Throwable e) {
        super(e.getMessage());
        this.msg = e.getMessage();
    }

}
