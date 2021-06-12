package org.tiddev.serviceManager.exception;

public class ServiceManagerException extends RuntimeException {

    private String exceptionCode;
    private String exceptionMessage;
    private Object[] args;

    public ServiceManagerException(String exceptionCode, String message, Throwable cause) {
        super(exceptionCode + ":" + message, cause);
        this.exceptionMessage = message;
        this.exceptionCode = exceptionCode;
    }

    public ServiceManagerException(String exceptionCode, String message, Throwable cause, Object[] args) {
        super(exceptionCode + ":" + message, cause);
        this.exceptionCode = exceptionCode;
        this.args = args;
    }

    public ServiceManagerException(String exceptionCode, String message) {
        super(exceptionCode + ":" + message);
        this.exceptionCode = exceptionCode;
    }

    public ServiceManagerException(String exceptionCode, String message, Object[] args) {
        super(exceptionCode + ":" + message);
        this.exceptionCode = exceptionCode;
        this.args = args;
    }

    public ServiceManagerException(String exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
        this.exceptionCode = exceptionCode;
    }

}
