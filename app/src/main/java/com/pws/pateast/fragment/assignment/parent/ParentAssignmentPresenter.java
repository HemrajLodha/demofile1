package com.pws.pateast.fragment.assignment.parent;

import android.text.TextUtils;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.enums.TagType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.assignment.AssignmentDownloadPresenter;
import com.pws.pateast.fragment.assignment.AssignmentPresenter;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 15-May-17.
 */

public class ParentAssignmentPresenter extends AssignmentDownloadPresenter<ParentAssignmentView> {
    private Ward ward;
    private User user;
    private AssignmentPresenter assignmentPresenter;

    @Override
    public void attachView(ParentAssignmentView view) {
        super.attachView(view);
        ward = preference.getWard();
        user = preference.getUser();
        assignmentPresenter = new AssignmentPresenter();
        assignmentPresenter.attachView(view);
        assignmentPresenter.setOpenDialog(true);
    }

    public User getUser() {
        return user;
    }

    public void getSubjectsByStudent() {
        switch (UserType.getUserType(user.getData().getUser_type())) {
            case STUDENT:
                getView().getSubjects(user.getUserdetails().getBcsMapId(), user.getUserdetails().getUserId(),
                        user.getData().getMasterId(), user.getUserdetails().getAcademicSessionId());
                break;
            case PARENT:
                getView().getSubjects(ward.getBcsMapId(), ward.getUserId(),
                        ward.getMasterId(), ward.getAcademicSessionId());
                break;
        }
    }

    public void getTags() {
        switch (UserType.getUserType(user.getData().getUser_type())) {
            case STUDENT:
                getView().getTags(user.getData().getMasterId(), TagType.ASSIGNMENT.getValue());
                break;
            case PARENT:
                getView().getTags(ward.getMasterId(), TagType.ASSIGNMENT.getValue());
                break;
        }
    }

    public void getStudentAssignment(int subjectId, int tagId, String dueDate) {
        switch (UserType.getUserType(user.getData().getUser_type())) {
            case STUDENT:
                assignmentPresenter.getStudentAssignment(user.getUserdetails().getUserId(), user.getUserdetails().getBcsMapId(), user.getData().getUser_type(),
                        user.getUserdetails().getAcademicSessionId(), user.getData().getMasterId(), subjectId,tagId, dueDate);
                break;
            case PARENT:
                assignmentPresenter.getStudentAssignment(ward.getStudentId(), ward.getBcsMapId(), ward.getUser_type(),
                    ward.getAcademicSessionId(), ward.getMasterId(), subjectId, tagId, dueDate);
                break;
        }
    }
}
