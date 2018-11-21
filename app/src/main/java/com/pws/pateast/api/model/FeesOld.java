package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 01-Feb-18.
 */

public class FeesOld extends Response<ArrayList<FeesOld>> implements Parcelable {
    List<FeesOld> heads, penalty, discount;
    int month, fee_id, pay_fee_id;
    double total, amount;
    String date, head_name, fee_status, mode, pay_date, invoice;


    protected FeesOld(Parcel in) {
        heads = in.createTypedArrayList(FeesOld.CREATOR);
        penalty = in.createTypedArrayList(FeesOld.CREATOR);
        discount = in.createTypedArrayList(FeesOld.CREATOR);
        month = in.readInt();
        fee_id = in.readInt();
        pay_fee_id = in.readInt();
        total = in.readDouble();
        amount = in.readDouble();
        date = in.readString();
        head_name = in.readString();
        fee_status = in.readString();
        mode = in.readString();
        pay_date = in.readString();
        invoice = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(heads);
        dest.writeTypedList(penalty);
        dest.writeTypedList(discount);
        dest.writeInt(month);
        dest.writeInt(fee_id);
        dest.writeInt(pay_fee_id);
        dest.writeDouble(total);
        dest.writeDouble(amount);
        dest.writeString(date);
        dest.writeString(head_name);
        dest.writeString(fee_status);
        dest.writeString(mode);
        dest.writeString(pay_date);
        dest.writeString(invoice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FeesOld> CREATOR = new Creator<FeesOld>() {
        @Override
        public FeesOld createFromParcel(Parcel in) {
            return new FeesOld(in);
        }

        @Override
        public FeesOld[] newArray(int size) {
            return new FeesOld[size];
        }
    };

    public List<FeesOld> getHeads() {
        return heads;
    }

    public void setHeads(List<FeesOld> heads) {
        this.heads = heads;
    }

    public List<FeesOld> getPenalty() {
        return penalty;
    }

    public void setPenalty(List<FeesOld> penalty) {
        this.penalty = penalty;
    }

    public List<FeesOld> getDiscount() {
        return discount;
    }

    public void setDiscount(List<FeesOld> discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHead_name() {
        return head_name;
    }

    public void setHead_name(String head_name) {
        this.head_name = head_name;
    }

    public String getFee_status() {
        return fee_status;
    }

    public void setFee_status(String fee_status) {
        this.fee_status = fee_status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public int getFee_id() {
        return fee_id;
    }

    public void setFee_id(int fee_id) {
        this.fee_id = fee_id;
    }

    public int getPay_fee_id() {
        return pay_fee_id;
    }

    public void setPay_fee_id(int pay_fee_id) {
        this.pay_fee_id = pay_fee_id;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}
