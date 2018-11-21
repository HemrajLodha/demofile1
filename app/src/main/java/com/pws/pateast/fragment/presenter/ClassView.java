package com.pws.pateast.fragment.presenter;

import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 20-Apr-17.
 */

public interface ClassView extends AppView
{
    void setClass(TeacherClass classes);
    void setClassAdapter(List<TeacherClass> classes);

}
