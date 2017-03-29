package wza.slx.com.xlxapplication.net.http.callback;

import wza.slx.com.xlxapplication.net.http.parser.BaseParser;

/**
 * Created by homelink on 2017/3/27.
 */

public abstract class BaseOKCallback<T> extends OkCallback<T> {
    public BaseOKCallback(BaseParser<T> mParser) {
        super(mParser);
    }

}
