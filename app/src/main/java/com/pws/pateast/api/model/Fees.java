package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static com.pws.pateast.api.model.Fees.FeesStatus.CHALLAN;
import static com.pws.pateast.api.model.Fees.FeesStatus.PAID;
import static com.pws.pateast.api.model.Fees.FeesStatus.UNPAID;

public class Fees extends Response<ArrayList<Fees>> implements Parcelable {
    int id, feeheadId, installment, feesubmissionId, no_of_installments, feepenaltyId, days, approved, feeStatus, type;
    double amount, submittedAmount, value;
    String date, name, submissionDate, invoiceUrl, challanUrl;
    Fees feehead, fee, feepenalty, feesubmission;
    private List<Fees> feeallocations, feesubmissionrecords, feeheaddetails, feepenaltyslabs, feediscounts, feediscountdetails, feeallocationpenalties, feepenaltydetails;

    protected Fees(Parcel in) {
        id = in.readInt();
        feeheadId = in.readInt();
        installment = in.readInt();
        feesubmissionId = in.readInt();
        no_of_installments = in.readInt();
        feepenaltyId = in.readInt();
        days = in.readInt();
        approved = in.readInt();
        feeStatus = in.readInt();
        type = in.readInt();
        amount = in.readDouble();
        submittedAmount = in.readDouble();
        value = in.readDouble();
        date = in.readString();
        name = in.readString();
        submissionDate = in.readString();
        invoiceUrl = in.readString();
        challanUrl = in.readString();
        feehead = in.readParcelable(Fees.class.getClassLoader());
        fee = in.readParcelable(Fees.class.getClassLoader());
        feepenalty = in.readParcelable(Fees.class.getClassLoader());
        feesubmission = in.readParcelable(Fees.class.getClassLoader());
        feeallocations = in.createTypedArrayList(Fees.CREATOR);
        feesubmissionrecords = in.createTypedArrayList(Fees.CREATOR);
        feeheaddetails = in.createTypedArrayList(Fees.CREATOR);
        feepenaltyslabs = in.createTypedArrayList(Fees.CREATOR);
        feediscounts = in.createTypedArrayList(Fees.CREATOR);
        feediscountdetails = in.createTypedArrayList(Fees.CREATOR);
        feeallocationpenalties = in.createTypedArrayList(Fees.CREATOR);
        feepenaltydetails = in.createTypedArrayList(Fees.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(feeheadId);
        dest.writeInt(installment);
        dest.writeInt(feesubmissionId);
        dest.writeInt(no_of_installments);
        dest.writeInt(feepenaltyId);
        dest.writeInt(days);
        dest.writeInt(approved);
        dest.writeInt(feeStatus);
        dest.writeInt(type);
        dest.writeDouble(amount);
        dest.writeDouble(submittedAmount);
        dest.writeDouble(value);
        dest.writeString(date);
        dest.writeString(name);
        dest.writeString(submissionDate);
        dest.writeString(invoiceUrl);
        dest.writeString(challanUrl);
        dest.writeParcelable(feehead, flags);
        dest.writeParcelable(fee, flags);
        dest.writeParcelable(feepenalty, flags);
        dest.writeParcelable(feesubmission, flags);
        dest.writeTypedList(feeallocations);
        dest.writeTypedList(feesubmissionrecords);
        dest.writeTypedList(feeheaddetails);
        dest.writeTypedList(feepenaltyslabs);
        dest.writeTypedList(feediscounts);
        dest.writeTypedList(feediscountdetails);
        dest.writeTypedList(feeallocationpenalties);
        dest.writeTypedList(feepenaltydetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Fees> CREATOR = new Creator<Fees>() {
        @Override
        public Fees createFromParcel(Parcel in) {
            return new Fees(in);
        }

        @Override
        public Fees[] newArray(int size) {
            return new Fees[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFeeheadId() {
        return feeheadId;
    }

    public void setFeeheadId(int feeheadId) {
        this.feeheadId = feeheadId;
    }

    public int getInstallment() {
        return installment;
    }

    public void setInstallment(int installment) {
        this.installment = installment;
    }

    public int getNo_of_installments() {
        return no_of_installments;
    }

    public void setNo_of_installments(int no_of_installments) {
        this.no_of_installments = no_of_installments;
    }

    public int getFeepenaltyId() {
        return feepenaltyId;
    }

    public void setFeepenaltyId(int feepenaltyId) {
        this.feepenaltyId = feepenaltyId;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fees getFeehead() {
        return feehead;
    }

    public void setFeehead(Fees feehead) {
        this.feehead = feehead;
    }

    public Fees getFee() {
        return fee;
    }

    public void setFee(Fees fee) {
        this.fee = fee;
    }

    public Fees getFeepenalty() {
        return feepenalty;
    }

    public void setFeepenalty(Fees feepenalty) {
        this.feepenalty = feepenalty;
    }

    public List<Fees> getFeeallocations() {
        return feeallocations;
    }

    public void setFeeallocations(List<Fees> feeallocations) {
        this.feeallocations = feeallocations;
    }

    public List<Fees> getFeesubmissionrecords() {
        return feesubmissionrecords;
    }

    public void setFeesubmissionrecords(List<Fees> feesubmissionrecords) {
        this.feesubmissionrecords = feesubmissionrecords;
    }

    public List<Fees> getFeeheaddetails() {
        return feeheaddetails;
    }

    public void setFeeheaddetails(List<Fees> feeheaddetails) {
        this.feeheaddetails = feeheaddetails;
    }

    public List<Fees> getFeediscounts() {
        return feediscounts;
    }

    public void setFeediscounts(List<Fees> feediscounts) {
        this.feediscounts = feediscounts;
    }

    public List<Fees> getFeeallocationpenalties() {
        return feeallocationpenalties;
    }

    public void setFeeallocationpenalties(List<Fees> feeallocationpenalties) {
        this.feeallocationpenalties = feeallocationpenalties;
    }

    public List<Fees> getFeepenaltydetails() {
        return feepenaltydetails;
    }

    public void setFeepenaltydetails(List<Fees> feepenaltydetails) {
        this.feepenaltydetails = feepenaltydetails;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public Fees getFeesubmission() {
        return feesubmission;
    }

    public void setFeesubmission(Fees feesubmission) {
        this.feesubmission = feesubmission;
    }

    public int getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(int feeStatus) {
        this.feeStatus = feeStatus;
    }

    public double getSubmittedAmount() {
        return submittedAmount;
    }

    public void setSubmittedAmount(double submittedAmount) {
        this.submittedAmount = submittedAmount;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public List<Fees> getFeediscountdetails() {
        return feediscountdetails;
    }

    public void setFeediscountdetails(List<Fees> feediscountdetails) {
        this.feediscountdetails = feediscountdetails;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public List<Fees> getFeepenaltyslabs() {
        return feepenaltyslabs;
    }

    public void setFeepenaltyslabs(List<Fees> feepenaltyslabs) {
        this.feepenaltyslabs = feepenaltyslabs;
    }

    public int getFeesubmissionId() {
        return feesubmissionId;
    }

    public void setFeesubmissionId(int feesubmissionId) {
        this.feesubmissionId = feesubmissionId;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public String getChallanUrl() {
        return challanUrl;
    }

    public void setChallanUrl(String challanUrl) {
        this.challanUrl = challanUrl;
    }

    @IntDef({CHALLAN, PAID, UNPAID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FeesStatus {
        int CHALLAN = 0;
        int PAID = 1;
        int UNPAID = -1;
    }
}
