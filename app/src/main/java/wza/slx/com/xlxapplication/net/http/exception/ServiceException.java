package wza.slx.com.xlxapplication.net.http.exception;

public class ServiceException extends Exception {

    private int code;

    public ServiceException(int code) {
        this.code = code;
    }

    public ServiceException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }


    public int getCode() {
        return code;
    }
}
