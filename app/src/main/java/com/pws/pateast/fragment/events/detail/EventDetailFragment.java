package com.pws.pateast.fragment.events.detail;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.MediaHelper;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.ProfileDetailView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.pws.pateast.utils.DateUtils.CHAT_DATE_TEMPLATE;
import static com.pws.pateast.utils.DateUtils.SERVER_DATE_TEMPLATE;

@RuntimePermissions
public class EventDetailFragment extends AppFragment implements EventDetailView {
    private EventDetailPresenter mPresenter;
    private TextView tvEventTitle, tvEventTime, tvEventAttachment;
    private ProfileDetailView venue, dressCode, instructions, details;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_event_details;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.events_details);
        tvEventTitle = (TextView) findViewById(R.id.tv_event_title);
        tvEventTime = (TextView) findViewById(R.id.tv_event_time);
        tvEventAttachment = (TextView) findViewById(R.id.tv_event_attachment);
        venue = (ProfileDetailView) findViewById(R.id.venue);
        dressCode = (ProfileDetailView) findViewById(R.id.dress_code);
        instructions = (ProfileDetailView) findViewById(R.id.instructions);
        details = (ProfileDetailView) findViewById(R.id.details);

        tvEventAttachment.setOnClickListener(this);

        mPresenter = new EventDetailPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getEventDetails();
    }

    @Override
    public int getEventID() {
        return getArguments().getInt(Extras.EVENT_ID);
    }

    @Override
    public void setEventDetail(Events events) {
        tvEventAttachment.setTag(events);
        tvEventTitle.setText(events.getEventdetails().get(0).getTitle());
        tvEventTime.setText(String.format("%s-%s", DateUtils.toDate(DateUtils.parse(events.getStart(), SERVER_DATE_TEMPLATE), CHAT_DATE_TEMPLATE), DateUtils.toDate(DateUtils.parse(events.getEnd(), SERVER_DATE_TEMPLATE), CHAT_DATE_TEMPLATE)));
        if (TextUtils.isEmpty(events.getEventdetails().get(0).getVenue()))
            venue.setDetail(R.string.not_available);
        else
            venue.setDetail(events.getEventdetails().get(0).getVenue());
        if (TextUtils.isEmpty(events.getEventdetails().get(0).getDresscode()))
            dressCode.setDetail(R.string.not_available);
        else
            dressCode.setDetail(events.getEventdetails().get(0).getDresscode());
        if (TextUtils.isEmpty(events.getEventdetails().get(0).getInstructions()))
            instructions.setDetail(R.string.not_available);
        else
            instructions.setDetail(events.getEventdetails().get(0).getInstructions());
        if (TextUtils.isEmpty(events.getEventdetails().get(0).getDetails()))
            details.setDetail(R.string.not_available);
        else
            details.setDetail(Utils.fromHtml(events.getEventdetails().get(0).getDetails()));

        tvEventAttachment.setVisibility(TextUtils.isEmpty(events.getFile()) ? View.GONE : View.VISIBLE);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @Override
    public void viewFile(Uri uri, String type) {
        Intent intent = MediaHelper.dispatchOpenFileIntent(getContext(), uri, type);
        if (intent != null)
            startActivity(intent);
        else
            Utils.showToast(getContext(), R.string.no_app_exists);
    }

    @Override
    public void onClick(View v) {
        Events events = (Events) v.getTag();
        switch (v.getId()) {
            case R.id.tv_event_attachment:
                EventDetailFragmentPermissionsDispatcher.viewFileWithCheck(this, Uri.parse(ServiceBuilder.IMAGE_URL + events.getFile()), FileUtils.getMimeType(events.getFile()));
                break;
        }
    }
}
