package com.pws.pateast.fragment.complaint.report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pws.pateast.R;
import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.listener.AdapterListener;

/**
 * Created by intel on 10-Feb-18.
 */

public class ComplaintReportFragment extends AppFragment implements ComplaintReportView {
    private EditText etReport;
    private Button btnSendEmail;
    private ComplaintReportPresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_complaint_report;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.report_discuss);

        etReport = (EditText) findViewById(R.id.et_report);
        btnSendEmail = (Button) findViewById(R.id.btn_send_email);

        mPresenter = new ComplaintReportPresenter();
        mPresenter.attachView(this);

        btnSendEmail.setOnClickListener(this);
    }

    @Override
    public boolean isError(CharSequence message) {
        boolean isError = false;
        if (TextUtils.isEmpty(message)) {
            isError = true;
            etReport.setError(getString(R.string.validate_edittext, getString(R.string.title_report)));
        }
        return isError;
    }

    @Override
    public void setReportDetail(Complaint complaint) {
        complaint = complaint.getComplaint();
        btnSendEmail.setText(getString(R.string.mail_to_admin, UserType.TEACHER.getName()));
    }

    @Override
    public Complaint getComplaint() {
        return getArguments().getParcelable(Extras.COMPLAINT);
    }

    @Override
    public void navigateTpDetail(String message) {
        showDialog(getString(R.string.app_name), message, new AdapterListener<String>() {
            @Override
            public void onClick(int id, String value) {
                getActivity().finish();
            }
        }, R.string.ok);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_email:
                if (mPresenter != null)
                    mPresenter.sendEmailForComplaint(etReport.getText());
                break;
        }
    }
}
