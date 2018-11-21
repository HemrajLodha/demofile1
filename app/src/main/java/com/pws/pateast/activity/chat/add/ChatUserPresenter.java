package com.pws.pateast.activity.chat.add;

import android.support.annotation.StringRes;
import android.widget.Filter;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.ChatUser;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;

/**
 * Created by intel on 03-Aug-17.
 */

public class ChatUserPresenter extends AppPresenter<ChatUserView> {
    @Inject
    protected ServiceBuilder serviceBuilder;

    @Inject
    protected Preference preference;

    protected User user;
    protected Ward ward;
    protected UserType userType;

    private APIService apiService;

    private ChatUserView chatUserView;
    private ChatUserObserver observer;
    @StringRes
    private int strRes;

    @Override
    public ChatUserView getView() {
        return chatUserView;
    }

    @Override
    public void attachView(ChatUserView view) {
        chatUserView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        userType = UserType.getUserType(user.getData().getUser_type());
        getChatUser();
    }

    public void setObserver(ChatUserObserver observer) {
        this.observer = observer;
    }

    public ChatUserObserver getObserver() {
        return observer;
    }

    private void getChatUser() {
        switch (getView().getChatUserType()) {
            case STUDENT:
                strRes = R.string.no_students;
                getView().setTitle(R.string.menu_students);
                getView().onActionClick();
                getStudents();
                break;
            case TEACHER:
                strRes = R.string.no_teachers;
                getView().setTitle(R.string.menu_teachers);
                getView().onActionClick();
                getTeachers();
                break;
            case ADMIN:
                strRes = R.string.no_admins;
                getView().setTitle(R.string.menu_admins);
                getView().onActionClick();
                getAdmins();
                break;
            case INSTITUTE:
                strRes = R.string.no_institute;
                getView().setTitle(R.string.menu_institutes);
                getInstitute();
                break;
            case PARENT:
                strRes = R.string.no_parents;
                getView().setTitle(R.string.menu_parents);
                getView().getClasses();
                break;
        }
    }


    public void getTeachers() {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        switch (userType) {
            case PARENT:
                params.put("masterId", String.valueOf(ward.getMasterId()));
                break;
            default:
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                break;
        }
        setObserver(new ChatUserObserver());
        dispose();
        disposable = apiService.getTeachers(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    public void getInstitute() {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        switch (userType) {
            case PARENT:
                params.put("masterId", String.valueOf(ward.getMasterId()));
                break;
            default:
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                break;
        }
        setObserver(new ChatUserObserver());
        dispose();
        disposable = apiService.getInstitute(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    public void getStudents() {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("bcsMapId", String.valueOf(user.getUserdetails().getBcsMapId()));

        setObserver(new ChatUserObserver());
        dispose();
        disposable = apiService.getStudents(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    public void getAdmins() {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(user.getData().getMasterId()));

        setObserver(new ChatUserObserver());
        dispose();
        disposable = apiService.getAdmins(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    public void getParents(Student student) {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("studentUserId", String.valueOf(student.getStudent().getUser().getId()));

        setObserver(new ChatUserObserver());
        dispose();
        disposable = apiService.getParents(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    private void setChatUser(List<UserInfo> chatUsers, boolean isSearch) {
        if (chatUsers.isEmpty()) {
            getObserver().onError(new RetrofitException(getString(strRes), ERROR_TYPE_MESSAGE));
        } else {
            if (isSearch) {
                getView().updateChatUserAdapter(chatUsers);
                getObserver().hide();
            } else
                getView().setChatUserAdapter(chatUsers);
        }
    }

    public Filter getFilter(List<UserInfo> originalList) {
        return new ChatUserFilter(originalList);
    }

    class ChatUserFilter extends Filter {
        private List<UserInfo> originalList;
        private List<UserInfo> mStudentFilterList;

        public ChatUserFilter(List<UserInfo> originalList) {
            this.originalList = originalList;
            this.mStudentFilterList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();

            if (charString.isEmpty()) {
                mStudentFilterList = originalList;
            } else {

                ArrayList<UserInfo> filteredList = new ArrayList<>();

                for (UserInfo userInfo : originalList) {

                    switch (getView().getChatUserType()) {
                        case STUDENT:
                            if (userInfo.getStudent().getUser().getUserdetails().get(0).getFullname().toLowerCase().contains(charString.toLowerCase()))
                                filteredList.add(userInfo);
                            break;
                        default:
                            if (userInfo.getUserdetails().get(0).getFullname().toLowerCase().contains(charString.toLowerCase()))
                                filteredList.add(userInfo);
                            break;

                    }

                }

                mStudentFilterList = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = mStudentFilterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mStudentFilterList = (ArrayList<UserInfo>) results.values;
            setChatUser(mStudentFilterList, true);
        }
    }

    class ChatUserObserver extends AppSingleObserver<ChatUser> {
        @Override
        public void onResponse(ChatUser response) {
            for (UserInfo userInfo : response.getData())
            {
                if(userInfo.getId() == user.getData().getId())
                {
                    response.getData().remove(userInfo);
                    break;
                }
            }
            setChatUser(response.getData(), false);
        }

        @Override
        public ChatUserPresenter getPresenter() {
            return ChatUserPresenter.this;
        }
    }
}
