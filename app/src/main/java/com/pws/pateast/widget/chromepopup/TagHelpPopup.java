package com.pws.pateast.widget.chromepopup;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.fragment.attendance.teacher.addupdate.AttendanceTagAdapter;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.Arrays;
import java.util.List;


public class TagHelpPopup implements BaseRecyclerAdapter.OnItemClickListener {

    private final RecyclerView rvTag;
    private AttendanceTagAdapter tagAdapter;
    protected WindowManager mWindowManager;

    protected Context mContext;
    protected PopupWindow mWindow;

    private ImageView mUpImageView;
    private ImageView mDownImageView;
    protected ViewGroup mView;

    protected Drawable mBackgroundDrawable = null;
    protected ShowListener showListener;
    protected OnItemClickListener onItemClickListener;


    public TagHelpPopup(Context context, int viewResource, List<Tag> tags, String[] selected) {
        mContext = context;
        mWindow = new PopupWindow(context);

        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setContentView((ViewGroup) layoutInflater.inflate(viewResource, null));

        mUpImageView = mView.findViewById(R.id.arrow_up);
        mDownImageView = mView.findViewById(R.id.arrow_down);
        rvTag = mView.findViewById(R.id.rv_popup);

        ChipsLayoutManager clm = ChipsLayoutManager.newBuilder(context).build();
        rvTag.setLayoutManager(clm);
        rvTag.addItemDecoration(new SpaceItemDecoration(context, R.dimen.size_8, 1, true));

        if (rvTag.getAdapter() == null) {
            tagAdapter = new AttendanceTagAdapter(context);
            rvTag.setAdapter(tagAdapter);
            tagAdapter.setOnItemClickListener(this);
        }
        if (tagAdapter != null) {
            tagAdapter.update(tags);
        }
        if (selected != null)
            tagAdapter.setTags(Arrays.asList(selected));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TagHelpPopup(Context context, List<Tag> tags, String[] selected) {
        this(context, R.layout.tag_popup_view, tags, selected);
    }

    public void show(View anchor) {
        preShow();

        int[] location = new int[2];

        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0]
                + anchor.getWidth(), location[1] + anchor.getHeight());

        final int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        final int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        mView.setLayoutParams(new LayoutParams((int) (screenWidth * 0.5), LayoutParams.WRAP_CONTENT));
        rvTag.getLayoutParams().width = (int) (screenWidth * 0.5);
        int approxHeight = (int) (screenHeight * 0.4);
        rvTag.getLayoutParams().height = approxHeight;

        mView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int rootWidth = (int) (screenWidth * 0.5);
        int rootHeight = mView.getMeasuredHeight();

        int yPos = anchorRect.top - (approxHeight + anchorRect.height());
        boolean onTop = true;
        if (anchorRect.top < screenHeight / 2) {
            yPos = anchorRect.bottom;
            onTop = false;
        }

        int whichArrow, requestedX;

        whichArrow = ((onTop) ? R.id.arrow_down : R.id.arrow_up);
        requestedX = anchorRect.centerX();

        View arrow = whichArrow == R.id.arrow_up ? mUpImageView
                : mDownImageView;
        View hideArrow = whichArrow == R.id.arrow_up ? mDownImageView
                : mUpImageView;

        final int arrowWidth = arrow.getMeasuredWidth();

        arrow.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) arrow
                .getLayoutParams();

        hideArrow.setVisibility(View.INVISIBLE);

        int xPos = 0;

        // ETXTREME RIGHT CLIKED
        if (anchorRect.left + rootWidth > screenWidth) {
            xPos = (screenWidth - rootWidth);
        }
        // ETXTREME LEFT CLIKED
        else if (anchorRect.left - (rootWidth / 2) < 0) {
            xPos = anchorRect.left;
        }
        // IN BETWEEN
        else {
            xPos = (anchorRect.centerX() - (rootWidth / 2));
        }

        param.leftMargin = (requestedX - xPos) - (arrowWidth / 2);


        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);

       /* mView.setAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.float_anim));*/

    }

    protected synchronized void preShow() {
        if (mView == null)
            throw new IllegalStateException("view undefined");


        if (showListener != null) {
            showListener.onPreShow();
            showListener.onShow();
        }

        if (mBackgroundDrawable == null)
            mWindow.setBackgroundDrawable(new BitmapDrawable());
        else
            mWindow.setBackgroundDrawable(mBackgroundDrawable);

        mWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setTouchable(true);
        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);
        mWindow.setContentView(mView);
    }

    public void setBackgroundDrawable(Drawable background) {
        mBackgroundDrawable = background;
    }

    public void setContentView(ViewGroup root) {
        mView = root;
        mWindow.setContentView(root);
    }

    public void setContentView(int layoutResID) {
        LayoutInflater inflator = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView((ViewGroup) inflator.inflate(layoutResID, null));
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mWindow.setOnDismissListener(listener);
    }

    public void dismiss() {
        mWindow.dismiss();
        if (showListener != null) {
            showListener.onDismiss();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (tagAdapter != null) {
            Tag tag = tagAdapter.getItem(position);
            view.setSelected(!view.isSelected());
            if (view.isSelected()) {
                tagAdapter.getTags().add(String.valueOf(tag.getId()));
            } else {
                tagAdapter.getTags().remove(String.valueOf(tag.getId()));
            }
            if (this.onItemClickListener != null) {
                this.onItemClickListener.OnItemClick(view, position, tagAdapter.getTags());
            }
        }
        dismiss();
    }

    public static interface ShowListener {
        void onPreShow();

        void onDismiss();

        void onShow();
    }

    public void setShowListener(ShowListener showListener) {
        this.showListener = showListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position, List<String> tags);
    }
}