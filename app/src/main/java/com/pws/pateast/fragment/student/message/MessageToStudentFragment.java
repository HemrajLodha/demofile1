package com.pws.pateast.fragment.student.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.student.MyStudentView;

import static android.view.View.GONE;

/**
 * Created by intel on 11-Jul-17.
 */

public class MessageToStudentFragment extends AppDialogFragment implements MessageStudentView, View.OnClickListener {
    private Button btnSend;
    private TextView tvStudentName;
    private TextView tvErrorMessage;
    private EditText etMessage;
    private TextInputLayout tilMessage;

    private MessageStudentPresenter messagePresenter;

    private MyStudentView studentView;

    public void attach(MyStudentView studentView) {
        this.studentView = studentView;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_message_student;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setToolbarVisibility(GONE);

        btnSend = (Button) findViewById(R.id.btn_send);
        tvStudentName = (TextView) findViewById(R.id.tv_student_name);
        etMessage = (EditText) findViewById(R.id.et_message);
        tilMessage = (TextInputLayout) findViewById(R.id.til_message);
        tvErrorMessage = (TextView) findViewById(R.id.tv_error_message);

        btnSend.setOnClickListener(this);

        messagePresenter = new MessageStudentPresenter();
        messagePresenter.attachView(this);
        tvStudentName.setText(getString(R.string.message_to, getStudentName()));
    }

    @Override
    public void setError(int id, String error) {
        switch (id) {
            case R.id.til_message:
                if (error != null) {
                    tvErrorMessage.setText(error);
                    tvErrorMessage.setVisibility(View.VISIBLE);
                } else {
                    tvErrorMessage.setText("");
                    tvErrorMessage.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public String getMessage() {
        return etMessage.getText().toString().trim();
    }

    @Override
    public int getStudentId() {
        return getArguments().getInt(Extras.STUDENT_ID);
    }

    @Override
    public String getStudentName() {
        return getArguments().getString(Extras.STUDENT_NAME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (messagePresenter != null)
                    messagePresenter.sendMessage();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (messagePresenter != null)
            messagePresenter.detachView();
    }
}
