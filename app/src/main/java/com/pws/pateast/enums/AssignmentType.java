package com.pws.pateast.enums;

/**
 * Created by intel on 06-Jul-17.
 */

public enum  AssignmentType
{
    DRAFT("Draft"),
    PUBLISHED("Published"),
    CANCELED("Canceled"),
    COMPLETED("Completed"),
    REVIEWED("Reviewed"),
    NONE("none");

    private final String value;

    AssignmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AssignmentType getAssignmentType(String value)
    {
        for (AssignmentType type : values())
        {
            if(type.getValue().equals(value))
                return type;
        }
        return NONE;
    }
}
