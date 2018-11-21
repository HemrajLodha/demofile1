package com.pws.pateast.fragment.fees;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.pws.pateast.R;
import com.pws.pateast.activity.parent.fees.WardFeesActivity;
import com.pws.pateast.api.model.Fees;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.fees.invoice.FeesInvoiceFragment;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.pws.pateast.api.model.Fees.FeesStatus.UNPAID;

/**
 * Created by intel on 02-Feb-18.
 */
@RuntimePermissions
public class FeesPaymentFragment extends AppFragment implements FeesPaymentView, BaseRecyclerAdapter.OnItemClickListener {
    private RecyclerView rvPaymentHistory;

    private LinearLayoutManager llm;
    private FeesPaymentPresenter mPresenter;
    private FeesPaymentAdapter mAdapter;
    private DownloadManager downloadManager;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_fees_payment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_payment_history);
        downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        rvPaymentHistory = (RecyclerView) findViewById(R.id.rv_payment_history);
        llm = new LinearLayoutManager(getContext());
        rvPaymentHistory.setLayoutManager(llm);

        mPresenter = new FeesPaymentPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getPaymentHistory();
    }

    @Override
    public void setFeesAdapter(List<Fees> fees) {
        if (rvPaymentHistory.getAdapter() == null) {
            mAdapter = new FeesPaymentAdapter(getContext(), this);
            rvPaymentHistory.setAdapter(mAdapter);
        }
        mAdapter.update(fees);
    }

    @Override
    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @Override
    public void downloadInvoice(String invoiceUrl, boolean isInvoice) {
        if (mPresenter != null)
            mPresenter.downloadInvoice(invoiceUrl, isInvoice);
    }


    @Override
    public void onItemClick(View view, int position) {
        Fees fees = mAdapter.getItem(position);
        switch (view.getId()) {
            default:
                if (fees.getFeeStatus() == UNPAID) {
                    Intent intent = new Intent(getContext(), WardFeesActivity.class);
                    intent.putExtra(Extras.FEES, fees);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                return;
            case R.id.btn_download_invoice:
                FeesPaymentFragmentPermissionsDispatcher.downloadInvoiceWithCheck(this, fees.getInvoiceUrl(), true);
                break;
            case R.id.btn_download_challan:
                FeesPaymentFragmentPermissionsDispatcher.downloadInvoiceWithCheck(this, fees.getChallanUrl(), false);
                break;
            case R.id.btn_send_invoice:
            case R.id.btn_send_challan:
                Bundle bundle = new Bundle();
                bundle.putInt(Extras.PAY_FEE_ID, fees.getFeesubmissionId());
                bundle.putBoolean(Extras.PAYMENT_MODE, view.getId() == R.id.btn_send_invoice);
                FeesInvoiceFragment invoiceFragment = AppDialogFragment.newInstance(FeesInvoiceFragment.class, getAppListener(), bundle);
                invoiceFragment.attachView(this);
                invoiceFragment.show(getFragmentManager(), FeesInvoiceFragment.class.getSimpleName());
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FeesPaymentFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    onActionClick();
                }
                break;
        }
    }
}
