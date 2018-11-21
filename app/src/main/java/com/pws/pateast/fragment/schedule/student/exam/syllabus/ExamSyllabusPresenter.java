package com.pws.pateast.fragment.schedule.student.exam.syllabus;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.pws.pateast.base.AppPresenter;

/**
 * Created by intel on 02-Sep-17.
 */

public class ExamSyllabusPresenter extends AppPresenter<ExamSyllabusView>
{
    private ExamSyllabusView view;

    @Override
    public ExamSyllabusView getView() {
        return view;
    }

    @Override
    public void attachView(ExamSyllabusView view) {
        this.view = view;
        getView().bindData(getView().getSchedule());
    }

    public int getScale(){
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(200);
        val = val * 100d;
        return val.intValue();
    }
}
