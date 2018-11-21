package com.pws.pateast.fragment.assignment.student.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pws.pateast.fragment.assignment.student.StudentAssignmentView;
import com.pws.pateast.R;
import com.pws.pateast.base.AppDialogFragment;

/**
 * Created by intel on 15-May-17.
 */

public class StudentAssignmentFilter extends AppDialogFragment implements StudentAssignmentFilterView, View.OnClickListener
{

    private Button btnApply, btnReset;

    private EditText etSubject,etTitle;

    private StudentAssignmentFilterPresenter presenter;

    private StudentAssignmentView assignmentView;


    public void attach(StudentAssignmentView assignmentView) {
        this.assignmentView = assignmentView;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_student_assignment_filter;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.filter);
        btnApply = (Button) findViewById(R.id.btn_apply);
        btnReset = (Button) findViewById(R.id.btn_reset);

        etSubject = (EditText) findViewById(R.id.et_subject);
        etTitle = (EditText) findViewById(R.id.et_title);

        btnApply.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        presenter = new StudentAssignmentFilterPresenter();
        presenter.attachView(this);
        presenter.setFilterData(assignmentView);
    }

    @Override
    public void setSubjectTitle(String subjectTitle) {
        etSubject.setText(subjectTitle);
    }

    @Override
    public void setAssignmentTitle(String assignmentTitle) {
        etTitle.setText(assignmentTitle);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_apply:
                if (assignmentView != null)
                {
                    String subject = etSubject.getText().toString();
                    String assignmentTitle = etTitle.getText().toString();
                    assignmentView.setFilter(subject,assignmentTitle);
                    dismiss();
                }
                break;
            case R.id.btn_reset:
                if (assignmentView != null) {
                    assignmentView.clearFilter();
                    presenter.setFilterData(assignmentView);
                }
                break;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        presenter.detachView();
    }

}
