package com.pws.pateast.fragment.feeds.add;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.activity.feeds.FeedsActivity;
import com.pws.pateast.adapter.ClassAdapter;
import com.pws.pateast.adapter.TeacherAdapter;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.MediaHelper;
import com.pws.pateast.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;

@RuntimePermissions
public class AddFeedsFragment extends AppFragment implements AddFeedsView {
    private EditText etPost;
    private TextView  tvSelectClasses, tvSelectTeacher;
    private CheckBox checkAll;
    private RecyclerView rvFiles;
    private LinearLayoutManager llm;
    private View viewAddFile;
    private Button btnSubmit;

    private AddFeedsPresenter mPresenter;
    private ListPopupWindow popUpClasses, popUpTeacher;
    private ClassAdapter myClassesAdapter;
    private TeacherAdapter myTeacherAdapter;
    private AddFileAdapter mFileAdapter;
    private MediaHelper mediaHelper;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_add_feed;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.title_add_feed);
        etPost = (EditText) findViewById(R.id.et_post);
        rvFiles = (RecyclerView) findViewById(R.id.rv_files);
        llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFiles.setLayoutManager(llm);
        //rvFiles.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        viewAddFile = findViewById(R.id.view_add_file);
        tvSelectClasses = (TextView) findViewById(R.id.tv_select_classes);
        tvSelectTeacher = (TextView) findViewById(R.id.tv_select_teacher);
        checkAll = (CheckBox) findViewById(R.id.check_all);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        tvSelectClasses.setOnClickListener(this);
        tvSelectTeacher.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        viewAddFile.setOnClickListener(this);

        mediaHelper = new MediaHelper(getContext());
        mPresenter = new AddFeedsPresenter(true);
        mPresenter.attachView(this);
        mPresenter.getMyClasses();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void setClass(TeacherClass classes) {
        tvSelectClasses.setTag(classes);
        if (classes != null) {
            tvSelectClasses.setText(myClassesAdapter.getClassName(classes));
        } else {
            tvSelectClasses.setText(R.string.hint_select_classes);
        }
    }

    @Override
    public void setClassAdapter(List<TeacherClass> classes) {
        classes.add(0, new TeacherClass(0));
        if (popUpClasses == null) {
            popUpClasses = new ListPopupWindow(getContext());
            popUpClasses.setAnchorView(tvSelectClasses);
            popUpClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TeacherClass item = null;
                    if (myClassesAdapter != null) {
                        item = myClassesAdapter.getItem(position);
                    }
                    setClass(item);
                    popUpClasses.dismiss();
                }
            });
        }
        if (myClassesAdapter == null) {
            myClassesAdapter = new ClassAdapter(getContext());
        }
        myClassesAdapter.update(classes);
        popUpClasses.setAdapter(myClassesAdapter);
    }

    @Override
    public void setTeacher(UserInfo teacher) {
        tvSelectTeacher.setTag(teacher);
        if (teacher != null) {
            tvSelectTeacher.setText(myTeacherAdapter.getTeacherName(teacher));
        } else {
            tvSelectTeacher.setText(R.string.hint_select_teacher);
        }
    }

    @Override
    public void setTeacherAdapter(List<UserInfo> teachers) {
        if (popUpTeacher == null) {
            popUpTeacher = new ListPopupWindow(getContext());
            popUpTeacher.setAnchorView(tvSelectTeacher);
            popUpTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UserInfo item = null;
                    if (myTeacherAdapter != null) {
                        item = myTeacherAdapter.getItem(position);
                    }
                    setTeacher(item);
                    popUpTeacher.dismiss();
                }
            });
        }
        if (myTeacherAdapter == null) {
            myTeacherAdapter = new TeacherAdapter(getContext());
        }
        myTeacherAdapter.update(teachers);
        popUpTeacher.setAdapter(myTeacherAdapter);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @Override
    public void openCamera(int media) {
        Intent intent = null;
        if (media == 0) {
            intent = mediaHelper.dispatchTakePictureIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_TAKE_PHOTO);
            else
                Utils.showToast(getContext(), R.string.no_camera_exists);
        } else if (media == 1) {
            intent = mediaHelper.dispatchTakeVideoIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_TAKE_VIDEO);
            else
                Utils.showToast(getContext(), R.string.no_camera_exists);
        }
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @Override
    public void openGallery(int media) {
        Intent intent = null;
        if (media == 2) {
            intent = mediaHelper.dispatchPickPictureIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_PICK_PHOTO);
            else
                Utils.showToast(getContext(), R.string.no_gallery_exists);
        } else if (media == 3) {
            intent = mediaHelper.dispatchPickVideoIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_PICK_VIDEO);
            else
                Utils.showToast(getContext(), R.string.no_gallery_exists);
        }
    }

    @Override
    public boolean isError() {
        boolean isError = false;
        if (TextUtils.isEmpty(getDescription())) {
            showMessage(getString(R.string.validate_edittext, getString(R.string.hint_description)), true, R.attr.colorPrimary);
            isError = true;
        } else if (mPresenter.getUserType() == UserType.TEACHER && getTeacherClass() == null) {
            showMessage(getString(R.string.validate_spinner, getString(R.string.title_class)), true, R.attr.colorPrimary);
            isError = true;
        } else if (mPresenter.getUserType() != UserType.TEACHER && getTeacher() == null) {
            showMessage(getString(R.string.validate_spinner, getString(R.string.title_teacher)), true, R.attr.colorPrimary);
            isError = true;
        }
        return isError;
    }

    @Override
    public CharSequence getDescription() {
        return etPost.getText();
    }

    @Override
    public int getBcsMapID(UserType userType) {
        switch (userType) {
            case TEACHER:
                TeacherClass aClass = getTeacherClass();
                return aClass == null ? 0 : aClass.getBcsMapId();
            case STUDENT:
            case PARENT:
                return checkAll.isChecked() ? 0 : -1;
        }
        return 0;
    }

    @Override
    public List<Feeds> getFiles() {
        return mFileAdapter == null ? new ArrayList<>() : mFileAdapter.getDatas();
    }

    @Override
    public void onFeedPosted(String message) {
        Utils.showToast(getContext(), message);
        getActivity().setResult(FeedsActivity.ADD_FEED_RESPONSE);
        getActivity().finish();
    }

    @Override
    public void showClasses(boolean show) {
        ((LinearLayout) tvSelectClasses.getParent()).setVisibility(show ? View.VISIBLE : View.GONE);
        ((LinearLayout) tvSelectTeacher.getParent()).setVisibility(show ? View.GONE : View.VISIBLE);
        checkAll.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public TeacherClass getTeacherClass() {
        return (TeacherClass) tvSelectClasses.getTag();
    }

    @Override
    public UserInfo getTeacher() {
        return (UserInfo) tvSelectTeacher.getTag();
    }

    @Override
    public void onCompressStart() {
        showProgressDialog(getString(R.string.preparing));
    }

    @Override
    public void onCompressComplete() {
        hideProgressDialog();
    }

    @Override
    public void uploadFile(MessageType messageType, String filePath) {
        if (filePath != null) {
            if (rvFiles.getAdapter() == null) {
                mFileAdapter = new AddFileAdapter(getContext(), this);
                rvFiles.setAdapter(mFileAdapter);
            }
            Feeds feeds = new Feeds();
            feeds.setFileUri(Uri.fromFile(new File(filePath)));
            feeds.setFeedType(mediaHelper.getMessageType(filePath).getValue());
            mFileAdapter.add(feeds);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.img_delete:
                mFileAdapter.remove(position);
                break;
        }
    }

    @Override
    public void hideProgressDialog() {
        super.hideProgressDialog();
        btnSubmit.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_classes:
                if (popUpClasses != null) {
                    popUpClasses.show();
                }
                break;
            case R.id.tv_select_teacher:
                if (popUpTeacher != null) {
                    popUpTeacher.show();
                }
                break;
            case R.id.view_add_file:
                mPresenter.chooseMediaFileDialog(mFileAdapter == null ? 0 : mFileAdapter.getItemCount());
                break;
            case R.id.btn_submit:
                btnSubmit.setEnabled(mPresenter.postFeed());
                break;
        }
    }

    @Override
    public void onClick(int id, Integer value) {
        switch (value) {
            case 0:
            case 1:
                AddFeedsFragmentPermissionsDispatcher.openCameraWithCheck(this, value);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                AddFeedsFragmentPermissionsDispatcher.openGalleryWithCheck(this, value);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MediaHelper.REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    CropImage.activity(mediaHelper.getUri())
                            .setFixAspectRatio(true)
                            .setOutputCompressQuality(100)
                            .start(getContext(), this);
                }
                break;
            case MediaHelper.REQUEST_PICK_PHOTO:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    CropImage.activity(data.getData())
                            .setFixAspectRatio(true)
                            .setOutputCompressQuality(100)
                            .start(getContext(), this);
                }
                break;
            case MediaHelper.REQUEST_TAKE_VIDEO:
                if (resultCode == RESULT_OK) {
                    mPresenter.handleFiles(mediaHelper.getMessageType(getContext(), mediaHelper.getUri()), mediaHelper.getUri());
                }
                break;
            case MediaHelper.REQUEST_PICK_VIDEO:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    mPresenter.handleFiles(mediaHelper.getMessageType(getContext(), data.getData()), mediaHelper.getUri());
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    mPresenter.handleFiles(mediaHelper.getMessageType(getContext(), result.getUri()), mediaHelper.getUri());
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddFeedsFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
