package com.pws.pateast.fragment.profile.reset;

import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.UserType;

/**
 * Created by intel on 15-May-17.
 */

public interface ResetView extends AppView
{
    boolean isResetPassword();
    boolean isResetUsername();
    boolean isResetProfile();
    void setError(int id,String error);

    void navigateToProfile(String message);
    void setProfileData(UserInfo profileData, UserType userType);
    void openImageChooser();
}
