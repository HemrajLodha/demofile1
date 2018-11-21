package com.pws.pateast.fragment.presenter;

import com.pws.pateast.api.model.Subject;

import java.util.List;

/**
 * Created by planet on 5/3/2017.
 */

public interface SubjectView extends StudentView
{
    void setSubject(Subject subject);
    void setSubjectAdapter(List<Subject> subjects);
}
