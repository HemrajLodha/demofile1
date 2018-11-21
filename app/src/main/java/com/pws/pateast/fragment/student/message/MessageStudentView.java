package com.pws.pateast.fragment.student.message;

import com.pws.pateast.base.AppView;

/**
 * Created by intel on 11-Jul-17.
 */

public interface MessageStudentView extends AppView
{
    void setError(int id, String error);
    String getMessage();
    int getStudentId();
    String getStudentName();
    void dismiss();
}
