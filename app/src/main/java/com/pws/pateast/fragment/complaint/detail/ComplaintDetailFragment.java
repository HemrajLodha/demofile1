package com.pws.pateast.fragment.complaint.detail;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.activity.parent.fees.checkout.CheckoutFragment;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.DateUtils;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.pws.pateast.utils.DateUtils.DATE_FORMAT_PATTERN;
import static com.pws.pateast.utils.DateUtils.DATE_TIME_TEMPLATE;

/**
 * Created by intel on 10-Feb-18.
 */
@RuntimePermissions
public class ComplaintDetailFragment extends AppFragment implements ComplaintDetailView {
    private TextView tvComplaintBy, tvComplaintByName, tvPenalty, tvComplaint, /*tvPaymentStatus,*/ tvDateTime, tvFollowAction;
    private Button btnDownloadProof, btnPay, btnSendEmail;
    private CardView cardAction;
    private ComplaintDetailPresenter mPresenter;
    private DownloadManager downloadManager;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_complaint_detail;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_complaint_detail);
        downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);

        cardAction = (CardView) findViewById(R.id.card_action);
        tvComplaintBy = (TextView) findViewById(R.id.tv_complaint_by);
        tvComplaintByName = (TextView) findViewById(R.id.tv_complaint_by_name);
        tvPenalty = (TextView) findViewById(R.id.tv_penalty);
        //tvPaymentStatus = (TextView) findViewById(R.id.tv_payment_status);
        tvComplaint = (TextView) findViewById(R.id.tv_complaint);
        tvDateTime = (TextView) findViewById(R.id.tv_date_time);
        tvFollowAction = (TextView) findViewById(R.id.tv_follow_up);
        btnDownloadProof = (Button) findViewById(R.id.btn_download_proof);
        btnPay = (Button) findViewById(R.id.btn_pay);
        btnSendEmail = (Button) findViewById(R.id.btn_send_email);

        mPresenter = new ComplaintDetailPresenter();
        mPresenter.attachView(this);

    }

    @Override
    public void setComplaintDetail(UserType userType, Complaint complaint) {
        complaint = complaint.getComplaint();
        tvComplaintBy.setText(getString(R.string.label_complaint_by));
        tvComplaintByName.setText(UserType.getUserType(complaint.getUser().getUser_type()).getName());
        if (complaint.getIs_penalty() == 1) {
            //tvPaymentStatus.setText(complaint.getPenalty_status() == 2 ? R.string.status_pending : R.string.status_paid);
            //tvPaymentStatus.setTextColor(ContextCompat.getColor(getContext(), complaint.getPenalty_status() == 2 ? R.color.md_red_700 : R.color.md_green_700));
            tvPenalty.setText(String.format("%s %d", Html.fromHtml("&#8377"), (int) complaint.getFine_amount()));
            //tvPaymentStatus.setVisibility(View.VISIBLE);
            ((LinearLayout) tvPenalty.getParent()).setVisibility(View.VISIBLE);
        } else {
            //tvPaymentStatus.setVisibility(View.GONE);
            ((LinearLayout) tvPenalty.getParent()).setVisibility(View.GONE);
        }
        tvComplaint.setText(complaint.getComplaint_detail());
        tvDateTime.setText(DateUtils.toDate(DateUtils.parse(complaint.getCreatedAt(), DATE_FORMAT_PATTERN), DATE_TIME_TEMPLATE));
        if (TextUtils.isEmpty(complaint.getTagIds()))
            ((LinearLayout) tvFollowAction.getParent()).setVisibility(View.GONE);
        else {
            tvFollowAction.setText(mPresenter.getFollowUp(complaint.getTagIds()));
            ((LinearLayout) tvFollowAction.getParent()).setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(complaint.getImage())) {
            btnDownloadProof.setOnClickListener(null);
            btnDownloadProof.setVisibility(View.GONE);
        } else {
            btnDownloadProof.setOnClickListener(this);
            btnDownloadProof.setVisibility(View.VISIBLE);
        }

        switch (userType) {
            case PARENT:
                cardAction.setVisibility(complaint.getPenalty_status() == 2 ? View.VISIBLE : View.GONE);
                btnPay.setVisibility(complaint.getIs_penalty() == 1 ? View.GONE : View.GONE);
                btnPay.setOnClickListener(this);
                btnSendEmail.setOnClickListener(this);
                break;
            case STUDENT:
                cardAction.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public Complaint getComplaint() {
        return getArguments().getParcelable(Extras.COMPLAINT);
    }

    @Override
    public List<Tag> getTags() {
        return getArguments().getParcelableArrayList(Extras.COMPLAINT_TAGS);
    }

    @Override
    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @Override
    public void downloadProof(String proofUrl) {
        if (mPresenter != null)
            mPresenter.downloadProof(proofUrl);
    }

    @Override
    public void doCheckout(String paymentMode) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download_proof:
                ComplaintDetailFragmentPermissionsDispatcher.downloadProofWithCheck(this, getComplaint().getComplaint().getImage());
                break;
            case R.id.btn_pay:
                CheckoutFragment checkoutFragment = AppDialogFragment.newInstance(CheckoutFragment.class, getAppListener());
                checkoutFragment.attachView(this);
                checkoutFragment.show(getFragmentManager(), CheckoutFragment.class.getSimpleName());
                break;
            case R.id.btn_send_email:
                Bundle bundle = new Bundle();
                bundle.putParcelable(Extras.COMPLAINT, getComplaint());
                bundle.putInt(FragmentActivity.EXTRA_TASK_TYPE, TaskType.COMPLAINT_REPORT.getValue());
                getAppListener().openActivity(ParentTaskActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ComplaintDetailFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
