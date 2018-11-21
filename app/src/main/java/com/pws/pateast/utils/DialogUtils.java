package com.pws.pateast.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.pws.pateast.R;
import com.pws.pateast.base.ProgressDialogFragment;
import com.pws.pateast.listener.AdapterListener;

/**
 * Created by intel on 9/12/2016.
 */

@SuppressWarnings("ALL")
public class DialogUtils {


    public static void cancel(Dialog dia) {
        if (dia != null) {
            dia.dismiss();
        }
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        return getProgressDialog(context, message, true);
    }

    public static ProgressDialog getProgressDialog(Context context, int message) {
        return getProgressDialog(context, context.getString(message));
    }

    public static ProgressDialog getProgressDialog(Context context, int message, boolean cancel) {
        return getProgressDialog(context, context.getString(message), cancel);
    }

    public static ProgressDialog getProgressDialog(Context context, String message, boolean cancel) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setMessage(message);
        pDialog.setCancelable(cancel);
        return pDialog;
    }

    public static ProgressDialogFragment getProgressFragmentDialog(Context context, String message) {
        return getProgressFragmentDialog(context, message, true);
    }

    public static ProgressDialogFragment getProgressFragmentDialog(Context context, int message) {
        return getProgressFragmentDialog(context, context.getString(message));
    }

    public static ProgressDialogFragment getProgressFragmentDialog(Context context, int message, boolean cancel) {
        return getProgressFragmentDialog(context, context.getString(message), cancel);
    }

    public static ProgressDialogFragment getProgressFragmentDialog(Context context, String message, boolean cancel) {
        ProgressDialogFragment pDialog = ProgressDialogFragment.newInstance(context);
        pDialog.setMessage(message);
        pDialog.setCancelable(cancel);
        return pDialog;
    }

    public static void showSingleChoiceDialog(Context context, String title, String[] fileList, int selected, AdapterListener<Integer> listener) {
        showSingleChoiceDialog(context, title, fileList, selected, listener, true, "Ok", "Cancel");
    }

    public static void showSingleChoiceDialog(Context context, String title, final String[] fileList, final AdapterListener<Integer> listener, String... args) {
        showSingleChoiceDialog(context, title, fileList, 0, listener, true, args);
    }

    public static Dialog showSingleChoiceDialog(Context context, String title, final String[] fileList, int selected, final AdapterListener<Integer> listener, boolean cancelable, String... args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(fileList, selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPositive.setEnabled(true);
            }
        });
        builder.setCancelable(cancelable);
        builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                whichButton = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                listener.onClick(AlertDialog.BUTTON_POSITIVE, whichButton);
            }
        });
        if (args.length > 1 && !TextUtils.isEmpty(args[1])) {
            builder.setNegativeButton(args[1], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    //listener.onClick(AlertDialog.BUTTON_NEGATIVE, whichButton);
                }
            });
        }
        Dialog dia = builder.create();
        dia.show();
        if (selected == -1) {
            Button buttonPositive = ((AlertDialog) dia).getButton(AlertDialog.BUTTON_POSITIVE);
            buttonPositive.setEnabled(false);
        }
        return dia;
    }

    public static Dialog showDialog(Context context, String title, String message,
                                    final AdapterListener<String> listener, int... args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        switch (args.length) {
            case 1:
                builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(0, "");
                    }
                });
                break;
            case 2:
                builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(0, "");
                    }
                });
                builder.setNegativeButton(args[1], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(1, "");
                    }
                });
                break;
            case 3:
                builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(0, "");
                    }
                });
                builder.setNegativeButton(args[1], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(1, "");
                    }
                });
                builder.setNeutralButton(args[2], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(2, "");
                    }
                });
                break;
        }
        Dialog dia = builder.create();
        dia.show();
        return dia;
    }

    public static Dialog showDialog(Context context, int title, int message, final AdapterListener<String> listener, int... args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        switch (args.length) {
            case 1:
                builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(0, "");
                    }
                });
                break;
            case 2:
                builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(0, "");
                    }
                });
                builder.setNegativeButton(args[1], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(1, "");
                    }
                });
                break;
            case 3:
                builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(0, "");
                    }
                });
                builder.setNegativeButton(args[1], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(1, "");
                    }
                });
                builder.setNeutralButton(args[2], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(2, "");
                    }
                });
                break;
        }
        Dialog dia = builder.create();
        dia.show();
        return dia;
    }

    public static AlertDialog showDialog(Context context, String title, String message, View view,
                                    final AdapterListener<String> listener, int... args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        EditText etComments = (EditText) view.findViewById(R.id.et_comments);
        switch (args.length) {
            case 1:
                builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null)
                            listener.onClick(0, "");
                    }
                });
                break;
            case 2:
                builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null)
                            listener.onClick(0, "");
                    }
                });
                builder.setNegativeButton(args[1], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null)
                            listener.onClick(1, "");
                    }
                });
                break;
            case 3:
                builder.setPositiveButton(args[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null)
                            listener.onClick(0, "");
                    }
                });
                builder.setNegativeButton(args[1], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null)
                            listener.onClick(1, "");
                    }
                });
                builder.setNeutralButton(args[2], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (listener != null)
                            listener.onClick(2, "");
                    }
                });
                break;
        }
        AlertDialog dia = builder.create();
        dia.setView(view);
        dia.show();
        return dia;
    }
}
