package com.pws.pateast.api.model;

import java.util.ArrayList;

/**
 * Created by intel on 25-Jul-17.
 */

public class ChatPermission extends Response<ArrayList<String>>
{
    ArrayList<String> permissions;

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }
}
