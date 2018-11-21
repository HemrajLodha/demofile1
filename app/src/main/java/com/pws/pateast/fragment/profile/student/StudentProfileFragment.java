package com.pws.pateast.fragment.profile.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.profile.ProfilePresenter;
import com.pws.pateast.fragment.profile.reset.ResetFragment;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.ImageUtils;
import com.pws.pateast.widget.ProfileDetailView;

import java.util.Locale;

/**
 * Created by intel on 05-Sep-17.
 */

public class StudentProfileFragment extends AppFragment implements StudentProfileView, View.OnClickListener {

    private ProfilePresenter profilePresenter;

    private ImageView imgProfile;
    private TextView tvName;
    private TextView tvInstituteName;
    private TextView tvPhone;

    protected ProfileDetailView rollno;
    protected ProfileDetailView username;
    protected ProfileDetailView class_name;
    protected ProfileDetailView enroll;
    protected ProfileDetailView dob;
    protected ProfileDetailView doj;
    protected ProfileDetailView birth_mark;
    protected ProfileDetailView height;
    protected ProfileDetailView gender;
    protected ProfileDetailView blood_group;
    protected ProfileDetailView address;
    protected ProfileDetailView country;
    protected ProfileDetailView state;
    protected ProfileDetailView city;
    protected ProfileDetailView father_name;
    protected ProfileDetailView father_contact;
    protected ProfileDetailView mother_name;
    protected ProfileDetailView mother_contact;

    @Override
    protected int getResourceLayout() {
        return R.layout.student_profile_fragment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(isStudent() ? R.string.menu_ward_profile : R.string.menu_my_profile);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvInstituteName = (TextView) findViewById(R.id.tv_institute_name);
        ((View) tvInstituteName.getParent()).setVisibility(View.VISIBLE);

        rollno = (ProfileDetailView) findViewById(R.id.rollno);
        username = (ProfileDetailView) findViewById(R.id.username);
        class_name = (ProfileDetailView) findViewById(R.id.class_name);
        enroll = (ProfileDetailView) findViewById(R.id.enroll);
        dob = (ProfileDetailView) findViewById(R.id.dob);
        doj = (ProfileDetailView) findViewById(R.id.doj);
        birth_mark = (ProfileDetailView) findViewById(R.id.birth_mark);
        height = (ProfileDetailView) findViewById(R.id.height);
        gender = (ProfileDetailView) findViewById(R.id.gender);
        blood_group = (ProfileDetailView) findViewById(R.id.blood_group);
        address = (ProfileDetailView) findViewById(R.id.address);
        country = (ProfileDetailView) findViewById(R.id.country);
        state = (ProfileDetailView) findViewById(R.id.state);
        city = (ProfileDetailView) findViewById(R.id.city);
        father_name = (ProfileDetailView) findViewById(R.id.father_name);
        father_contact = (ProfileDetailView) findViewById(R.id.father_contact);
        mother_name = (ProfileDetailView) findViewById(R.id.mother_name);
        mother_contact = (ProfileDetailView) findViewById(R.id.mother_contact);

        if (isStudent()) {
            username.setFontIconVisibility(false);
        } else {
            username.setFontIconVisibility(true);
            username.setOnIconClickListener(this);
        }

        profilePresenter = new ProfilePresenter();
        profilePresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        profilePresenter.getUserDetails(false);
    }

