package com.base.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.base.ui.BaseActivity;


public abstract class BaseFragment<Data extends Parcelable> extends Fragment {

    private View view;
    protected Context mContext;
    protected Data mData;
    protected LayoutInflater mInflater;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mInflater = LayoutInflater.from(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = mInflater.inflate(getResourceLayout(), container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewReady(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(hasOptionMenu());
        if (savedInstanceState != null) {
            mData = savedInstanceState.getParcelable("mDatas");
        }

    }

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        this.mData = data;
    }

    protected abstract int getResourceLayout();

    protected abstract boolean hasOptionMenu();

    protected abstract void onViewReady(@Nullable Bundle savedInstanceState);

    public final <T extends View> T findViewById(@IdRes int id) {
        return view.findViewById(id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("mDatas", mData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mData = null;
        super.onDestroy();
    }

    protected void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    protected ActionBar getSupportActionBar() {
        return ((BaseActivity) getActivity()).getSupportActionBar();
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
    }

    protected Activity getBaseActivity() {
        return getActivity();
    }
}
