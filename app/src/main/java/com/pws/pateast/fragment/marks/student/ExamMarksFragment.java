package com.pws.pateast.fragment.marks.student;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.base.ui.view.BaseRecyclerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.adapter.ClassAdapter;
import com.pws.pateast.adapter.ExamAdapter;
import com.pws.pateast.adapter.StudentAdapter;
import com.pws.pateast.api.model.ExamMarks;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.presenter.StudentPresenter;
import com.pws.pateast.fragment.presenter.StudentView;
import com.pws.pateast.utils.FontManager;
import com.pws.pateast.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by intel on 17-May-17.
 */

public class ExamMarksFragment extends AppFragment implements ExamMarksView, StudentView, View.OnClickListener, OnChartValueSelectedListener {
    private CardView cardFilter;
    private CardView cardStudent;
    private CardView cardMarks;
    private CircleImageView imgProfile;
    private TextView tvStudentName, tvEnrollNo, tvPhoneNo;

    private ListPopupWindow popUpClass, popUpExam, popUpStudent;
    private TextView tvSelectClass, tvSelectExam, tvSelectStudent, tvMessage;

    private BaseRecyclerView rvExamMarks;
    private ExamMarksAdapter marksAdapter;

    private ClassAdapter classAdapter;
    private ExamAdapter examAdapter;
    private StudentAdapter studentAdapter;

