package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.pws.pateast.fragment.attendance.teacher.addupdate.AttendanceTakerView;
import com.pws.pateast.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by intel on 26-Apr-17.
 */

public class Student extends Response<ArrayList<Student>> implements Parcelable {
    String createdAt, updatedAt;
    int id, userId, languageId, masterId, bcsMapId, studentId, boardId, academicSessionId, classId, sectionId, attendanceId, is_present, holidayId, subjectId, totalAttendance, totalPresent;

    String father_contact, enrollment_no, father_name, mother_name, guardian_name, guardian_relationship, birthmark, height, tags, name, alias, date, address, birthplace, religion, caste, nationality, pre_school_name, pre_school_address, pre_qualification;
    String form_no, fee_receipt_no, doa, dob, blood_group, gender, mother_contact, guardian_contact, mark_list_img, birth_certificate_img, tc_img, cast_certificate_img, migration_certificate_img, affidavit_img;
    String display_order;
    int countryId, stateId, cityId, is_active, period;
    boolean holiday, isOpened;
    Student student, attendance, assignmentremark, attendancerecord, studentrecord, holiday_data;
    String roll_no;
    UserInfo user;
    Trip triprecord;
    TeacherClass bcsmap;

    Country country;
    State state;
    City city;
    ArrayList<Student> studentdetails, record, attendancerecords, holidaydetails, students;
    ArrayList<Tag> tagsData;

    public Student(int studentId) {
        this.studentId = studentId;
    }

    public Student() {
    }

