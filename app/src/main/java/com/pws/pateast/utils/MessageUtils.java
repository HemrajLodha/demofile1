package com.pws.pateast.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.pws.pateast.R;


/**
 * Created by intel on 9/9/2016.
 */
public class MessageUtils
{
    private static Snackbar actionSnackBar;

    public static void snackBarWithAction(Context context, View root, String errorMsg, int color)
    {
        actionSnackBar = Snackbar.make(root, errorMsg, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = actionSnackBar.getView();
        snackbarView.setBackgroundColor(ColorUtil.getAttrColor(context,color));
        actionSnackBar.setActionTextColor(ContextCompat.getColor(context, R.color.white));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context,R.color.white));
        actionSnackBar.setAction(R.string.ok
                , new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                actionSnackBar.dismiss();

            }
        });
        actionSnackBar.show();
    }

    public static void snackBarWithoutAction(Context context,View root,String errorMsg,int color)
    {
        actionSnackBar = Snackbar.make(root, errorMsg, Snackbar.LENGTH_SHORT);
        View snackbarView = actionSnackBar.getView();
        snackbarView.setBackgroundColor(ColorUtil.getAttrColor(context,color));
        actionSnackBar.setActionTextColor(ContextCompat.getColor(context,R.color.white));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context,R.color.white));
        actionSnackBar.show();
    }

    public static void hideSnackBar()
    {
        if(actionSnackBar != null)
            actionSnackBar.dismiss();
    }
}
