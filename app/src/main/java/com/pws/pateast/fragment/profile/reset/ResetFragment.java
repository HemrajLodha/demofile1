package com.pws.pateast.fragment.profile.reset;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pws.pateast.R;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppView;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.ImageUtils;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.ProfileImageView;
import com.theartofdev.edmodo.cropper.CropImage;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;
import static com.pws.pateast.Constants.FILE_AUTHORITY;
import static com.pws.pateast.utils.MediaHelper.TYPE_IMAGE;

/**
 * Created by intel on 15-May-17.
 */

@RuntimePermissions
public class ResetFragment extends AppDialogFragment implements ResetView, View.OnClickListener {
    String TAG = ResetFragment.class.getSimpleName();

    private static final int REQUEST_IMAGE_CAPTURE = 2000;
    private static final int REQUEST_IMAGE_GALLERY = 2001;
    private Button btnResetUsername, btnResetPassword, btnUpdateProfile;

    private ProfileImageView imgProfile;

    private EditText etCurrentPassword, etNewPassword, etRepeatPassword;
    private EditText etUsername, etPassword;
    private EditText etName, etEmail, etMobile;

    private TextInputLayout tilCurrentPassword, tilNewPassword, tilRepeatPassword;
    private TextInputLayout tilName, tilEmail, tilMobile;

    private ResetPresenter presenter;
    private AppView profileView;


    public void attachView(AppView profileView) {
        this.profileView = profileView;
    }

    @Override
    protected int getContentLayout() {
        if (isResetPassword())
            return R.layout.fragment_reset_password;
        else if (isResetUsername())
            return R.layout.fragment_reset_username;
        else if (isResetProfile())
            return R.layout.fragment_reset_profile;
        else return 0;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        presenter = new ResetPresenter();
        presenter.attachView(this);
        setData();
    }

    private void setData() {
        if (isResetPassword()) {
            setTitle(R.string.change_password);
            etCurrentPassword = (EditText) findViewById(R.id.et_current_password);
            etNewPassword = (EditText) findViewById(R.id.et_new_password);
            etRepeatPassword = (EditText) findViewById(R.id.et_repeat_password);

            tilCurrentPassword = (TextInputLayout) findViewById(R.id.til_current_password);
            tilNewPassword = (TextInputLayout) findViewById(R.id.til_new_password);
            tilRepeatPassword = (TextInputLayout) findViewById(R.id.til_repeat_password);

            btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

            etCurrentPassword.addTextChangedListener(new OnTextChangedListener(etCurrentPassword.getId()));
            etNewPassword.addTextChangedListener(new OnTextChangedListener(etNewPassword.getId()));
            etRepeatPassword.addTextChangedListener(new OnTextChangedListener(etRepeatPassword.getId()));
            btnResetPassword.setOnClickListener(this);
        } else if (isResetUsername()) {
            setTitle(R.string.reset_username);

            etUsername = (EditText) findViewById(R.id.et_user_name);
            etPassword = (EditText) findViewById(R.id.et_password);

            etUsername.addTextChangedListener(new OnTextChangedListener(etUsername.getId()));
            etPassword.addTextChangedListener(new OnTextChangedListener(etPassword.getId()));

            btnResetUsername = (Button) findViewById(R.id.btn_reset_username);

            btnResetUsername.setOnClickListener(this);
        } else if (isResetProfile()) {
            setTitle(R.string.update_profile);

            imgProfile = (ProfileImageView) findViewById(R.id.img_profile);

            etName = (EditText) findViewById(R.id.et_name);
            etEmail = (EditText) findViewById(R.id.et_email);
            etMobile = (EditText) findViewById(R.id.et_mobile);

            tilName = (TextInputLayout) findViewById(R.id.til_name);
            tilEmail = (TextInputLayout) findViewById(R.id.til_email);
            tilMobile = (TextInputLayout) findViewById(R.id.til_mobile);

            btnUpdateProfile = (Button) findViewById(R.id.btn_update);

            presenter.setProfileData();

            etName.addTextChangedListener(new OnTextChangedListener(etName.getId()));
            etEmail.addTextChangedListener(new OnTextChangedListener(etEmail.getId()));
            etMobile.addTextChangedListener(new OnTextChangedListener(etMobile.getId()));

            btnUpdateProfile.setOnClickListener(this);
            imgProfile.setOnClickListener(this);
        }
    }

    @Override
    public boolean isResetPassword() {
        return getArguments().getBoolean(Extras.RESET_PASSWORD, false);
    }

