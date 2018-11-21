package com.pws.pateast.activity.teacher.student;

import android.os.Bundle;

import com.pws.pateast.R;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.presenter.ClassPresenter;
import com.pws.pateast.fragment.student.MyStudentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 26-Apr-17.
 */

public class StudentFilterPresenter extends ClassPresenter<StudentFilterView> {

    public StudentFilterPresenter(boolean openDialog) {
        super(openDialog);
    }


    public List<MyStudentFragment> getStudentFragments(List<TeacherClass> myClasses) {
        List<MyStudentFragment> studentFragments = new ArrayList<>();

        for (TeacherClass aClass : myClasses) {
            Bundle bundle = new Bundle();
            bundle.putInt(Extras.CLASS_ID, aClass.getBcsMapId());
            bundle.putString(Extras.TITLE, getStudentFragmentTitle(aClass));
            bundle.putBoolean(Extras.ADD_USER, getView().isAddUser());
            studentFragments.add(AppFragment.newInstance(MyStudentFragment.class, null, bundle));
        }


        return studentFragments;
    }

    private String getStudentFragmentTitle(TeacherClass aClass) {
        return getString(R.string.class_name, aClass.getBcsmap().getBoard().getBoarddetails().get(0).getAlias(), aClass.getBcsmap().getClasses().getClassesdetails().get(0).getName(), aClass.getBcsmap().getSection().getSectiondetails().get(0).getName());
    }

}