    private ExamMarksPresenter mPresenter;
    private StudentPresenter studentPresenter;
    private TeacherClass mTeacherClass;
    private Schedule mExamHead;
    private Student mStudent;
    private LineChart mChart;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_student_marks;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_exam_marks);

        cardFilter = (CardView) findViewById(R.id.card_filter);
        tvSelectClass = (TextView) findViewById(R.id.tv_select_class);
        tvSelectExam = (TextView) findViewById(R.id.tv_select_exam);
        tvSelectStudent = (TextView) findViewById(R.id.tv_select_student);
        tvMessage = (TextView) findViewById(R.id.tv_message);

        cardStudent = (CardView) findViewById(R.id.card_student);
        cardMarks = (CardView) findViewById(R.id.card_marks);
        imgProfile = (CircleImageView) findViewById(R.id.img_profile);
        tvEnrollNo = (TextView) findViewById(R.id.tv_enroll_number);
        tvStudentName = (TextView) findViewById(R.id.tv_student_name);
        tvPhoneNo = (TextView) findViewById(R.id.tv_phone);
        mChart = (LineChart) findViewById(R.id.graph);

        rvExamMarks = (BaseRecyclerView) findViewById(R.id.rv_exam_marks);
        rvExamMarks.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvExamMarks.setUpAsList();
        rvExamMarks.setLoadingMoreEnabled(false);
        rvExamMarks.setPullRefreshEnabled(false);

        tvSelectClass.setOnClickListener(this);
        tvSelectExam.setOnClickListener(this);
        tvSelectStudent.setOnClickListener(this);

        if (getActivity() instanceof TeacherTaskActivity) {
            mPresenter = new ExamMarksPresenter(true);
            mPresenter.attachView(this);

            studentPresenter = new StudentPresenter(true);
            studentPresenter.attachView(this);
            studentPresenter.getMyClasses();
        } else {
            mPresenter = new ExamMarksPresenter(false);
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
        /*mChart.centerViewToAnimated(e.getX(), e.getY(), mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);*/
    }

    @Override
    public void onNothingSelected() {

    }

    private void setData(final ArrayList<ExamMarks> marks) {

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();
        double maxMark = 0;
        for (ExamMarks item : marks) {
            xVals.add(item.getSubject().getSubjectdetails().get(0).getAlias());
            yVals.add(new Entry((float) item.getMarkrecords().get(0).getObtained_mark(), marks.indexOf(item)));
            if (maxMark < item.getMax_mark())
                maxMark = item.getMax_mark();
        }


        LineDataSet set1;
        // create a dataset and give it a type
        String title = getString(R.string.marks_statics);
        set1 = new LineDataSet(yVals, title);
        set1.setDrawValues(true);

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setFillAlpha(65);
        set1.setCircleRadius(3f);
        set1.setFillColor(Color.BLACK);
        set1.setValueTextColor(Color.BLACK);
        set1.setHighLightColor(Color.BLACK);
        set1.setDrawCircleHole(true);
        set1.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        LineData data = new LineData(xVals, set1);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(10f);
        // set data
        mChart.setData(data);
        mChart.getAxisLeft().setAxisMaxValue((float) maxMark);
        mChart.getAxisLeft().setAxisMinValue(0);

        initializeData();
    }

    private void initializeData() {
        mChart.setOnChartValueSelectedListener(null);
        // enable touch gestures
        mChart.setTouchEnabled(false);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setHighlightPerDragEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        mChart.getAxisRight().setEnabled(false);
        mChart.setDescription("");
    }


    private void drawMarksGraph(final ArrayList<ExamMarks> marks) {
        // add data
        setData(marks);
        //mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(FontManager.getAppFont(getContext()));
        l.setTextSize(10f);
        l.setTextColor(Color.BLACK);
//        l.setYOffset(11f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(FontManager.getAppFont(getContext()));
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(FontManager.getAppFont(getContext()));
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawTopYLabelEntry(false);
        mChart.setOnChartValueSelectedListener(this);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }


    @Override
    protected boolean hasOptionMenu() {
        return false;
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getMyExamMarks();
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.getTaskType(getArguments().getInt(TaskActivity.EXTRA_TASK_TYPE));
    }

    @Override
    public int getExamScheduleID() {
        if (mExamHead != null)
            return mExamHead.getId();
        return getArguments().getInt(Extras.EXAM_SCHEDULE_ID);
    }

    @Override
    public void bindStudent(Student student) {
        if (student != null) {
            ImageUtils.setImageUrl(getContext(), imgProfile, student.getUser().getUser_image(), R.drawable.user_placeholder);
            tvStudentName.setText(student.getUser().getUserdetails().get(0).getFullname());
            if (!TextUtils.isEmpty(student.getUser().getMobile())) {
                tvPhoneNo.setText(student.getUser().getMobile());
            } else {
                tvPhoneNo.setText(R.string.not_available);
            }
            tvEnrollNo.setText(student.getEnrollment_no());
            cardStudent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setExamMarksAdapter(ArrayList<ExamMarks> marks) {
        if (rvExamMarks.getAdapter() == null) {
            marksAdapter = new ExamMarksAdapter(getContext());
            rvExamMarks.setAdapter(marksAdapter);
        }
        marksAdapter.update(marks);
        rvExamMarks.setVisibility(View.VISIBLE);
        cardMarks.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.GONE);
        drawMarksGraph(marks);
    }

    @Override
    public TeacherClass getTeacherClass() {
        return mTeacherClass;
    }

    @Override
    public Student getStudent() {
        return mStudent;
    }

    @Override
    public void setError(String error) {
        tvMessage.setText(error);
        tvMessage.setVisibility(View.VISIBLE);
        cardMarks.setVisibility(View.GONE);
        rvExamMarks.setVisibility(View.GONE);
        if (marksAdapter != null)
            marksAdapter.clear();
    }

    @Override
    public void setClass(TeacherClass classes) {
        setExamHead(null);
        mTeacherClass = classes;
        if (mTeacherClass != null) {
            tvSelectClass.setText(classAdapter.getClassName(mTeacherClass));
            mPresenter.getTeacherExamHead(mTeacherClass, false);
            studentPresenter.getStudents(mTeacherClass);
        } else {
            tvSelectClass.setText(null);
        }
    }

    @Override
    public void setClassAdapter(List<TeacherClass> classes) {
        if (popUpClass == null) {
            popUpClass = new ListPopupWindow(getContext());
            popUpClass.setAnchorView(tvSelectClass);
            popUpClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (classAdapter != null) {
                        setClass(classAdapter.getItem(position));
                    } else {
                        setClass(null);
                    }
                    popUpClass.dismiss();
                }
            });
        }
        if (classAdapter == null) {
            classAdapter = new ClassAdapter(getContext());
        }
        classAdapter.update(classes);
        popUpClass.setAdapter(classAdapter);
        cardFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void setStudent(Student student) {
        mStudent = student;
        if (mStudent != null)
            tvSelectStudent.setText(studentAdapter.getStudentName(mStudent));
        if (mStudent != null && mExamHead != null)
            onActionClick();
        else if (mStudent == null) {
            cardStudent.setVisibility(View.GONE);
            cardMarks.setVisibility(View.GONE);
            rvExamMarks.setVisibility(View.GONE);
            if (marksAdapter != null)
                marksAdapter.clear();
            if (studentAdapter != null)
                studentAdapter.clear();
            tvSelectStudent.setText(null);
        }
    }

    @Override
    public void setStudentAdapter(List<Student> students) {
        if (popUpStudent == null) {
            popUpStudent = new ListPopupWindow(getContext());
            popUpStudent.setAnchorView(tvSelectStudent);
            popUpStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (studentAdapter != null) {
                        setStudent(studentAdapter.getItem(position));
                    } else {
                        setStudent(null);
                    }
                    popUpStudent.dismiss();
                }
            });
        }
        if (studentAdapter == null) {
            studentAdapter = new StudentAdapter(getContext());
        }
        studentAdapter.update(students);
        popUpStudent.setAdapter(studentAdapter);
    }

    @Override
    public void setExamHead(Schedule examHead) {
        mExamHead = examHead;
        if (mExamHead != null) {
            tvSelectExam.setText(examAdapter.getExamName(mExamHead));
        }
        if (mStudent != null && mExamHead != null)
            onActionClick();
        else if (mExamHead == null) {
            tvSelectExam.setText(null);
            if (examAdapter != null)
                examAdapter.clear();
            setStudent(null);
        }
    }

    @Override
    public void setExamHeads(List<Schedule> examHeads, boolean isSchedule) {
        if (popUpExam == null) {
            popUpExam = new ListPopupWindow(getContext());
            popUpExam.setAnchorView(tvSelectExam);
            popUpExam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (examAdapter != null) {
                        setExamHead(examAdapter.getItem(position));
                    } else {
                        setExamHead(null);
                    }
                    popUpExam.dismiss();
                }
            });
        }
        if (examAdapter == null) {
            examAdapter = new ExamAdapter(getContext());

        }
        examAdapter.update(examHeads);
        popUpExam.setAdapter(examAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_class:
                if (popUpClass != null)
                    popUpClass.show();
                break;
            case R.id.tv_select_exam:
                if (popUpExam != null)
                    popUpExam.show();
                break;
            case R.id.tv_select_student:
                if (popUpStudent != null)
                    popUpStudent.show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
