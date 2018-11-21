package com.pws.pateast.fragment.profile.teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
 * Created by planet on 8/31/2017.
 */

public class TeacherProfileFragment extends AppFragment implements TeacherProfileView, View.OnClickListener {

    private ProfilePresenter profilePresenter;

    private ImageView imgProfile;
    private TextView tvName;
    private TextView tvInstituteName;
    private TextView tvPhone;
    protected ProfileDetailView username;
    protected ProfileDetailView dob;
    protected ProfileDetailView doj;
    protected ProfileDetailView address;
    protected ProfileDetailView country;
    protected ProfileDetailView state;
    protected ProfileDetailView city;
    protected ProfileDetailView qualification;
    protected ProfileDetailView subject;

    @Override
    protected int getResourceLayout() {
        return R.layout.teacher_profile_fragment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_my_profile);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvInstituteName = (TextView) findViewById(R.id.tv_institute_name);
        ((View) tvInstituteName.getParent()).setVisibility(View.VISIBLE);

        username = (ProfileDetailView) findViewById(R.id.username);
        dob = (ProfileDetailView) findViewById(R.id.dob);
        doj = (ProfileDetailView) findViewById(R.id.doj);
        address = (ProfileDetailView) findViewById(R.id.address);
        country = (ProfileDetailView) findViewById(R.id.country);
        state = (ProfileDetailView) findViewById(R.id.state);
        city = (ProfileDetailView) findViewById(R.id.city);
        qualification = (ProfileDetailView) findViewById(R.id.qualification);
        subject = (ProfileDetailView) findViewById(R.id.subject);

        username.setOnIconClickListener(this);

        profilePresenter = new ProfilePresenter();
        profilePresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        profilePresenter.getUserDetails(false);
    }

    @Override
    protected boolean hasOptionMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset_profile:
                ResetFragment resetFragment = AppDialogFragment.newInstance(ResetFragment.class, getAppListener());
                resetFragment.getArguments().putBoolean(Extras.RESET_PROFILE, true);
                resetFragment.attachView(this);
                resetFragment.show(getFragmentManager(), ResetFragment.class.getSimpleName());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setData(User user, UserInfo userInfo) {
        String salutation = (userInfo.getSalutation() != null && !TextUtils.isEmpty(userInfo.getSalutation())) ?
                (userInfo.getSalutation() + ". ") : "";
        tvName.setText(salutation + userInfo.getUserdetails().get(0).getFullname());
        tvPhone.setText(String.valueOf(userInfo.getMobile()));
        tvInstituteName.setText(user.getUserdetails().getInstitute_name());

        username.setDetail(userInfo.getUser_name());

        if (!TextUtils.isEmpty(userInfo.getTeacher().getDob())) {
            dob.setDetail(DateUtils.toDate(DateUtils.parse(userInfo.getTeacher().getDob(), DateUtils.DATE_FORMAT_PATTERN), "yyyy-MM-dd", Locale.getDefault()));
        } else {
            dob.setDetail(getString(R.string.not_available));
        }

        if (!TextUtils.isEmpty(userInfo.getTeacher().getJoin_date())) {
            doj.setDetail(DateUtils.toDate(DateUtils.parse(userInfo.getTeacher().getJoin_date(), DateUtils.DATE_FORMAT_PATTERN), "yyyy-MM-dd", Locale.getDefault()));
        } else {
            doj.setDetail(getString(R.string.not_available));
        }


        String addressDetail = userInfo.getTeacher().getTeacherdetails().get(0).getAddress();
        address.setDetail(!TextUtils.isEmpty(addressDetail) ? addressDetail.trim() : "N/A");

        String countryDetail = userInfo.getTeacher().getCountry().getCountrydetails().get(0).getName();
        country.setDetail(!TextUtils.isEmpty(countryDetail) ? countryDetail.trim() : "N/A");

        String stateDetail = userInfo.getTeacher().getState().getStatedetails().get(0).getName();
        state.setDetail(!TextUtils.isEmpty(stateDetail) ? stateDetail.trim() : "N/A");

        String cityDetail = userInfo.getTeacher().getCity().getCitydetails().get(0).getName();
        city.setDetail(!TextUtils.isEmpty(cityDetail) ? cityDetail.trim() : "N/A");

        String qualificationDetail = userInfo.getTeacher().getTeacherdetails().get(0).getLast_qualification();
        qualification.setDetail(!TextUtils.isEmpty(qualificationDetail) ? qualificationDetail.trim() : "N/A");

        String subjectDetail = profilePresenter.getSubjects(userInfo.getTeacher().getTeachersubjects());
        subject.setDetail(!TextUtils.isEmpty(subjectDetail) ? subjectDetail.trim() : "N/A");

        ImageUtils.setImageUrl(getContext(), imgProfile, userInfo.getUser_image(), R.drawable.user_placeholder);
    }

    @Override
    public boolean loadFromPreference() {
        return true;
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
}