    @Override
    public void setData(User user, UserInfo userInfo) {
        tvName.setText(userInfo.getUserdetails().get(0).getFullname());
        tvPhone.setText(String.valueOf(userInfo.getMobile()));

        if (!TextUtils.isEmpty(user.getUserdetails().getInstitute_name())) {
            tvInstituteName.setText(user.getUserdetails().getInstitute_name());
        } else if (!TextUtils.isEmpty(userInfo.getInstitute_name())) {
            tvInstituteName.setText(userInfo.getInstitute_name());
        } else {
            tvInstituteName.setText(getString(R.string.not_available));
        }

        if (userInfo.getStudent() != null && !TextUtils.isEmpty(userInfo.getStudent().getStudentrecord().getRoll_no())) {
            rollno.setDetail(userInfo.getStudent().getStudentrecord().getRoll_no());
        } else {
            rollno.setDetail(getString(R.string.not_available));
        }

        username.setDetail(userInfo.getUser_name());

        if (userInfo.getStudent() != null) {
            class_name.setDetail(profilePresenter.getClassName(userInfo.getStudent().getStudentrecord().getBcsmap()));
            enroll.setDetail(userInfo.getStudent().getEnrollment_no());
            gender.setDetail(userInfo.getStudent().getGender());

            dob.setDetail(DateUtils.toDate(DateUtils.parse(userInfo.getStudent().getDob(), DateUtils.DATE_FORMAT_PATTERN), "yyyy-MM-dd", Locale.getDefault()));
            doj.setDetail(DateUtils.toDate(DateUtils.parse(userInfo.getStudent().getDoa(), DateUtils.DATE_FORMAT_PATTERN), "yyyy-MM-dd", Locale.getDefault()));

            String addressDetail = userInfo.getStudent().getStudentdetails().get(0).getAddress();
            address.setDetail(!TextUtils.isEmpty(addressDetail) ? addressDetail.trim() : "N/A");

            String countryDetail = userInfo.getStudent().getCountry().getCountrydetails().get(0).getName();
            country.setDetail(!TextUtils.isEmpty(countryDetail) ? countryDetail.trim() : "N/A");

            String stateDetail = userInfo.getStudent().getState().getStatedetails().get(0).getName();
            state.setDetail(!TextUtils.isEmpty(stateDetail) ? stateDetail.trim() : "N/A");

            String cityDetail = userInfo.getStudent().getCity().getCitydetails().get(0).getName();
            city.setDetail(!TextUtils.isEmpty(cityDetail) ? cityDetail.trim() : "N/A");

            String birthMark = userInfo.getStudent().getStudentdetails().get(0).getBirthmark();
            birth_mark.setDetail(!TextUtils.isEmpty(birthMark) ? birthMark.trim() : "N/A");

            String heightDetail = userInfo.getStudent().getStudentdetails().get(0).getHeight();
            height.setDetail(!TextUtils.isEmpty(heightDetail) ? heightDetail.trim() : "N/A");

            String bloodGroup = userInfo.getStudent().getBlood_group();
            blood_group.setDetail(!TextUtils.isEmpty(bloodGroup) ? bloodGroup.trim() : "N/A");

            String fatherName = userInfo.getStudent().getStudentdetails().get(0).getFather_name();
            father_name.setDetail(!TextUtils.isEmpty(fatherName) ? fatherName.trim() : "N/A");

            String fatherContact = userInfo.getStudent().getFather_contact();
            father_contact.setDetail(!TextUtils.isEmpty(fatherContact) ? fatherContact.trim() : "N/A");

            String motherName = userInfo.getStudent().getStudentdetails().get(0).getMother_name();
            mother_name.setDetail(!TextUtils.isEmpty(motherName) ? motherName.trim() : "N/A");

            String motherContact = userInfo.getStudent().getMother_contact();
            mother_contact.setDetail(!TextUtils.isEmpty(motherContact) ? motherContact.trim() : "N/A");
        }


        ImageUtils.setImageUrl(getContext(), imgProfile, userInfo.getUser_image(), R.drawable.user_placeholder);
    }

    @Override
    public boolean loadFromPreference() {
        return !isStudent();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.username:
                ResetFragment resetFragment = AppDialogFragment.newInstance(ResetFragment.class, getAppListener());
                resetFragment.getArguments().putBoolean(Extras.RESET_USERNAME, true);
                resetFragment.attachView(this);
                resetFragment.show(getFragmentManager(), ResetFragment.class.getSimpleName());
                break;
        }
    }

    @Override
    public void showDialog(String title, String message, AdapterListener<String> listener, int... args) {
        super.showDialog(title, message, new AdapterListener<String>() {
            @Override
            public void onClick(int id, String value) {
                onActionClick();
            }
        }, args);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (profilePresenter != null)
            profilePresenter.detachView();
    }

    @Override
    public boolean isStudent() {
        return getArguments().getBoolean(AppFragment.EXTRA_DATA, false);
    }
}
