package com.pws.pateast.api.exception;

import java.io.IOException;

/**
 * Created by intel on 20-Apr-17.
 */

public class RetrofitException extends IOException
{
    public static final int ERROR_TYPE_MESSAGE = 1;
    public static final int ERROR_TYPE_UNAUTHORIZED = -1;
    private String message;
    private int errorType;

    public RetrofitException(String message,int type)
    {
        this(message);
        this.errorType = type;
    }


    public RetrofitException(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public int getErrorType() {
        return errorType;
    }
}
