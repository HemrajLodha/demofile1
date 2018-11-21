package com.pws.pateast.fragment.feeds.add;

import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.presenter.ClassView;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.service.upload.task.UploadView;

import java.util.List;

public interface AddFeedsView extends ClassView, UploadView, View.OnClickListener, AdapterListener<Integer>, BaseRecyclerAdapter.OnItemClickListener {

    void openCamera(int media);

    void openGallery(int media);

    boolean isError();

    CharSequence getDescription();

    int getBcsMapID(UserType userType);

    List<Feeds> getFiles();

    void onFeedPosted(String message);

    void showClasses(boolean show);

    TeacherClass getTeacherClass();

    UserInfo getTeacher();

    void setTeacher(UserInfo teacher);

    void setTeacherAdapter(List<UserInfo> teachers);
}
