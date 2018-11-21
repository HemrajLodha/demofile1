package com.pws.pateast.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by intel on 04-Jul-17.
 */

public class ProgressDialogFragment extends DialogFragment
{
    private Context mContext;
    private String title;
    private String message;


    public static ProgressDialogFragment newInstance(Context context)
    {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setContext(context);
        return fragment;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(getTitle());
        dialog.setMessage(getMessage());
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }

    @Override
    public Context getContext()
    {
        if(mContext != null)
            return mContext;
        return super.getContext();
    }


    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null && fragment.isAdded()) {
            return;
        }
        super.show(manager, tag);
    }
}
