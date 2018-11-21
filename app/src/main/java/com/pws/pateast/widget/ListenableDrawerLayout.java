package com.pws.pateast.widget;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;


public class ListenableDrawerLayout extends DrawerLayout {

    private OnToggleButtonClickedListener mOnToggleButtonClickedListener;
    private boolean mManualCall;
    private Context context;

    public ListenableDrawerLayout(Context context) {
        super(context);
        this.context=context;
    }

    public ListenableDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

    }

    public ListenableDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }

    /**
     * Sets the listener for the toggle button
     *
     * @param mOnToggleButtonClickedListener
     */
    public void setOnToggleButtonClickedListener(OnToggleButtonClickedListener mOnToggleButtonClickedListener) {
        this.mOnToggleButtonClickedListener = mOnToggleButtonClickedListener;
    }

    /**
     * Opens the navigation drawer manually from code<br>
     * <b>NOTE: </b>Use this function instead of the normal openDrawer method
     *
     * @param drawerView
     */
    public void openDrawerManual(View drawerView) {
        mManualCall = true;
        openDrawer(drawerView);
    }

    /**
     * Closes the navigation drawer manually from code<br>
     * <b>NOTE: </b>Use this function instead of the normal closeDrawer method
     *
     * @param drawerView
     */
    public void closeDrawerManual(View drawerView) {
        mManualCall = true;
        closeDrawer(drawerView);
    }


    @Override
    public void openDrawer(View drawerView) {

        if(( (FragmentActivity)context).getSupportFragmentManager().getBackStackEntryCount()>=1){
            ( (FragmentActivity)context).onBackPressed();
            return;
        }

        // Check for listener and for not manual open
        if (!mManualCall && mOnToggleButtonClickedListener != null) {

            // Notify the listener and behave on its reaction
            if (mOnToggleButtonClickedListener.toggleOpenDrawer()) {
                return;
            }

        }
        // Manual call done
        mManualCall = false;

        // Let the drawer layout to its stuff
        super.openDrawer(drawerView);
    }

    @Override
    public void closeDrawer(View drawerView) {
       /* if(( (FragmentActivity)context).getSupportFragmentManager().getBackStackEntryCount()>=1){
            ( (FragmentActivity)context).onBackPressed();
            return;
        }*/

        // Check for listener and for not manual close
        if (!mManualCall && mOnToggleButtonClickedListener != null) {

            // Notify the listener and behave on its reaction
            if (mOnToggleButtonClickedListener.toggleCloseDrawer()) {
                return;
            }

        }
        // Manual call done
        mManualCall = false;

        // Let the drawer layout to its stuff
        super.closeDrawer(drawerView);
    }

    /**
     * Interface for toggle button callbacks
     */
    public static interface OnToggleButtonClickedListener {

        /**
         * The ActionBarDrawerToggle has been pressed in order to open the drawer
         *
         * @return true if we want to consume the event, false if we want the normal behaviour
         */
        public boolean toggleOpenDrawer();

        /**
         * The ActionBarDrawerToggle has been pressed in order to close the drawer
         *
         * @return true if we want to consume the event, false if we want the normal behaviour
         */
        public boolean toggleCloseDrawer();
    }

}