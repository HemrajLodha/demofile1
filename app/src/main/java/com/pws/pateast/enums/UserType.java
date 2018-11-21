package com.pws.pateast.enums;

/**
 * Created by planet on 4/19/2017.
 */

public enum UserType {
    STUDENT("student"),
    PARENT("parent"),
    TEACHER("teacher"),
    ADMIN("admin"),
    INSTITUTE("institute"),
    DRIVER("driver"),
    NONE("none");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName()
    {
        return getValue().substring(0, 1).toUpperCase() + getValue().substring(1);
    }

    public static UserType getUserType(String value) {
        for (UserType userType : values())
        {
            if(userType.getValue().equalsIgnoreCase(value))
                return userType;
        }
        return NONE;
    }
}
