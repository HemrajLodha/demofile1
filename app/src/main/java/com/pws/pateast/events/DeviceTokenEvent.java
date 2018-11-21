package com.pws.pateast.events;

/**
 * Created by intel on 18-Jul-17.
 */

public class DeviceTokenEvent
{
    private String deviceToken;
    public DeviceTokenEvent(String deviceToken)
    {
        setDeviceToken(deviceToken);
    }

    public DeviceTokenEvent()
    {

    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }
}
