package com.pws.pateast.fragment.assignment.teacher.add;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.adapter.ClassAdapter;
import com.pws.pateast.adapter.SubjectAdapter;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.listener.OnDateChangeListener;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.datepickerview.DatePickerEditText;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;
import static com.pws.pateast.Constants.FILE_AUTHORITY;
import static com.pws.pateast.utils.DateUtils.SERVER_DATE_TEMPLATE;
import static com.pws.pateast.utils.MediaHelper.TYPE_IMAGE;


/**
 * Created by planet on 5/3/2017.
 */

@RuntimePermissions
public class AddAssignmentFragment extends AppFragment implements AddAssignmentView, View.OnClickListener {
    String TAG = AddAssignmentFragment.class.getSimpleName();

    private static final int MAX_FILE_SIZE_IN_MB = 20;
    private static final int REQUEST_IMAGE_CAPTURE = 2000;

    private static final int CHOOSE_FILE_REQUEST_CODE = 101;
    public static final int ADD_REQUEST = 1001;
    public static final int ADD_RESPONSE = 1002;
    private Button btnSubmit;
    private TextView tvSelectClasses, tvSelectSubject;
    private View viewUploadAssignment;
    private DatePickerEditText etStartDate;
    private DatePickerEditText etEndDate;
    private EditText etComment;
    private EditText etTitle;
    private TextView etUploadAssignment;
    private ClassAdapter myClassesAdapter;
    private SubjectAdapter subjectAdapter;
    private boolean isUpdate;
    private List<String> allowedMimeTypes;

    private AddAssignmentPresenter mPresenter;

    private ListPopupWindow popUpClasses, popUpSubject;


    private OnDateChangeListener onStartDateChangeListener = new OnDateChangeListener() {
        @Override
        public void onDateChange(View view, Calendar time, String timeString) {
            Calendar calendar = (Calendar) time.clone();
            if (etEndDate.getDate() != null && etEndDate.getDate().getTime().before(time.getTime())) {
                etEndDate.setText("");
            }
            etEndDate.setMinDate(calendar.getTime());
        }
    };

