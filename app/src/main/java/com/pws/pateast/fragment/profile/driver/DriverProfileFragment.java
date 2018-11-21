package com.pws.pateast.fragment.profile.driver;

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

/**
 * Created by intel on 07-Sep-17.
 */

public class DriverProfileFragment extends AppFragment implements DriverProfileView, View.OnClickListener {
    private ProfilePresenter profilePresenter;

    private ImageView imgProfile;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvInstituteName;
    private TextView tvPhone;

    protected ProfileDetailView username;
    protected ProfileDetailView address;
    protected ProfileDetailView licenceNumber;
    protected ProfileDetailView licenceDate;

    @Override
    protected int getResourceLayout() {
        return R.layout.driver_profile_fragment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_my_profile);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvInstituteName = (TextView) findViewById(R.id.tv_institute_name);
        ((View) tvEmail.getParent()).setVisibility(View.VISIBLE);
        ((View) tvInstituteName.getParent()).setVisibility(View.VISIBLE);

        username = (ProfileDetailView) findViewById(R.id.username);
        address = (ProfileDetailView) findViewById(R.id.address);
        licenceNumber = (ProfileDetailView) findViewById(R.id.licence_number);
        licenceDate = (ProfileDetailView) findViewById(R.id.licence_date);

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
        tvName.setText(userInfo.getUserdetails().get(0).getFullname());
        tvPhone.setText(String.valueOf(userInfo.getMobile()));
        tvEmail.setText(TextUtils.isEmpty(userInfo.getEmail()) ? getString(R.string.not_available) : userInfo.getEmail());
        tvInstituteName.setText(TextUtils.isEmpty(userInfo.getInstitute_name()) ? getString(R.string.not_available) : userInfo.getInstitute_name());

        username.setDetail(userInfo.getUser_name());


        String addressDetail = userInfo.getUserdetails().get(0).getAddress();
        String licenceNumberDetail = userInfo.getGovt_identity_number();
        String licenceDateDetail = userInfo.getGovt_identity_expiry();
        address.setDetail(!TextUtils.isEmpty(addressDetail) ? addressDetail.trim() : "N/A");
        licenceNumber.setDetail(!TextUtils.isEmpty(licenceNumberDetail) ? licenceNumberDetail.trim() : "N/A");
        licenceDate.setDetail(!TextUtils.isEmpty(licenceDateDetail) ? DateUtils.toDate(DateUtils.parse(licenceDateDetail.trim(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM, yyyy") : "N/A");

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
