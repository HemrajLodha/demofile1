package com.pws.pateast.api.model;

import java.util.ArrayList;

/**
 * Created by intel on 08-Aug-17.
 */

public class Role
{
    String slug,name;
    int id;

    ArrayList<Role> roledetails;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Role> getRoledetails() {
        return roledetails;
    }

    public void setRoledetails(ArrayList<Role> roledetails) {
        this.roledetails = roledetails;
    }
}
