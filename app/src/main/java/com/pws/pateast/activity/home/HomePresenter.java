package com.pws.pateast.activity.home;

import com.pws.pateast.utils.Preference;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.home.DashBoardFragment;
import com.pws.pateast.fragment.home.parent.ParentFragment;
import com.pws.pateast.fragment.home.student.StudentDashBoardFragment;
import com.pws.pateast.fragment.home.teacher.TeacherDashBoardFragment;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 20-Apr-17.
 */

public class HomePresenter extends AppPresenter<HomeView>
{
    @Inject
    ServiceBuilder serviceBuilder;

    @Inject
    Preference preference;

    private User user;

    private APIService apiService;

    private HomeView mHomeView;

    @Override
    public HomeView getView()
    {
        return mHomeView;
    }

    @Override
    public void attachView(HomeView mHomeView)
    {
        this.mHomeView = mHomeView;
        getComponent().inject(this);
        user = preference.getUser().getData();
        getView().getAppListener().bindSocketService();
    }



    public void getUserDetails()
    {
        if(preference.getUserInfo() == null)
        {
            apiService = serviceBuilder.createService(APIService.class);

            HashMap<String,String> params = serviceBuilder.getParams();
            params.put("id", String.valueOf(user.getId()));
            params.put("user_type", user.getUser_type());

            disposable = apiService.getUserProfile(params)
                    .subscribeOn(getApp().getSubscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new AppSingleObserver<UserInfo>()
                    {
                        @Override
                        public void onResponse(UserInfo response)
                        {
                            preference.setUserInfo(response);
                            setHomeFragment();
                        }

                        @Override
                        public HomePresenter getPresenter()
                        {
                            return HomePresenter.this;
                        }
                    });
        }
        else
        {
            mHomeView.showProgress(false);
            setHomeFragment();
        }
    }

    public void setHomeFragment()
    {
        switch (UserType.getUserType(user.getUser_type())) {
            case STUDENT:
                mHomeView.setHomeFragment(AppFragment.newInstance(StudentDashBoardFragment.class, getView().getAppListener()));
                mHomeView.inflateMenu(R.menu.menu_student);
                break;
            case PARENT:
                mHomeView.setHomeFragment(AppFragment.newInstance(ParentFragment.class, getView().getAppListener()));
                mHomeView.inflateMenu(R.menu.menu_parent);
                break;
            case TEACHER:
                mHomeView.setHomeFragment(AppFragment.newInstance(TeacherDashBoardFragment.class, getView().getAppListener()));
                mHomeView.inflateMenu(R.menu.menu_teacher);
                break;
            default:
                mHomeView.setHomeFragment(AppFragment.newInstance(DashBoardFragment.class, getView().getAppListener()));
                break;
        }
    }

    @Override
    public void detachView() {
        getView().getAppListener().unBindSocketService();
        super.detachView();
    }
}
