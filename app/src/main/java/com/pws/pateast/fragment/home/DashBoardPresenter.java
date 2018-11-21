package com.pws.pateast.fragment.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Language;
import com.pws.pateast.api.model.Session;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppActivity;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.model.DashboardItem;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.R;
import com.pws.pateast.utils.ShortcutUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by intel on 13-May-17.
 */

public abstract class DashBoardPresenter<V extends DashBoardView> extends AppPresenter<V>
{
    @Inject
    protected EventBus mEventBus;
    @Inject
    protected Preference preference;
    @Inject
    protected ServiceBuilder serviceBuilder;
    @Inject
    protected ShortcutUtils shortcutUtils;

    private APIService apiService;

    protected UserInfo userInfo;

    protected User user;

    V dashBoardView;

    abstract public List<DashboardItem> getDashboardItem();


    @Override
    public V getView() {
        return dashBoardView;
    }

    @Override
    public void attachView(V view) {
        dashBoardView = view;
        getComponent().inject((DashBoardPresenter<DashBoardView>)this);
        userInfo = preference.getUserInfo().getData();
        user = preference.getUser();

    }



    public void setDashboardAdapter()
    {
        List<DashboardItem> dashboardItems = getDashboardItem();
        getView().setDashboardAdapter(dashboardItems);
        if (!preference.isAppShortcutCreated() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutUtils.createShortcuts(dashboardItems);
            preference.setAppShortcutCreated(true);
        }
    }

    public void showSessionDialog(boolean cancelable)
    {
        final List<Session> items  = user.getUserdetails().getAcademicSessions();
        DialogUtils.showSingleChoiceDialog(getContext(),
                getString(R.string.action_change_session_title),
                Session.getNameArray(items),
                Session.getSelectedPosition(items, user.getData().getAcademicSessionId()),
                new AdapterListener<Integer>() {
                    @Override
                    public void onClick(int id, Integer value) {
                        if (id == AlertDialog.BUTTON_POSITIVE) {
                          //  preference.setSessionId(items.get(value).getId());
                        }
                    }
                },cancelable,
                getString(R.string.ok)
        );
    }

    public void showLanguageDialog(boolean cancelable)
    {
        final List<Language> items  = user.getLanguages();

        DialogUtils.showSingleChoiceDialog(getContext(),
                getString(R.string.action_change_language_title),
                Language.getNameArray(items),
                Language.getSelectedPosition(items, preference.getLanguage()),
                new AdapterListener<Integer>() {
                    @Override
                    public void onClick(int id, Integer value) {
                        if (id == AlertDialog.BUTTON_POSITIVE)
                        {
                            preference.setLanguage(items.get(value).getCode());
                            preference.setLanguageID(items.get(value).getId());
                            getContext().sendBroadcast(new Intent(AppActivity.LANGUAGE));
                        }
                    }
                },cancelable,
                getString(R.string.ok)
        );
    }

}
