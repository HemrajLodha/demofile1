package com.pws.pateast.api.exception;

import java.io.IOException;

/**
 * Created by intel on 20-Apr-17.
 */

public class NoConnectivityException extends IOException
{
    String message;

    public NoConnectivityException(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
