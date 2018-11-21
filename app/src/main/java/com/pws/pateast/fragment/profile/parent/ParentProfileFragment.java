package com.pws.pateast.fragment.profile.parent;

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
import com.pws.pateast.utils.ImageUtils;

/**
 * Created by intel on 07-Sep-17.
 */

public class ParentProfileFragment extends AppFragment implements ParentProfileView {
    private ProfilePresenter profilePresenter;

    private ImageView imgProfile;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvInstituteName;


    @Override
    protected int getResourceLayout() {
        return R.layout.parent_profile_fragment;
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
        tvInstituteName.setText(!TextUtils.isEmpty(user.getUserdetails().getInstitute_name()) ?
                user.getUserdetails().getInstitute_name() : getString(R.string.not_available));
        tvPhone.setText(TextUtils.isEmpty(String.valueOf(userInfo.getMobile())) ? getString(R.string.not_available) :
                String.valueOf(userInfo.getMobile()));
        tvEmail.setText(TextUtils.isEmpty(userInfo.getEmail()) ? getString(R.string.not_available) : userInfo.getEmail());
        ImageUtils.setImageUrl(getContext(), imgProfile, userInfo.getUser_image(), R.drawable.user_placeholder);
    }

    @Override
    public boolean loadFromPreference() {
        return true;
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
