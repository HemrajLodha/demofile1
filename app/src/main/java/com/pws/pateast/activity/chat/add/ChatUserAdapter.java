package com.pws.pateast.activity.chat.add;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by intel on 08-Aug-17.
 */

public class ChatUserAdapter extends BaseRecyclerAdapter<UserInfo, ChatUserAdapter.ChatUserHolder> {
    private static final int STUDENT = 0;
    private static final int TEACHER = 1;
    private static final int ADMIN = 2;
    private static final int INSTITUTE = 3;
    private static final int PARENT = 4;

    protected List<UserInfo> mFilterList;

    private UserType userType;

    public ChatUserAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
        mFilterList = new ArrayList<>();
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public void clear() {
        if (mFilterList != null)
            mFilterList.clear();
        super.clear();
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        switch (viewType) {
            case STUDENT:
                return R.layout.item_chat_user;
            case TEACHER:
                return R.layout.item_chat_user;
            case ADMIN:
                return R.layout.item_chat_user;
            case INSTITUTE:
                return R.layout.item_chat_user;
            case PARENT:
                return R.layout.item_chat_user;
            default:
                return R.layout.item_chat_user;
        }
    }

    @Override
    public ChatUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case STUDENT:
                return new StudentHolder(getView(parent, viewType), mItemClickListener);
            case TEACHER:
                return new TeacherHolder(getView(parent, viewType), mItemClickListener);
            case ADMIN:
                return new AdminHolder(getView(parent, viewType), mItemClickListener);
            case INSTITUTE:
                return new InstituteHolder(getView(parent, viewType), mItemClickListener);
            case PARENT:
                return new ParentHolder(getView(parent, viewType), mItemClickListener);
            default:
                return new ChatUserHolder(getView(parent, viewType), mItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    @Override
    public UserInfo getItem(int position) {
        return mFilterList.get(position);
    }

    @Override
    public void update(List<UserInfo> items) {
        super.update(items);
        mFilterList = items;
    }

    public void updateSearch(List<UserInfo> items) {
        mFilterList = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (userType) {
            case STUDENT:
                return STUDENT;
            case TEACHER:
                return TEACHER;
            case ADMIN:
                return ADMIN;
            case INSTITUTE:
                return INSTITUTE;
            case PARENT:
                return PARENT;
            default:
                return super.getItemViewType(position);
        }
    }

    class ChatUserHolder extends BaseItemViewHolder<UserInfo> {
        protected CircleImageView imgUserProfile;
        protected TextView tvUserName;
        protected TextView tvInfoTag1, tvInfoTag2, tvInfoTag3;
        protected TextView tvInfo1, tvInfo2, tvInfo3;

        public ChatUserHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            imgUserProfile = (CircleImageView) findViewById(R.id.img_user_profile);
            tvUserName = (TextView) findViewById(R.id.tv_user_name);
            tvInfoTag1 = (TextView) findViewById(R.id.tv_info_tag_1);
            tvInfoTag2 = (TextView) findViewById(R.id.tv_info_tag_2);
            tvInfoTag3 = (TextView) findViewById(R.id.tv_info_tag_3);
            tvInfo1 = (TextView) findViewById(R.id.tv_info_1);
            tvInfo2 = (TextView) findViewById(R.id.tv_info_2);
            tvInfo3 = (TextView) findViewById(R.id.tv_info_3);
        }

        @Override
        public void bind(UserInfo userInfo) {

        }


    }

    class StudentHolder extends ChatUserHolder {

        public StudentHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
        }

        @Override
        public void bind(UserInfo userInfo) {
            super.bind(userInfo);
            ImageUtils.setImageUrl(getContext(), imgUserProfile, userInfo.getStudent().getUser().getUser_image(), R.drawable.avatar1);

            tvUserName.setText(userInfo.getStudent().getUser().getUserdetails().get(0).getFullname());

            tvInfoTag1.setText(R.string.father);
            tvInfo1.setText(userInfo.getStudent().getStudentdetails().get(0).getFather_name());

            tvInfoTag2.setText(R.string.phone_number);
            tvInfo2.setText(userInfo.getStudent().getFather_contact());

            tvInfoTag3.setVisibility(View.GONE);
            tvInfo3.setVisibility(View.GONE);
        }
    }

    class TeacherHolder extends ChatUserHolder {

        public TeacherHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
        }

        @Override
        public void bind(UserInfo userInfo) {
            super.bind(userInfo);
            ImageUtils.setImageUrl(getContext(), imgUserProfile, userInfo.getUser_image(), R.drawable.avatar1);

            tvUserName.setText(userInfo.getUserdetails().get(0).getFullname());

            tvInfoTag1.setText(R.string.phone_number);
            tvInfo1.setText(userInfo.getMobile());

            tvInfoTag2.setText(R.string.subject);
            tvInfo2.setText(getTeacherSubject(userInfo.getTeacher().getTeachersubjects()));

            tvInfoTag3.setVisibility(View.GONE);
            tvInfo3.setVisibility(View.GONE);
        }

        private String getTeacherSubject(ArrayList<Subject> subjects) {
            StringBuilder builder = new StringBuilder();

            for (Subject subject : subjects) {
                if (builder.length() > 0)
                    builder.append(", ");

                builder.append(subject.getSubject().getSubjectdetails().get(0).getName());
            }

            return builder.toString();
        }
    }

    class AdminHolder extends ChatUserHolder {

        public AdminHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
        }

        @Override
        public void bind(UserInfo userInfo) {
            super.bind(userInfo);
            ImageUtils.setImageUrl(getContext(), imgUserProfile, userInfo.getUser_image(), R.drawable.avatar1);

            tvUserName.setText(userInfo.getUserdetails().get(0).getFullname());

            tvInfoTag1.setText(R.string.role);
            tvInfo1.setText(userInfo.getRole().getRoledetails().get(0).getName());

            tvInfoTag2.setText(R.string.phone_number);
            tvInfo2.setText(userInfo.getMobile());

            tvInfoTag3.setVisibility(View.GONE);
            tvInfo3.setVisibility(View.GONE);
        }
    }

    class InstituteHolder extends ChatUserHolder {

        public InstituteHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
        }

        @Override
        public void bind(UserInfo userInfo) {
            super.bind(userInfo);
            ImageUtils.setImageUrl(getContext(), imgUserProfile, userInfo.getUser_image(), R.drawable.avatar1);

            tvUserName.setText(userInfo.getUserdetails().get(0).getFullname());

            tvInfoTag1.setVisibility(View.GONE);
            tvInfo1.setVisibility(View.GONE);

            tvInfoTag2.setText(R.string.phone_number);
            tvInfo2.setText(userInfo.getMobile());

            tvInfoTag3.setVisibility(View.GONE);
            tvInfo3.setVisibility(View.GONE);
        }
    }

    class ParentHolder extends ChatUserHolder {

        public ParentHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
        }

        @Override
        public void bind(UserInfo userInfo) {
            super.bind(userInfo);
            ImageUtils.setImageUrl(getContext(), imgUserProfile, userInfo.getUser_image(), R.drawable.avatar1);

            tvUserName.setText(userInfo.getUserdetails().get(0).getFullname());

            tvInfoTag1.setVisibility(View.GONE);
            tvInfo1.setVisibility(View.GONE);

            tvInfoTag2.setText(R.string.phone_number);
            tvInfo2.setText(userInfo.getMobile());

            tvInfoTag3.setVisibility(View.GONE);
            tvInfo3.setVisibility(View.GONE);
        }
    }

}
