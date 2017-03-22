package wza.slx.com.xlxapplication.net.http.exception;

/**
 * Created by jin.h on 2016/8/28.
 *
 */

public class HttpException extends Exception {

    private int statusCode;

    public HttpException(){}

    public HttpException(int code){
        this.statusCode = code;
    }


    public int getStatusCode(){
        return statusCode;
    }

}
