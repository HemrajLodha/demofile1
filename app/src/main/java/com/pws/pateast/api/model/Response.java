package com.pws.pateast.api.model;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by intel on 06-Mar-17.
 */

public class Response<T> {
    private int code, totalData, pageCount, pageLimit, blocked;
    private boolean status;
    private String message, error_description, error, path;
    private ArrayList<Response> errors;
    protected T data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        if (!TextUtils.isEmpty(message))
            return message;
        else if (!TextUtils.isEmpty(error_description))
            return error_description;
        else if (errors != null && !errors.isEmpty())
            return errors.get(0).getMessage();
        else
            return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageLimit() {
        return pageLimit;
    }

    public void setPageLimit(int pageLimit) {
        this.pageLimit = pageLimit;
    }

    public boolean isBlocked() {
        return blocked == 1;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    public int getBlocked() {
        return blocked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Response> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<Response> errors) {
        this.errors = errors;
    }
}
