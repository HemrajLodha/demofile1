package com.pws.pateast.fragment.circular.detail;

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
import com.pws.pateast.api.model.Circular;
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
public class CircularDetailFragment extends AppFragment implements CircularDetailView {
    private CircularDetailPresenter mPresenter;
    private TextView tvCircularTitle, tvCircularTime, tvCircularAttachment;
    private ProfileDetailView circularNumber, description;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_circular_details;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.circular_details);
        tvCircularTitle = (TextView) findViewById(R.id.tv_circular_title);
        tvCircularTime = (TextView) findViewById(R.id.tv_circular_time);
        tvCircularAttachment = (TextView) findViewById(R.id.tv_circular_attachment);
        circularNumber = (ProfileDetailView) findViewById(R.id.circular_number);
        description = (ProfileDetailView) findViewById(R.id.description);

        tvCircularAttachment.setOnClickListener(this);

        mPresenter = new CircularDetailPresenter();
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
        mPresenter.getCircularDetails();
    }

    @Override
    public int getCircularID() {
        return getArguments().getInt(Extras.CIRCULAR_ID);
    }

    @Override
    public void setCircularDetail(Circular circular) {
        tvCircularAttachment.setTag(circular);
        tvCircularTitle.setText(circular.getCirculardetails().get(0).getTitle());
        tvCircularTime.setText(String.format("%s", DateUtils.toDate(DateUtils.parse(circular.getDate(), SERVER_DATE_TEMPLATE), CHAT_DATE_TEMPLATE)));
        if (TextUtils.isEmpty(circular.getNumber()))
            circularNumber.setDetail(R.string.not_available);
        else
            circularNumber.setDetail(circular.getNumber());
        if (TextUtils.isEmpty(circular.getCirculardetails().get(0).getDetails()))
            description.setDetail(R.string.not_available);
        else
            description.setDetail(Utils.fromHtml(circular.getCirculardetails().get(0).getDetails()));

        tvCircularAttachment.setVisibility(TextUtils.isEmpty(circular.getFile()) ? View.GONE : View.VISIBLE);
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
        Circular circular = (Circular) v.getTag();
        switch (v.getId()) {
            case R.id.tv_circular_attachment:
                CircularDetailFragmentPermissionsDispatcher.viewFileWithCheck(this, Uri.parse(ServiceBuilder.IMAGE_URL + circular.getFile()), FileUtils.getMimeType(circular.getFile()));
                break;
        }
    }
}
