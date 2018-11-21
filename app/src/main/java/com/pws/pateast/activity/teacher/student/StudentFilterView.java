package com.pws.pateast.activity.teacher.student;

import com.pws.pateast.fragment.presenter.ClassView;

/**
 * Created by intel on 26-Apr-17.
 */

public interface StudentFilterView extends ClassView
{
    int tabIndicatorColor();
    int tabSelectedTextColor();
    int tabTextAppearance();
    int tabBackground();
    boolean isAddUser();
}
