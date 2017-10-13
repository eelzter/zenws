package se.loveone.zenws.exception;

public class NotLoggedInException extends RuntimeException {

    public String redirectUrl;

    public NotLoggedInException(String redirectUrl) {
        this.redirectUrl = redirectUrl;

    }
}
