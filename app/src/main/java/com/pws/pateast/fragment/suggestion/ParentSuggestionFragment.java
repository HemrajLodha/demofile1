package com.pws.pateast.fragment.suggestion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pws.pateast.R;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.utils.Utils;

/**
 * Created by intel on 19-Aug-17.
 */

public class ParentSuggestionFragment extends AppFragment implements ParentSuggestionView, View.OnClickListener {
    private EditText etSubject, etDescription;
    private Button btnSubmit;

    private ParentSuggestionPresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_parent_suggestion;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.title_suggestions);

        etSubject = (EditText) findViewById(R.id.et_subject);
        etDescription = (EditText) findViewById(R.id.et_description);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(this);

        mPresenter = new ParentSuggestionPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void setError(int id, String error) {
        switch (id) {
            case R.id.et_subject:
                etSubject.setError(error);
                break;
            case R.id.et_description:
                etDescription.setError(error);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                boolean isError = false;

                if (TextUtils.isEmpty(etSubject.getText())) {
                    setError(R.id.et_subject, getString(R.string.validate_edittext, getString(R.string.hint_subject)));
                    isError = true;
                } else {
                    setError(R.id.et_subject, null);
                }

                if (TextUtils.isEmpty(etDescription.getText())) {
                    setError(R.id.et_description, getString(R.string.validate_edittext, getString(R.string.hint_description)));
                    isError = true;
                } else {
                    setError(R.id.et_description, null);
                }

                if (!isError) {
                    mPresenter.sendSuggestion(etSubject.getText().toString(), etDescription.getText().toString());
                }
                break;
        }
    }

    @Override
    public void onError(String message) {
        Utils.showToast(getContext(), !TextUtils.isEmpty(message) ? message :
                getContext().getString(R.string.message_failed));
    }

    @Override
    public void onSuccess(String message) {
        Utils.showToast(getContext(), !TextUtils.isEmpty(message) ? message :
                getContext().getString(R.string.message_saved_successfully));
        getActivity().finish();
    }
}