    private OnDateChangeListener onEndDateChangeListener = new OnDateChangeListener() {
        @Override
        public void onDateChange(View view, Calendar time, String timeString) {

        }
    };

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_add_assignment_new;
    }


    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.upload_assignment);
        mPresenter = new AddAssignmentPresenter(true);
        tvSelectClasses = (TextView) findViewById(R.id.tv_select_classes);
        tvSelectSubject = (TextView) findViewById(R.id.tv_select_subject);
        etStartDate = (DatePickerEditText) findViewById(R.id.et_start_date);
        etEndDate = (DatePickerEditText) findViewById(R.id.et_end_date);
        etTitle = (EditText) findViewById(R.id.et_assignment_title);
        etComment = (EditText) findViewById(R.id.et_comment);
        etUploadAssignment = (TextView) findViewById(R.id.tv_select_file);
        viewUploadAssignment = findViewById(R.id.view_upload_assignment);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        viewUploadAssignment.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        etStartDate.setManager(getFragmentManager(), onStartDateChangeListener);
        etStartDate.setMinDate(Calendar.getInstance().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        etEndDate.setManager(getFragmentManager(), onEndDateChangeListener);
        etEndDate.setMinDate(calendar.getTime());
        tvSelectClasses.setOnClickListener(this);
        tvSelectSubject.setOnClickListener(this);

        if (getArguments() != null) {
            Assignment assignment = getArguments().getParcelable(EXTRA_DATA);
            mPresenter.setAssignment(assignment);
        }

        mPresenter.attachView(this);

        onActionClick();
        mPresenter.getMyClasses();
        allowedMimeTypes = Utils.getAllowedFileMimeTypes(getContext());
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
    }

    @Override
    public void openDialog(String message) {
        Utils.showToast(getContext(), message);
        getActivity().setResult(ADD_RESPONSE);
        getActivity().finish();
    }

    @Override
    public void setData() {
        Assignment assignment = mPresenter.getAssignment();
        isUpdate = true;
        etTitle.setText(assignment.getAssignmentdetails().get(0).getTitle());
        etComment.setText(assignment.getAssignmentdetails().get(0).getComment());
        etUploadAssignment.setText(assignment.getAssignment_file_name());
        mPresenter.setDate(R.id.et_start_date, assignment.getStart_date());
        mPresenter.setDate(R.id.et_end_date, assignment.getEnd_date());
    }

    @Override
    public void setDate(int id, String date) {
        switch (id) {
            case R.id.et_start_date:
                etStartDate.setText(date);
                break;
            case R.id.et_end_date:
                etEndDate.setText(date);
                break;
        }
    }

    @Override
    public void setCalenderDate(Date minDate, Date maxDate) {
        etStartDate.setMinDate(minDate);
        etStartDate.setMaxDate(maxDate);
        etEndDate.setMinDate(minDate);
        etEndDate.setMaxDate(maxDate);
    }

    @Override
    public void setDateFormat(String dateFormat) {
        etStartDate.setDateFormat(dateFormat);
        etEndDate.setDateFormat(dateFormat);
    }

    @Override
    public void setFileData() {
        String mimeType = FileUtils.getMimeType(mPresenter.getUploadedFileName());
        if (allowedMimeTypes.contains(mimeType)) {
            if (mPresenter.getUploadedFileSizeInMb() > MAX_FILE_SIZE_IN_MB) {
                Utils.showToast(getContext(), getString(R.string.validate_file_size, String.valueOf(MAX_FILE_SIZE_IN_MB)));
                mPresenter.setSelectedFileUri(null);
            } else {
                etUploadAssignment.setText(mPresenter.getUploadedFileName());
            }
        } else {
            Utils.showToast(getContext(), getString(R.string.validate_file_type));
            mPresenter.setSelectedFileUri(null);
        }
    }

    @Override
    public void onSubmit(boolean isUpdate) {
        boolean isError = false;
        if (mPresenter.getClasses() == null) {
            isError = true;
            mPresenter.validateFormData(R.id.tv_select_subject, getString(R.string.validate_spinner, getString(R.string.title_class)));
        }

        if (!isError && mPresenter.getSubject() == null) {
            isError = true;
            mPresenter.validateFormData(R.id.tv_select_subject, getString(R.string.validate_spinner, getString(R.string.title_subject)));
        }

        if (!isError && TextUtils.isEmpty(etTitle.getText())) {
            isError = true;
            mPresenter.validateFormData(R.id.et_assignment_title, getString(R.string.validate_edittext, getString(R.string.hint_assignment_title)));
        }

        if (!isError && TextUtils.isEmpty(etStartDate.getText())) {
            isError = true;
            mPresenter.validateFormData(R.id.et_start_date, getString(R.string.validate_date_time_picker, getString(R.string.hint_start_date)));
        }

        if (!isError && TextUtils.isEmpty(etEndDate.getText())) {
            isError = true;
            mPresenter.validateFormData(R.id.et_end_date, getString(R.string.validate_date_time_picker, getString(R.string.hint_end_date)));
        }

        if (!isError) {
            long startDate = DateUtils.getDateTimestamp(etStartDate.getDate());
            long endDate = DateUtils.getDateTimestamp(etEndDate.getDate());
            if (startDate > endDate) {
                isError = true;
                mPresenter.validateFormData(R.id.et_end_date, getString(R.string.validate_between_date));
            } else {
                mPresenter.validateFormData(R.id.et_end_date, null);
            }
        }

        /*if (!isError && mPresenter.getSelectedFileUri() == null && !isUpdate) {
            isError = true;
            mPresenter.validateFormData(R.id.tv_select_file, getString(R.string.validate_file, getString(R.string.title_file)));
        } else {
            mPresenter.validateFormData(R.id.tv_select_file, null);
        }*/

        if (!isError && TextUtils.isEmpty(etComment.getText())) {
            isError = true;
            mPresenter.validateFormData(R.id.et_comment, getString(R.string.validate_edittext, getString(R.string.hint_assignment_comment)));
        } else {
            mPresenter.validateFormData(R.id.et_comment, null);
        }

        if (!isError) {
            mPresenter.saveAssignment(isUpdate,
                    etStartDate.getDateString(SERVER_DATE_TEMPLATE),
                    etEndDate.getDateString(SERVER_DATE_TEMPLATE),
                    etTitle.getText().toString(),
                    etComment.getText().toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    CropImage.activity(mPresenter.getImageUri())
                            .setFixAspectRatio(true)
                            .setOutputCompressQuality(10)
                            .start(getContext(), this);
                }
                break;
            case CHOOSE_FILE_REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        mPresenter.setFileData(data.getData());
                    }
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    mPresenter.setFileData(result.getUri());
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AddAssignmentFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPresenter.setImageUri(FileProvider.getUriForFile(getContext(), FILE_AUTHORITY, FileUtils.getOutputMediaFile(TYPE_IMAGE, true)));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPresenter.getImageUri());
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openGallery() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent(true);
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, CHOOSE_FILE_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public void setError(int id, String error) {
        if (error != null) {
            showMessage(error, true, R.attr.colorPrimary);
        }
    }

    @Override
    public void setClass(TeacherClass classes) {
        if (classes != null) {
            tvSelectClasses.setText(myClassesAdapter.getClassName(classes));
            mPresenter.validateFormData(R.id.sp_class, null);
            mPresenter.setClasses(classes);
            mPresenter.getSubject(String.valueOf(mPresenter.getClasses().getBcsMapId()));
        } else {
            tvSelectClasses.setText(R.string.hint_select_classes);
            mPresenter.setClasses(null);
        }
    }

    @Override
    public void setClassAdapter(final List<TeacherClass> classes) {
        if (popUpClasses == null) {
            popUpClasses = new ListPopupWindow(getContext());
            popUpClasses.setAnchorView(tvSelectClasses);
            popUpClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (myClassesAdapter != null) {
                        TeacherClass item = myClassesAdapter.getItem(position);
                        setClass(item);
                        setSubject(null);
                    } else {
                        setClass(null);
                        setSubject(null);
                        setSubjectAdapter(new ArrayList<Subject>());
                    }
                    popUpClasses.dismiss();
                }
            });
        }
        if (myClassesAdapter == null) {
            myClassesAdapter = new ClassAdapter(getContext());

        }
        myClassesAdapter.update(classes);
        popUpClasses.setAdapter(myClassesAdapter);

        if (isUpdate) {
            final TeacherClass myClasses = new TeacherClass();
            myClasses.setBcsMapId(mPresenter.getAssignment().getBcsMapId());
            setClass(myClassesAdapter.getClass(classes.indexOf(myClasses)));
        }
    }

    @Override
    public void setSubject(Subject subject) {
        if (subject != null) {
            tvSelectSubject.setText(subjectAdapter.getSubjectName(subject));
            mPresenter.validateFormData(R.id.sp_subject, null);
            mPresenter.setSubject(subject);
        } else {
            tvSelectSubject.setText(R.string.hint_select_subject);
            mPresenter.setSubject(null);
        }

    }

    @Override
    public void setSubjectAdapter(List<Subject> subjects) {
        if (popUpSubject == null) {
            popUpSubject = new ListPopupWindow(getContext());
            popUpSubject.setAnchorView(tvSelectSubject);
            popUpSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (subjectAdapter != null) {
                        Subject item = subjectAdapter.getItem(position);
                        setSubject(item);
                    } else {
                        setSubject(null);
                    }
                    popUpSubject.dismiss();
                }
            });
        }
        if (subjectAdapter == null) {
            subjectAdapter = new SubjectAdapter(getContext());

        }
        subjectAdapter.update(subjects);
        popUpSubject.setAdapter(subjectAdapter);

        if (isUpdate) {
            Subject subject = new Subject(mPresenter.getAssignment().getSubjectId());
            setSubject(subjectAdapter.getSubject(subjects.indexOf(subject)));
        } else {
            //setSubject(new Subject(0));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_classes:
                if (popUpClasses != null) {
                    popUpClasses.show();
                }
                break;
            case R.id.tv_select_subject:
                if (popUpSubject != null) {
                    popUpSubject.show();
                }
                break;
            case R.id.view_upload_assignment:
                selectImageDialog();
                break;
            case R.id.btn_submit:
                mPresenter.onSubmit();
                break;
        }
    }

    @Override
    public void setStudent(Student student) {

    }

    @Override
    public void setStudentAdapter(List<Student> students) {

    }

    private void selectImageDialog() {
        String[] options = {getString(R.string.camera), getString(R.string.gallery)};
        DialogUtils.showSingleChoiceDialog(getContext(), getString(R.string.add_photo), options, 0, new AdapterListener<Integer>() {
            @Override
            public void onClick(int id, Integer value) {
                switch (value) {
                    case 0:
                        AddAssignmentFragmentPermissionsDispatcher.openCameraWithCheck(AddAssignmentFragment.this);
                        break;
                    case 1:
                        AddAssignmentFragmentPermissionsDispatcher.openGalleryWithCheck(AddAssignmentFragment.this);
                        break;
                }
            }
        });
    }

}
