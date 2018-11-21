package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 25-Apr-17.
 */

public class Schedule extends Response<ArrayList<Schedule>> implements Parcelable {
    int id, timetableId, teacherId, subjectId, examScheduleId, examheadId, masterId, min_passing_mark, max_mark, order, is_break, is_present;
    String weekday, start_time, end_time, date, exam_type, duration, syllabus;

    Schedule timetable;

    TeacherClass bcsmap;
    ExamHead examhead;

    Subject subject;
    UserInfo teacher;
    private ArrayList<Schedule> timetableallocations;
    private ArrayList<Schedule> examsyllabuses;
    ArrayList<Schedule> attendancerecords;


    public Schedule() {
    }

    public Schedule(ArrayList<Schedule> timetableallocations) {
        this.timetableallocations = timetableallocations;
    }

    protected Schedule(Parcel in) {
        id = in.readInt();
        timetableId = in.readInt();
        teacherId = in.readInt();
        subjectId = in.readInt();
        examScheduleId = in.readInt();
        examheadId = in.readInt();
        masterId = in.readInt();
        min_passing_mark = in.readInt();
        max_mark = in.readInt();
        order = in.readInt();
        is_break = in.readInt();
        is_present = in.readInt();
        weekday = in.readString();
        start_time = in.readString();
        end_time = in.readString();
        date = in.readString();
        exam_type = in.readString();
        duration = in.readString();
        syllabus = in.readString();
        timetable = in.readParcelable(Schedule.class.getClassLoader());
        bcsmap = in.readParcelable(TeacherClass.class.getClassLoader());
        examhead = in.readParcelable(ExamHead.class.getClassLoader());
        subject = in.readParcelable(Subject.class.getClassLoader());
        teacher = in.readParcelable(UserInfo.class.getClassLoader());
        timetableallocations = in.createTypedArrayList(Schedule.CREATOR);
        examsyllabuses = in.createTypedArrayList(Schedule.CREATOR);
        attendancerecords = in.createTypedArrayList(Schedule.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(timetableId);
        dest.writeInt(teacherId);
        dest.writeInt(subjectId);
        dest.writeInt(examScheduleId);
        dest.writeInt(examheadId);
        dest.writeInt(masterId);
        dest.writeInt(min_passing_mark);
        dest.writeInt(max_mark);
        dest.writeInt(order);
        dest.writeInt(is_break);
        dest.writeInt(is_present);
        dest.writeString(weekday);
        dest.writeString(start_time);
        dest.writeString(end_time);
        dest.writeString(date);
        dest.writeString(exam_type);
        dest.writeString(duration);
        dest.writeString(syllabus);
        dest.writeParcelable(timetable, flags);
        dest.writeParcelable(bcsmap, flags);
        dest.writeParcelable(examhead, flags);
        dest.writeParcelable(subject, flags);
        dest.writeParcelable(teacher, flags);
        dest.writeTypedList(timetableallocations);
        dest.writeTypedList(examsyllabuses);
        dest.writeTypedList(attendancerecords);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getExamScheduleId() {
        return examScheduleId;
    }

    public void setExamScheduleId(int examScheduleId) {
        this.examScheduleId = examScheduleId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getMin_passing_mark() {
        return min_passing_mark;
    }

    public void setMin_passing_mark(int min_passing_mark) {
        this.min_passing_mark = min_passing_mark;
    }

    public int getMax_mark() {
        return max_mark;
    }

    public void setMax_mark(int max_mark) {
        this.max_mark = max_mark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ExamHead getExamhead() {
        return examhead;
    }

    public void setExamhead(ExamHead examhead) {
        this.examhead = examhead;
    }

    public int getExamheadId() {
        return examheadId;
    }

    public void setExamheadId(int examheadId) {
        this.examheadId = examheadId;
    }

    public Schedule getTimetable() {
        return timetable;
    }

    public void setTimetable(Schedule timetable) {
        this.timetable = timetable;
    }

    public TeacherClass getBcsmap() {
        return bcsmap;
    }

    public void setBcsmap(TeacherClass bcsmap) {
        this.bcsmap = bcsmap;
    }

    public UserInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(UserInfo teacher) {
        this.teacher = teacher;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getIs_break() {
        return is_break;
    }

    public void setIs_break(int is_break) {
        this.is_break = is_break;
    }

    public ArrayList<Schedule> getTimetableallocations() {
        return timetableallocations;
    }

    public void setTimetableallocations(ArrayList<Schedule> timetableallocations) {
        this.timetableallocations = timetableallocations;
    }

    public int getIs_present() {
        return is_present;
    }

    public void setIs_present(int is_present) {
        this.is_present = is_present;
    }

    public ArrayList<Schedule> getAttendancerecords() {
        return attendancerecords;
    }

    public void setAttendancerecords(ArrayList<Schedule> attendancerecords) {
        this.attendancerecords = attendancerecords;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public ArrayList<Schedule> getExamsyllabuses() {
        return examsyllabuses;
    }

    public void setExamsyllabuses(ArrayList<Schedule> examsyllabuses) {
        this.examsyllabuses = examsyllabuses;
    }

    public String getTime(DateFormat format1, DateFormat format2) {
        String start_time = "--", end_time = "--";
        if (this.start_time == null || this.end_time == null)
            return String.format("%s - %s", start_time, end_time);
        try {
            start_time = format2.format(format1.parse(this.start_time));
            end_time = format2.format(format1.parse(this.end_time));
            return String.format("%s - %s", start_time, end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getStartTime(DateFormat format1, DateFormat format2) {
        String start_time = "--";
        if(this.start_time == null)
            return String.format("%s", start_time);
        try {
            start_time = format2.format(format1.parse(this.start_time));
            return String.format("%s", start_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String[] getNameArray(List<Schedule> examHeads) {
        if (examHeads == null)
            examHeads = new ArrayList<>();
        String[] examHeadArray = new String[examHeads.size()];
        for (Schedule item : examHeads) {
            examHeadArray[examHeads.indexOf(item)] = item.getExamhead().getExamheaddetails().get(0).getName();
        }
        return examHeadArray;
    }

    public static int getSelectedPosition(List<Schedule> examHeads, int Id) {
        if (examHeads == null)
            examHeads = new ArrayList<>();
        int pos = -1;
        for (Schedule item : examHeads) {
            if (item.getId() == Id) {
                pos = examHeads.indexOf(item);
                break;
            }
        }
        return pos;
    }

}
