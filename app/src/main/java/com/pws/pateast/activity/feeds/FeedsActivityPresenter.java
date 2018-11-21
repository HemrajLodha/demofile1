package com.pws.pateast.activity.feeds;

import android.os.Bundle;

import com.pws.pateast.R;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.FeedCategory;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.feeds.FeedsFragment;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class FeedsActivityPresenter extends AppPresenter<FeedsActivityView> {
    @Inject
    Preference preference;

    private FeedsActivityView mView;
    private User user;
    private Ward ward;

    @Override
    public FeedsActivityView getView() {
        return mView;
    }

    @Override
    public void attachView(FeedsActivityView view) {
        mView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        getView().setFeedsCategoryAdapter();
    }

    public UserType getUserType() {
        return UserType.getUserType(getUser().getUser_type());
    }

    public User getUser() {
        return user.getData();
    }

    public List<FeedsFragment> getFeedsFragments() {
        List<Integer> categories = new ArrayList<>();
        switch (getUserType()) {
            case PARENT:
            case STUDENT:
                categories.add(FeedCategory.ALL);
                categories.add(FeedCategory.MINE);
                break;
            case TEACHER:
                categories.add(FeedCategory.ALL);
                categories.add(FeedCategory.MINE);
                categories.add(FeedCategory.APPROVAL);
                break;
        }
        return StreamSupport
                .stream(categories)
                .map(category -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Extras.EXTRA_TYPE, category.intValue());
                    bundle.putString(Extras.TITLE, getFeedsFragmentTitle(category.intValue()));
                    return AppFragment.newInstance(FeedsFragment.class, null, bundle);
                })
                .collect(Collectors.toList());
    }

    private String getFeedsFragmentTitle(int category) {
        switch (category) {
            case FeedCategory.ALL:
                return getString(R.string.title_all);
            case FeedCategory.MINE:
                return getString(R.string.title_mine);
            case FeedCategory.APPROVED:
                return getString(R.string.title_approved);
            case FeedCategory.APPROVAL:
                return getString(R.string.title_approval);
            default:
                return getString(R.string.title_all);
        }
    }

}