    @Override
    public boolean isResetUsername() {
        return getArguments().getBoolean(Extras.RESET_USERNAME, false);
    }

    @Override
    public boolean isResetProfile() {
        return getArguments().getBoolean(Extras.RESET_PROFILE, false);
    }

    @Override
    public void setError(int id, String error) {
        switch (id) {
            case R.id.et_current_password:
                tilCurrentPassword.setError(error);
                tilCurrentPassword.setErrorEnabled(error != null);
                break;
            case R.id.et_new_password:
                tilNewPassword.setError(error);
                tilNewPassword.setErrorEnabled(error != null);
                break;
            case R.id.et_repeat_password:
                tilRepeatPassword.setError(error);
                tilRepeatPassword.setErrorEnabled(error != null);
                break;
            case R.id.et_user_name:
                etUsername.setError(error);
                break;
            case R.id.et_password:
                etPassword.setError(error);
                break;
            case R.id.et_name:
                tilName.setError(error);
                tilName.setErrorEnabled(error != null);
                break;
            case R.id.et_email:
                tilEmail.setError(error);
                tilEmail.setErrorEnabled(error != null);
                break;
            case R.id.et_mobile:
                tilMobile.setError(error);
                tilMobile.setErrorEnabled(error != null);
                break;

        }
    }

    @Override
    public void navigateToProfile(String message) {
        profileView.showDialog(getString(R.string.app_name), message, null, R.string.ok);
        dismiss();
    }

    @Override
    public void setProfileData(UserInfo profileData, UserType userType) {
        if (profileData != null) {
            ImageUtils.setImageUrl(getContext(), imgProfile, profileData.getUser_image(), R.drawable.user_placeholder);
            etName.setText(profileData.getUserdetails().get(0).getFullname());
            etEmail.setText(profileData.getEmail());
            etMobile.setText(profileData.getMobile());
        }
        etMobile.setFocusable(userType != UserType.PARENT);
        etMobile.setFocusableInTouchMode(userType != UserType.PARENT);
    }

    @Override
    public void openImageChooser() {
        selectImageDialog();
    }

    private void selectImageDialog() {
        String[] options = {getString(R.string.camera), getString(R.string.gallery)};
        DialogUtils.showSingleChoiceDialog(getContext(), getString(R.string.add_photo), options, 0, new AdapterListener<Integer>() {
            @Override
            public void onClick(int id, Integer value) {
                switch (value) {
                    case 0:
                        ResetFragmentPermissionsDispatcher.openCameraWithCheck(ResetFragment.this);
                        break;
                    case 1:
                        ResetFragmentPermissionsDispatcher.openGalleryWithCheck(ResetFragment.this);
                        break;
                }
            }
        });
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        presenter.setProfileImage(FileProvider.getUriForFile(getContext(), FILE_AUTHORITY, FileUtils.getOutputMediaFile(TYPE_IMAGE, true)));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, presenter.getProfileImage());
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void onClick(View v) {
        Utils.keyboard(getContext(), v, false);
        switch (v.getId()) {
            case R.id.btn_reset_password:
                if (presenter != null) {
                    presenter.validateResetPassword(etCurrentPassword.getText(), etNewPassword.getText(), etRepeatPassword.getText());
                }
                break;
            case R.id.btn_reset_username:
                if (presenter != null) {
                    presenter.validateResetUsername(etUsername.getText(), etPassword.getText());
                }
                break;
            case R.id.btn_update:
                if (presenter != null) {
                    presenter.validateResetProfile(etName.getText(), etEmail.getText(), etMobile.getText());
                }
                break;
            case R.id.img_profile:
                openImageChooser();
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    class OnTextChangedListener implements TextWatcher {
        int id;

        public OnTextChangedListener(int id) {
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setError(id, null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    CropImage.activity(presenter.getProfileImage())
                            .setFixAspectRatio(true)
                            .setOutputCompressQuality(10)
                            .start(getContext(), this);
                }
                break;
            case REQUEST_IMAGE_GALLERY:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getData() != null)
                        presenter.setProfileImage(data.getData());

                    CropImage.activity(presenter.getProfileImage())
                            .setFixAspectRatio(true)
                            .setOutputCompressQuality(10)
                            .start(getContext(), this);
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    presenter.setProfileImage(result.getUri());
                    ImageUtils.setImageUri(getContext(), imgProfile, presenter.getProfileImage(), R.drawable.user_placeholder);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ResetFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
