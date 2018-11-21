package com.pws.pateast.activity.lms.filter;

import android.view.View;

import com.pws.pateast.api.model.Chapter;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.Topic;
import com.pws.pateast.base.AppView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by intel on 24-Jan-18.
 */

public interface LMSFilterView extends AppView, View.OnClickListener {
    void setSubject(Subject subject);

    Subject getSubject();

    HashMap<Integer, List<Subject>> getSubjectData();

    void setSubjectData(HashMap<Integer, List<Subject>> subjectData);

    void setSubjectAdapter(List<Subject> subjects);

    void setChapter(Chapter chapter);

    Chapter getChapter();

    HashMap<Integer, List<Chapter>> getChapterData();

    void setChapterData(HashMap<Integer, List<Chapter>> chapterData);

    void setChapterAdapter(List<Chapter> chapters);

    void setTopic(Topic topic);

    Topic getTopic();

    HashMap<Integer, List<Topic>> getTopicData();

    void setTopicData(HashMap<Integer, List<Topic>> topicData);

    void setTopicAdapter(List<Topic> topics);

    boolean isError();
}