    protected Student(Parcel in) {
        createdAt = in.readString();
        updatedAt = in.readString();
        id = in.readInt();
        userId = in.readInt();
        languageId = in.readInt();
        masterId = in.readInt();
        bcsMapId = in.readInt();
        studentId = in.readInt();
        boardId = in.readInt();
        academicSessionId = in.readInt();
        classId = in.readInt();
        sectionId = in.readInt();
        attendanceId = in.readInt();
        is_present = in.readInt();
        holidayId = in.readInt();
        subjectId = in.readInt();
        totalAttendance = in.readInt();
        totalPresent = in.readInt();
        father_contact = in.readString();
        enrollment_no = in.readString();
        father_name = in.readString();
        mother_name = in.readString();
        guardian_name = in.readString();
        guardian_relationship = in.readString();
        birthmark = in.readString();
        height = in.readString();
        tags = in.readString();
        name = in.readString();
        alias = in.readString();
        date = in.readString();
        address = in.readString();
        birthplace = in.readString();
        religion = in.readString();
        caste = in.readString();
        nationality = in.readString();
        pre_school_name = in.readString();
        pre_school_address = in.readString();
        pre_qualification = in.readString();
        form_no = in.readString();
        fee_receipt_no = in.readString();
        doa = in.readString();
        dob = in.readString();
        blood_group = in.readString();
        gender = in.readString();
        mother_contact = in.readString();
        guardian_contact = in.readString();
        mark_list_img = in.readString();
        birth_certificate_img = in.readString();
        tc_img = in.readString();
        cast_certificate_img = in.readString();
        migration_certificate_img = in.readString();
        affidavit_img = in.readString();
        display_order = in.readString();
        countryId = in.readInt();
        stateId = in.readInt();
        cityId = in.readInt();
        is_active = in.readInt();
        period = in.readInt();
        holiday = in.readByte() != 0;
        isOpened = in.readByte() != 0;
        student = in.readParcelable(Student.class.getClassLoader());
        attendance = in.readParcelable(Student.class.getClassLoader());
        assignmentremark = in.readParcelable(Student.class.getClassLoader());
        attendancerecord = in.readParcelable(Student.class.getClassLoader());
        studentrecord = in.readParcelable(Student.class.getClassLoader());
        holiday_data = in.readParcelable(Student.class.getClassLoader());
        roll_no = in.readString();
        user = in.readParcelable(UserInfo.class.getClassLoader());
        triprecord = in.readParcelable(Trip.class.getClassLoader());
        bcsmap = in.readParcelable(TeacherClass.class.getClassLoader());
        studentdetails = in.createTypedArrayList(Student.CREATOR);
        record = in.createTypedArrayList(Student.CREATOR);
        attendancerecords = in.createTypedArrayList(Student.CREATOR);
        holidaydetails = in.createTypedArrayList(Student.CREATOR);
        students = in.createTypedArrayList(Student.CREATOR);
        tagsData = in.createTypedArrayList(Tag.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(languageId);
        dest.writeInt(masterId);
        dest.writeInt(bcsMapId);
        dest.writeInt(studentId);
        dest.writeInt(boardId);
        dest.writeInt(academicSessionId);
        dest.writeInt(classId);
        dest.writeInt(sectionId);
        dest.writeInt(attendanceId);
        dest.writeInt(is_present);
        dest.writeInt(holidayId);
        dest.writeInt(subjectId);
        dest.writeInt(totalAttendance);
        dest.writeInt(totalPresent);
        dest.writeString(father_contact);
        dest.writeString(enrollment_no);
        dest.writeString(father_name);
        dest.writeString(mother_name);
        dest.writeString(guardian_name);
        dest.writeString(guardian_relationship);
        dest.writeString(birthmark);
        dest.writeString(height);
        dest.writeString(tags);
        dest.writeString(name);
        dest.writeString(alias);
        dest.writeString(date);
        dest.writeString(address);
        dest.writeString(birthplace);
        dest.writeString(religion);
        dest.writeString(caste);
        dest.writeString(nationality);
        dest.writeString(pre_school_name);
        dest.writeString(pre_school_address);
        dest.writeString(pre_qualification);
        dest.writeString(form_no);
        dest.writeString(fee_receipt_no);
        dest.writeString(doa);
        dest.writeString(dob);
        dest.writeString(blood_group);
        dest.writeString(gender);
        dest.writeString(mother_contact);
        dest.writeString(guardian_contact);
        dest.writeString(mark_list_img);
        dest.writeString(birth_certificate_img);
        dest.writeString(tc_img);
        dest.writeString(cast_certificate_img);
        dest.writeString(migration_certificate_img);
        dest.writeString(affidavit_img);
        dest.writeString(display_order);
        dest.writeInt(countryId);
        dest.writeInt(stateId);
        dest.writeInt(cityId);
        dest.writeInt(is_active);
        dest.writeInt(period);
        dest.writeByte((byte) (holiday ? 1 : 0));
        dest.writeByte((byte) (isOpened ? 1 : 0));
        dest.writeParcelable(student, flags);
        dest.writeParcelable(attendance, flags);
        dest.writeParcelable(assignmentremark, flags);
        dest.writeParcelable(attendancerecord, flags);
        dest.writeParcelable(studentrecord, flags);
        dest.writeParcelable(holiday_data, flags);
        dest.writeString(roll_no);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(triprecord, flags);
        dest.writeParcelable(bcsmap, flags);
        dest.writeTypedList(studentdetails);
        dest.writeTypedList(record);
        dest.writeTypedList(attendancerecords);
        dest.writeTypedList(holidaydetails);
        dest.writeTypedList(students);
        dest.writeTypedList(tagsData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(int academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getFather_contact() {
        return father_contact;
    }

    public void setFather_contact(String father_contact) {
        this.father_contact = father_contact;
    }

    public String getEnrollment_no() {
        return enrollment_no;
    }

    public void setEnrollment_no(String enrollment_no) {
        this.enrollment_no = enrollment_no;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public ArrayList<Student> getStudentdetails() {
        return studentdetails;
    }

    public void setStudentdetails(ArrayList<Student> studentdetails) {
        this.studentdetails = studentdetails;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getIs_present() {
        return is_present == 0 ? AttendanceTakerView.SELECTED_PRESENT : is_present;
    }

    public void setIs_present(int is_present) {
        this.is_present = is_present;
    }

    public String getTags() {
        return tags == null ? "" : tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ArrayList<Student> getRecord() {
        return record;
    }

    public void setRecord(ArrayList<Student> record) {
        this.record = record;
    }

    public ArrayList<Student> getAttendancerecords() {
        return attendancerecords;
    }

    public void setAttendancerecords(ArrayList<Student> attendancerecords) {
        this.attendancerecords = attendancerecords;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public int getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Student> getHolidaydetails() {
        return holidaydetails;
    }

    public void setHolidaydetails(ArrayList<Student> holidaydetails) {
        this.holidaydetails = holidaydetails;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getTotalAttendance() {
        return totalAttendance;
    }

    public void setTotalAttendance(int totalAttendance) {
        this.totalAttendance = totalAttendance;
    }

    public int getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(int totalPresent) {
        this.totalPresent = totalPresent;
    }

    public Student getAttendance() {
        return attendance;
    }

    public void setAttendance(Student attendance) {
        this.attendance = attendance;
    }

    public Date getDate() {
        return DateUtils.parse(date, DateUtils.DATE_FORMAT_PATTERN);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getBcsMapId() {
        return bcsMapId;
    }

    public void setBcsMapId(int bcsMapId) {
        this.bcsMapId = bcsMapId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    public String getGuardian_relationship() {
        return guardian_relationship;
    }

    public void setGuardian_relationship(String guardian_relationship) {
        this.guardian_relationship = guardian_relationship;
    }

    public String getBirthmark() {
        return birthmark;
    }

    public void setBirthmark(String birthmark) {
        this.birthmark = birthmark;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPre_school_name() {
        return pre_school_name;
    }

    public void setPre_school_name(String pre_school_name) {
        this.pre_school_name = pre_school_name;
    }

    public String getPre_school_address() {
        return pre_school_address;
    }

    public void setPre_school_address(String pre_school_address) {
        this.pre_school_address = pre_school_address;
    }

    public String getPre_qualification() {
        return pre_qualification;
    }

    public void setPre_qualification(String pre_qualification) {
        this.pre_qualification = pre_qualification;
    }

    public String getForm_no() {
        return form_no;
    }

    public void setForm_no(String form_no) {
        this.form_no = form_no;
    }

    public String getFee_receipt_no() {
        return fee_receipt_no;
    }

    public void setFee_receipt_no(String fee_receipt_no) {
        this.fee_receipt_no = fee_receipt_no;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getGender() {
        return gender != null ? gender.substring(0, 1).toUpperCase() + gender.substring(1) : "N/A";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMother_contact() {
        return mother_contact;
    }

    public void setMother_contact(String mother_contact) {
        this.mother_contact = mother_contact;
    }

    public String getGuardian_contact() {
        return guardian_contact;
    }

    public void setGuardian_contact(String guardian_contact) {
        this.guardian_contact = guardian_contact;
    }

    public String getMark_list_img() {
        return mark_list_img;
    }

    public void setMark_list_img(String mark_list_img) {
        this.mark_list_img = mark_list_img;
    }

    public String getBirth_certificate_img() {
        return birth_certificate_img;
    }

    public void setBirth_certificate_img(String birth_certificate_img) {
        this.birth_certificate_img = birth_certificate_img;
    }

    public String getTc_img() {
        return tc_img;
    }

    public void setTc_img(String tc_img) {
        this.tc_img = tc_img;
    }

    public String getCast_certificate_img() {
        return cast_certificate_img;
    }

    public void setCast_certificate_img(String cast_certificate_img) {
        this.cast_certificate_img = cast_certificate_img;
    }

    public String getMigration_certificate_img() {
        return migration_certificate_img;
    }

    public void setMigration_certificate_img(String migration_certificate_img) {
        this.migration_certificate_img = migration_certificate_img;
    }

    public String getAffidavit_img() {
        return affidavit_img;
    }

    public void setAffidavit_img(String affidavit_img) {
        this.affidavit_img = affidavit_img;
    }

    public String getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(String display_order) {
        this.display_order = display_order;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Student getStudentrecord() {
        return studentrecord;
    }

    public void setStudentrecord(Student studentrecord) {
        this.studentrecord = studentrecord;
    }

    public TeacherClass getBcsmap() {
        return bcsmap;
    }

    public void setBcsmap(TeacherClass bcsmap) {
        this.bcsmap = bcsmap;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ArrayList<Tag> getTagsData() {
        return tagsData;
    }

    public void setTagsData(ArrayList<Tag> tagsData) {
        this.tagsData = tagsData;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public Trip getTriprecord() {
        return triprecord;
    }

    public void setTriprecord(Trip triprecord) {
        this.triprecord = triprecord;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public Student getAttendancerecord() {
        return attendancerecord;
    }

    public void setAttendancerecord(Student attendancerecord) {
        this.attendancerecord = attendancerecord;
    }

    public Student getHoliday_data() {
        return holiday_data;
    }

    public void setHoliday_data(Student holiday_data) {
        this.holiday_data = holiday_data;
    }

    public Student getAssignmentremark() {
        if (assignmentremark == null)
            assignmentremark = new Student();
        return assignmentremark;
    }

    public void setAssignmentremark(Student assignmentremark) {
        this.assignmentremark = assignmentremark;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    @Override
    public boolean equals(Object obj) {
        return getStudentId() == ((Student) obj).getStudentId();
    }


    public static Student getStudent(int studentId) {
        if (studentId == 0)
            return null;
        return new Student(studentId);
    }
}
