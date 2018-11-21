package com.pws.pateast.fragment.suggestion;

import com.pws.pateast.base.AppView;

/**
 * Created by intel on 19-Aug-17.
 */

public interface ParentSuggestionView extends AppView
{
    void setError(int id, String error);

    void onSuccess(String message);
    void onError(String message);
}
