package wza.slx.com.xlxapplication.manager;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TypeEva {
    public static final int SMS = 1;
    public static final int Contacts = 2;
    public static final int CallRecords = 3;

    @IntDef({SMS, Contacts, CallRecords})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EvaType {
    }
}
