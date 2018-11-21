package com.pws.pateast.fragment.assignment.teacher.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.pws.pateast.fragment.assignment.teacher.AssignmentListView;
import com.pws.pateast.adapter.ClassAdapter;
import com.pws.pateast.widget.MaterialSpinner;
import com.pws.pateast.R;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppDialogFragment;

import java.util.List;

/**
 * Created by intel on 03-May-17.
 */

public class AssignmentFilterFragment extends AppDialogFragment implements AssignmentFilterView, AdapterView.OnItemSelectedListener, View.OnClickListener
{
    private Button btnApply, btnReset;

    private MaterialSpinner spClass;
    private EditText etSubject,etTitle;
    private ClassAdapter classAdapter;

    private AssignmentListView assignmentView;
    private AssignmentFilterPresenter presenter;
    private TeacherClass classes;

    public void attach(AssignmentListView assignmentView)
    {
        this.assignmentView = assignmentView;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_assignment_filter;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState)
    {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.filter);

        btnApply = (Button) findViewById(R.id.btn_apply);
        btnReset = (Button) findViewById(R.id.btn_reset);

        spClass = (MaterialSpinner) findViewById(R.id.sp_class);
        etSubject = (EditText) findViewById(R.id.et_subject);
        etTitle = (EditText) findViewById(R.id.et_title);

        btnApply.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        spClass.setOnItemSelectedListener(this);

        presenter = new AssignmentFilterPresenter(true);
        presenter.attachView(this);
        presenter.setFilterData(assignmentView);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        presenter.detachView();
    }


    @Override
    public void setSubject(String subject) {
        etSubject.setText(subject);
    }

    @Override
    public void setAssignmentTitle(String assignmentTitle) {
        etTitle.setText(assignmentTitle);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.sp_class:
                if(position != -1 && classAdapter!= null)
                {
                    classes = classAdapter.getItem(position);
                }
                else
                {
                    classes = null;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btn_apply:
                if (assignmentView != null)
                {
                    String subject = etSubject.getText().toString();
                    String assignmentTitle = etTitle.getText().toString();
                    assignmentView.setFilter(classes != null ? classes.getBcsMapId() : 0,subject,assignmentTitle);
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
    public void setClass(TeacherClass classes) {
        this.classes = classes;
        spClass.setSelection(presenter.getSelectedItemPosition(classAdapter.getDatas(),classes));
    }

    @Override
    public void setClassAdapter(List<TeacherClass> classes) {
        if(spClass.getAdapter() == null)
        {
            classAdapter = new ClassAdapter(getContext());
            spClass.setAdapter(classAdapter);
        }
        classAdapter.update(classes);
        assignmentView.setMyClasses(classes);
        setClass(TeacherClass.getTeacherClass(assignmentView.getClassId()));
    }
}
