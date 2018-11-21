package com.pws.pateast.fragment.feeds;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.FeedCategory;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.Utils;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FeedsPresenter extends AppPresenter<FeedsView> {
    @Inject
    ServiceBuilder serviceBuilder;
    @Inject
    Preference preference;

    private FeedsView mFeedsView;

    private APIService apiService;

    private User user;
    private Ward ward;

    @Override
    public FeedsView getView() {
        return mFeedsView;
    }

    @Override
    public void attachView(FeedsView view) {
        mFeedsView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
    }

    public UserType getUserType() {
        return UserType.getUserType(getUser().getUser_type());
    }

    public User getUser() {
        return user.getData();
    }

    public int getUserId() {
        return getUser().getId();
    }

    public void getFeeds() {
        apiService = serviceBuilder.createService(APIService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        switch (UserType.getUserType(user.getData().getUser_type())) {
            case PARENT:
                params.put("masterId", String.valueOf(ward.getMasterId()));
                params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
                params.put("bcsmapId", String.valueOf(ward.getBcsMapId()));
                break;
            case STUDENT:
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
                params.put("bcsmapId", String.valueOf(user.getUserdetails().getBcsMapId()));
                break;
            case TEACHER:
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
                break;
        }

        switch (getView().getFeedCategory()) {
            case FeedCategory.MINE:
                params.put("userId", String.valueOf(user.getData().getId()));
                break;
            case FeedCategory.APPROVAL:
                params.put("approvable", String.valueOf(1));
                break;
        }

        disposable = apiService.getFeeds(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Feeds>() {

                    @Override
                    public void onResponse(Feeds response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_feeds_found), RetrofitException.ERROR_TYPE_MESSAGE));
                        }

                        getView().setFeedAdapter(response.getData(), getUserId());
                    }

                    @Override
                    public FeedsPresenter getPresenter() {
                        return FeedsPresenter.this;
                    }
                });
    }

    public void showDeleteFeedDialog(int feedID) {
        getView().showDialog(getString(R.string.menu_delete), getString(R.string.delete_prompt_message), (id, value) -> {
                    if (id == 0) {
                        deleteFeed(feedID);
                    }
                }, R.string.menu_delete, R.string.cancel
        );
    }

    public void showRejectFeedDialog(int feedID) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_edit_text, null);
        EditText etComments = view.findViewById(R.id.et_comments);
        etComments.setHint(getString(R.string.validate_edittext, "reject reason"));
        AlertDialog dialog = DialogUtils.showDialog(getContext(), getString(R.string.menu_reject), getString(R.string.reject_prompt_message), view, (id, value) -> {
                    if (id == 0 && !TextUtils.isEmpty(etComments.getText())) {
                        rejectFeed(feedID, etComments.getText().toString());
                    }
                }, R.string.menu_reject, R.string.cancel
        );
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        etComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!TextUtils.isEmpty(etComments.getText()));
            }
        });
    }


    private void deleteFeed(int feedID) {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("id", String.valueOf(feedID));

        disposable = apiService.deleteFeeds(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        getView().onActionClick();
                        Utils.showToast(getContext(), response.getMessage());
                    }

                    @Override
                    public FeedsPresenter getPresenter() {
                        return FeedsPresenter.this;
                    }
                });

    }

    private void rejectFeed(int feedID, String rejectReason) {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("id", String.valueOf(feedID));
        params.put("reject_reason", rejectReason);

        disposable = apiService.rejectFeeds(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        getView().onActionClick();
                        Utils.showToast(getContext(), response.getMessage());
                    }

                    @Override
                    public FeedsPresenter getPresenter() {
                        return FeedsPresenter.this;
                    }
                });

    }

    public void showApproveFeedDialog(int feedID, int approved) {
        String title, message = null;
        switch (approved) {
            case 0:
                title = getString(R.string.menu_disapprove);
                message = getString(R.string.disapprove_prompt_message);
                break;
            case 1:
                title = getString(R.string.menu_approve_class);
                message = getString(R.string.approve_class_prompt_message);
                break;
            case 2:
                title = getString(R.string.menu_approve_all);
                message = getString(R.string.approve_all_prompt_message);
                break;
            default:
                return;
        }
        getView().showDialog(title, message, (id, value) -> {
                    if (id == 0) {
                        approveFeed(feedID, approved);
                    }
                },
                R.string.yes, R.string.no
        );
    }

    private void approveFeed(int feedID, int approved) {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("id", String.valueOf(feedID));
        params.put("approved", String.valueOf(approved));

        disposable = apiService.approveFeeds(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        getView().onActionClick();
                        Utils.showToast(getContext(), response.getMessage());
                    }

                    @Override
                    public FeedsPresenter getPresenter() {
                        return FeedsPresenter.this;
                    }
                });

    }

    public void likeUnlikeFeed(int feedID, boolean isLiked) {
        apiService = serviceBuilder.createService(APIService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("id", String.valueOf(feedID));
        Single<Response> request = isLiked ? apiService.likeFeeds(params) : apiService.unlikeFeeds(params);
        disposable = request.subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        getView().updateAction(feedID, isLiked);
                    }

                    @Override
                    public FeedsPresenter getPresenter() {
                        return FeedsPresenter.this;
                    }
                });

    }

}
