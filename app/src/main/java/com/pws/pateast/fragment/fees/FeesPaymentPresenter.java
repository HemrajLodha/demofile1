package com.pws.pateast.fragment.fees;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Fees;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.download.RxDownloadManagerHelper;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.pws.pateast.Constants.CHALLAN_FILES_DIR;
import static com.pws.pateast.Constants.INVOICE_FILES_DIR;
import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;
import static com.pws.pateast.api.model.Fees.FeesStatus.PAID;

/**
 * Created by intel on 02-Feb-18.
 */

public class FeesPaymentPresenter extends AppPresenter<FeesPaymentView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    private User user;
    private Ward ward;
    private ParentService apiService;
    private FeesPaymentView paymentView;

    @Override
    public FeesPaymentView getView() {
        return paymentView;
    }

    @Override
    public void attachView(FeesPaymentView view) {
        paymentView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        getView().onActionClick();
    }

    public void getPaymentHistory() {
        apiService = serviceBuilder.createService(ParentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("studentId", String.valueOf(ward.getUserInfo().getUserId()));
        params.put("masterId", String.valueOf(ward.getMasterId()));
        params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));

        disposable = apiService.getAllFee(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Fees>() {
                    @Override
                    public void onResponse(Fees response) {
                        if (response.getFeeallocations() == null || response.getFeeallocations().isEmpty()) {
                            onError(new RetrofitException(response.getMessage(), ERROR_TYPE_MESSAGE));
                        } else {
                            arrangeFees(response.getFeeallocations(), response.getFeesubmissionrecords());
                        }
                    }

                    @Override
                    public FeesPaymentPresenter getPresenter() {
                        return FeesPaymentPresenter.this;
                    }
                });
    }

    private void arrangeFees(List<Fees> feeAllocations, List<Fees> feeRecords) {
        List<Fees> list = StreamSupport
                .stream(feeAllocations)
                .map(feesAllocation -> {
                    Fees status = StreamSupport
                            .stream(feeRecords)
                            .filter(feesRecord -> {
                                return feesAllocation.getFeeheadId() == feesRecord.getFeeheadId() && feesAllocation.getInstallment() == feesRecord.getInstallment();
                            })
                            .findFirst()
                            .orElse(null);
                    if (status != null) {
                        feesAllocation.setSubmittedAmount(status.getAmount());
                        feesAllocation.setSubmissionDate(status.getFeesubmission().getDate());
                        feesAllocation.setFeeStatus(status.getFeesubmission().getApproved());
                        feesAllocation.setFeesubmissionId(status.getFeesubmissionId());
                        if (feesAllocation.getFeeStatus() == PAID)
                            feesAllocation.setInvoiceUrl("admin/feesubmission/" + status.getFeesubmissionId() + "/invoice.pdf");
                        else
                            feesAllocation.setChallanUrl("admin/feechallan/" + status.getFeesubmissionId() + "/challan.pdf");
                    } else {
                        feesAllocation.setFeeStatus(Fees.FeesStatus.UNPAID);
                    }
                    return feesAllocation;
                })
                .collect(Collectors.toList());

        getView().setFeesAdapter(list);
    }

    public void downloadInvoice(final String invoiceUrl, boolean isInvoice) {
        getView().showDialog(getString(R.string.app_name), getString(R.string.download_prompt_message), new AdapterListener<String>() {
            @Override
            public void onClick(int id, String value) {
                switch (id) {
                    case 0:
                        startDownload(invoiceUrl, isInvoice);
                        break;
                    case 1:
                        break;
                }
            }
        }, R.string.yes, R.string.no);
    }

    private void startDownload(String invoiceUrl, boolean isInvoice) {
        String downloadUrl = ServiceBuilder.API_BASE_URL + invoiceUrl + "?langId=" + preference.getLanguageID() + "&lang=" + preference.getLanguage();
        String destFileName = FileUtils.getFileName(invoiceUrl);
        RxDownloadManagerHelper.enqueueDownload(getView().getDownloadManager(), preference.getAccessToken().bearer(), downloadUrl, isInvoice ? INVOICE_FILES_DIR : CHALLAN_FILES_DIR, destFileName);
    }
}
