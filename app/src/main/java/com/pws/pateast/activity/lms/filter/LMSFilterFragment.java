package com.pws.pateast.activity.lms.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.activity.lms.LMSView;
import com.pws.pateast.adapter.ChapterAdapter;
import com.pws.pateast.adapter.SubjectAdapter;
import com.pws.pateast.adapter.TopicAdapter;
import com.pws.pateast.api.model.Chapter;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.Topic;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;

import java.util.HashMap;
import java.util.List;

/**
 * Created by intel on 24-Jan-18.
 */

public class LMSFilterFragment extends AppDialogFragment implements LMSFilterView {
    private TextView tvSelectSubject, tvSelectChapter, tvSelectTopic;
    private ListPopupWindow popUpSubject, popUpChapter, popUpTopic;
    private Button btnApply;
    private SubjectAdapter subjectAdapter;
    private ChapterAdapter chapterAdapter;
    private TopicAdapter topicAdapter;
    private LMSView lmsView;

    private LMSFilterPresenter mPresenter;

    public void attachView(LMSView lmsView) {
        this.lmsView = lmsView;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_lms_filter;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.filter);
        setCancelable(false);
        tvSelectSubject = (TextView) findViewById(R.id.tv_select_subject);
        tvSelectChapter = (TextView) findViewById(R.id.tv_select_chapter);
        tvSelectTopic = (TextView) findViewById(R.id.tv_select_topic);
        btnApply = (Button) findViewById(R.id.btn_apply);

        mPresenter = new LMSFilterPresenter(true);
        mPresenter.attachView(this);
        mPresenter.getSubject();

        tvSelectSubject.setOnClickListener(this);
        tvSelectChapter.setOnClickListener(this);
        tvSelectTopic.setOnClickListener(this);
        btnApply.setOnClickListener(this);
    }

    @Override
    public void setTopic(Topic topic) {
        if (topic != null) {
            tvSelectTopic.setText(topicAdapter.getTopicName(topic));
        } else {
            tvSelectTopic.setText(R.string.hint_select_topic);

        }
        getArguments().putParcelable(Extras.TOPIC, topic);
    }

    @Override
    public Topic getTopic() {
        return getArguments().getParcelable(Extras.TOPIC);
    }

    @Override
    public HashMap<Integer, List<Topic>> getTopicData() {
        return (HashMap<Integer, List<Topic>>) getArguments().getSerializable(Extras.TOPICS);
    }

    @Override
    public void setTopicData(HashMap<Integer, List<Topic>> topicData) {
        getArguments().putSerializable(Extras.TOPICS, topicData);
    }

    @Override
    public void setTopicAdapter(List<Topic> topics) {
        if (popUpTopic == null) {
            popUpTopic = new ListPopupWindow(getContext());
            popUpTopic.setAnchorView(tvSelectTopic);
            popUpTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (topicAdapter != null) {
                        setTopic(topicAdapter.getItem(position));
                    } else {
                        setTopic(null);
                    }
                    popUpTopic.dismiss();
                }
            });
        }
        if (topicAdapter == null) {
            topicAdapter = new TopicAdapter(getContext());
            Topic topic = getTopic();
            if (topic != null) {
                setTopic(topic);
            }
        }
        topicAdapter.update(topics);
        popUpTopic.setAdapter(topicAdapter);
    }

    @Override
    public void setChapter(Chapter chapter) {
        if (chapter != null) {
            tvSelectChapter.setText(chapterAdapter.getChapterName(chapter));
            mPresenter.getTopics(chapter.getId());
        } else {
            tvSelectChapter.setText(R.string.hint_select_chapter);
            mPresenter.getTopics(-1);
            setTopic(null);
        }
        getArguments().putParcelable(Extras.CHAPTER, chapter);
    }

    @Override
    public Chapter getChapter() {
        return getArguments().getParcelable(Extras.CHAPTER);
    }

    @Override
    public HashMap<Integer, List<Chapter>> getChapterData() {
        return (HashMap<Integer, List<Chapter>>) getArguments().getSerializable(Extras.CHAPTERS);
    }

    @Override
    public void setChapterData(HashMap<Integer, List<Chapter>> chapterData) {
        getArguments().putSerializable(Extras.CHAPTERS, chapterData);
    }


    @Override
    public void setChapterAdapter(List<Chapter> chapters) {
        if (popUpChapter == null) {
            popUpChapter = new ListPopupWindow(getContext());
            popUpChapter.setAnchorView(tvSelectChapter);
            popUpChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setTopic(null);
                    if (chapterAdapter != null) {
                        setChapter(chapterAdapter.getItem(position));
                    } else {
                        setChapter(null);
                    }
                    popUpChapter.dismiss();
                }
            });
        }
        if (chapterAdapter == null) {
            chapterAdapter = new ChapterAdapter(getContext());
            Chapter chapter = getChapter();
            if (chapter != null) {
                setChapter(chapter);
            }
        }
        chapterAdapter.update(chapters);
        popUpChapter.setAdapter(chapterAdapter);
    }


    @Override
    public void setSubject(Subject subject) {
        if (subject != null) {
            tvSelectSubject.setText(subjectAdapter.getSubjectName(subject));
            mPresenter.getChapters(subject.getSubject().getId());
        } else {
            tvSelectSubject.setText(R.string.hint_select_subject);
            mPresenter.getChapters(-1);
        }
        getArguments().putParcelable(Extras.SUBJECT, subject);
    }

    @Override
    public Subject getSubject() {
        return getArguments().getParcelable(Extras.SUBJECT);
    }

    @Override
    public HashMap<Integer, List<Subject>> getSubjectData() {
        return (HashMap<Integer, List<Subject>>) getArguments().getSerializable(Extras.SUBJECTS);
    }

    @Override
    public void setSubjectData(HashMap<Integer, List<Subject>> subjectData) {
        getArguments().putSerializable(Extras.SUBJECTS, subjectData);
    }

    @Override
    public void setSubjectAdapter(List<Subject> subjects) {
        if (popUpSubject == null) {
            popUpSubject = new ListPopupWindow(getContext());
            popUpSubject.setAnchorView(tvSelectSubject);
            popUpSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setChapter(null);
                    if (subjectAdapter != null) {
                        setSubject(subjectAdapter.getItem(position));
                    } else {
                        setSubject(null);
                    }
                    popUpSubject.dismiss();
                }
            });
        }
        if (subjectAdapter == null) {
            subjectAdapter = new SubjectAdapter(getContext());
            Subject subject = getSubject();
            if (subject != null) {
                setSubject(subject);
            }
        }
        subjectAdapter.update(subjects);
        popUpSubject.setAdapter(subjectAdapter);
    }

    @Override
    public boolean isError() {
        boolean isError = getSubject() == null;
        tvSelectSubject.setError(null);
        tvSelectChapter.setError(null);
        tvSelectTopic.setError(null);
        if (isError) {
            tvSelectSubject.setError(getString(R.string.validate_spinner, getString(R.string.hint_subject)));
            return isError;
        }
        isError = getChapter() == null;
        if (isError) {
            tvSelectChapter.setError(getString(R.string.validate_spinner, getString(R.string.hint_chapter)));
            return isError;
        }
        isError = getTopic() == null;
        if (isError) {
            tvSelectTopic.setError(getString(R.string.validate_spinner, getString(R.string.hint_topic)));
            return isError;
        }
        return isError;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_subject:
                if (popUpSubject != null)
                    popUpSubject.show();
                break;
            case R.id.tv_select_chapter:
                if (popUpChapter != null)
                    popUpChapter.show();
                break;
            case R.id.tv_select_topic:
                if (popUpTopic != null)
                    popUpTopic.show();
                break;
            case R.id.btn_apply:
                if (!isError() && lmsView != null) {
                    dismiss();
                    lmsView.onActionClick();
                }
                break;
        }
    }


    @Override
    public void dismiss() {
        if (lmsView != null)
            lmsView.setFilterData(getArguments());
        super.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }
}
