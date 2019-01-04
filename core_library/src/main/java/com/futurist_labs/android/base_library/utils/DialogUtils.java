package com.futurist_labs.android.base_library.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.futurist_labs.android.base_library.R;


/**
 * Created by Galeen on 7.1.2016 г..
 */
public class DialogUtils {
//    public static void showInfoDialog(Context context, String message, String title) {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        if (title != null)
//            dialog.setTitle(title);
//        if (message != null)
//            dialog.setMessage(message);
//        dialog.setPositiveButton(R.string.btn_close,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface di, int i) {
//                        di.dismiss();
//                    }
//                });
////        dialog.setNeutralButton(R.string.btn_change_settings_wifi, new DialogInterface.OnClickListener() {
////
////            @Override
////            public void onClick(DialogInterface di, int i) {
////                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
////            }
////        });
////        dialog.setNegativeButton(R.string.btn_close, null);
//        dialog.show();
//    }

//    public static void openWiFiSettingsDialog(final Context context, String message) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        dialog.setTitle(R.string.msg_no_network);
//        if (message == null)
//            dialog.setMessage(R.string.msg_need_network);
//        else
//            dialog.setMessage(message);
//        dialog.setPositiveButton(R.string.btn_change_settings_3g,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface di, int i) {
//                        context.startActivity(new Intent(
//                                Settings.ACTION_WIRELESS_SETTINGS));
//                    }
//                });
//        dialog.setNeutralButton(R.string.btn_change_settings_wifi, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface di, int i) {
//                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//            }
//        });
//        dialog.setNegativeButton(R.string.btn_close, null);
//        dialog.show();
//    }


    public static void showNoNetworkMsg(View v) {
        Snackbar.make(v, R.string.msg_no_network, Snackbar.LENGTH_LONG).show();
    }

    public static void showError(Activity activity, int message) {
        if (activity != null)
            showInfoSnackbar(activity, message, null, Color.WHITE, Color.WHITE, 0, null);
    }

    public static void showError(View view, String message) {
        if (view != null)
            showInfoSnackbar(view, message, 0, Color.WHITE, Color.WHITE, 0, null);
    }

    public static void showError(View view, int message) {
        if (view != null)
            showInfoSnackbar(view, null, message, Color.WHITE, Color.WHITE, 0, null);
    }

    public static void showError(Activity activity, String message) {
        if (activity != null)
            showInfoSnackbar(activity, 0, message, Color.WHITE, Color.WHITE, 0, null);
    }

    public static void showInfoSnackbar(View v, int message) {
        showInfoSnackbar(v, null, message, Color.WHITE, Color.WHITE, 0, null);
    }

    public static void showInfoSnackbar(View v, String message) {
        showInfoSnackbar(v, message, 0, Color.WHITE, Color.WHITE, 0, null);
//                .setAction(Application.getInstance().getUser().getLang().equalsIgnoreCase(Constants.FR_USER_LANGUAGE)?R.string.english:R.string.french,
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                User user = Application.getInstance().getUser();
//                                if (user.getLang().equalsIgnoreCase(Constants.FR_USER_LANGUAGE)) {
//                                    user.setLang(Constants.EN_USER_LANGUAGE);
//                                    tvLang.setText(R.string.english);
//                                    tv2Lang.setText(R.string.french);
//                                    lang = Constants.EN_APP_LANGUAGE;
//                                } else {
//                                    user.setLang(Constants.FR_USER_LANGUAGE);
//                                    tvLang.setText(R.string.french);
//                                    tv2Lang.setText(R.string.english);
//                                    lang = Constants.FR_APP_LANGUAGE;
//                                }
//                                tv2Lang.setVisibility(View.GONE);
//                                isLanguageUpdated = true;
//                                if(!PostOperationToServer.updateUser(getParentActivity(), updateCallback))
//                                    notifyLanguageChange();
//
//                            }
//                        }
//                ).show();
    }

    public static void showInfoSnackbar(Activity activity, String message) {
        if (activity != null && message != null)
            Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    public static void showInfoSnackbar(Activity activity, int message, String msg, int colBtn, int colMsg, int colBkg, View.OnClickListener listener) {
        if (activity != null && (message != 0 || msg != null)) {
            Snackbar snackbar;
            if (msg != null) {
                snackbar = Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
            } else {
                snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
            // Set the Snackbar action button default text color
            if (colBtn != 0)
                snackbar.setActionTextColor(colBtn);

            // Change the Snackbar default text color
            View snackbarView = snackbar.getView();
            if (colMsg != 0) {
                TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(colMsg);
            }
            // Change the Snackbar default background color
            if (colBkg != 0)
                snackbarView.setBackgroundColor(colBkg);

            if (listener == null)
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        snackbar.dismiss();
                    }
                });
            else
                snackbar.setAction("OK", listener);
            // Display the Snackbar
            snackbar.show();
        }
    }

    public static void showInfoSnackbar(View view, String message, int messageRes, int colBtn, int colMsg, int colBkg, View.OnClickListener listener) {
        if (view != null) {
            Snackbar snackbar = message != null ? Snackbar.make(view, message, Snackbar.LENGTH_LONG) :
                    Snackbar.make(view, messageRes, Snackbar.LENGTH_LONG);
            // Set the Snackbar action button default text color
            if (colBtn != 0)
                snackbar.setActionTextColor(colBtn);

            // Change the Snackbar default text color
            View snackbarView = snackbar.getView();
            if (colMsg != 0) {
                TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(colMsg);
            }
            // Change the Snackbar default background color
            if (colBkg != 0)
                snackbarView.setBackgroundColor(colBkg);

            if (listener == null)
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        snackbar.dismiss();
                    }
                });
            else
                snackbar.setAction("OK", listener);
            // Display the Snackbar
            snackbar.show();
        }
    }

    public static void showInfoSnackbar(Activity activity, int message) {
        if (activity != null && message != 0)
            showInfoSnackbar(activity, message, null, Color.WHITE, Color.WHITE, 0, null);
//            Snackbar.make(presenterCallback.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

//    public static void showSaveAndExitDialog(Activity presenterCallback,DialogInterface.OnClickListener listenerYes,
//                                             DialogInterface.OnClickListener listenerNO){
//        if (presenterCallback != null)
//            showChoiceDialog(presenterCallback, R.string.title_save_and_exit_dialog,R.string.msg_save_and_exit_dialog,listenerYes,
//                    listenerNO, presenterCallback.getString(R.string.btn_yes_save_and_exit_dialog),
//                    presenterCallback.getString(R.string.btn_no_save_and_exit_dialog));
//    }


    public static void showSimpleDialog(Context ctx, String msg) {
        showSimpleDialog(ctx, msg, null);
    }

    public static void showSimpleDialog(Context ctx, int msg) {
        showSimpleDialog(ctx, msg, null);
    }

    public static void showSimpleDialog(Context ctx, int msg, DialogInterface.OnClickListener listener) {
        showSimpleDialog(ctx, -1, msg, null, listener);
    }

    public static void showSimpleDialog(Context ctx, String msg, DialogInterface.OnClickListener listener) {
        showSimpleDialog(ctx, -1, -1, msg, listener);
    }

    private static void showSimpleDialog(Context ctx, int title, int msg, String txtMesage, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setCancelable(false);
        if (title != -1)
            dialog.setTitle(title);
        if (msg != -1) {
            dialog.setMessage(msg);
        } else {
            dialog.setMessage(txtMesage);
        }
        if (listener == null)
            dialog.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        else
            dialog.setPositiveButton(R.string.btn_ok, listener);
        dialog.show();
    }

    public static void showChoiceDialog(Context ctx, int title, int msg, DialogInterface.OnClickListener listenerYes
            , DialogInterface.OnClickListener listenerNo, int btnYes, int btnNo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setCancelable(false);
        if (title != -1)
            dialog.setTitle(title);
        dialog.setMessage(msg);
        if (listenerYes == null)
            dialog.setPositiveButton(btnYes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        else
            dialog.setPositiveButton(btnYes, listenerYes);
        if (listenerNo == null)
            dialog.setNegativeButton(btnNo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        else
            dialog.setNegativeButton(btnNo, listenerNo);
        dialog.show();
    }

    /**
     * Show custom dialog.
     * Even if you set button listeners to null on click the dialog is closed.
     *
     * @param ctx           Context
     * @param msgRes        Main message string resource id, 0 to hide
     * @param msgString     Main message as String, NULL to hide
     * @param msg2Res       Second message string resource id, 0 to hide
     * @param msg2String    Second message as String, NULL to hide
     * @param btnYesRes     string resource id, 0 to hide
     * @param btnYesString  as String, NULL to hide
     * @param btnNoRes      string resource id, 0 to hide
     * @param btnNoString   as String, NULL to hide
     * @param listenerYes   callback to be called on click, can be null
     * @param listenerNo    callback to be called on click, can be null
     * @param listenerClose callback to be called on click, can be null
     * @param callback      it is called right after the dialog view is inflated and prepared. Here you receive the view
     *                      it self and can do extra settings, like set ripple to buttons etc.
     * @param showOnCreate  if true the dialog will be show immediately after is created, on False will just create
     *                      and return the dialog to be show by the caller
     * @param layout        res id of the layout of our custom dialog
     * @param idMsg         res id of TextView1, it expects a TextView or successor
     * @param idMsg2        res id of TextView2, it expects a TextView or successor
     * @param idBtnYes      res id of button Yes, it expects a TextView or successor
     * @param idBtnNo       res id of button Yes, it expects a TextView or successor
     * @param idBtnClose    res id of button Yes, it expects a TextView or successor
     * @return the dialog it self or null if something get wrong
     */
    @Nullable
    public static Dialog showCustomDialog(Context ctx,
                                        @StringRes int msgRes, String msgString,
                                        @StringRes int msg2Res, String msg2String,
                                        @StringRes int btnYesRes, String btnYesString,
                                        @StringRes int btnNoRes, String btnNoString,

                                        final View.OnClickListener listenerYes,
                                        final View.OnClickListener listenerNo,
                                        final View.OnClickListener listenerClose,

                                        DialogEvents callback,
                                        boolean showOnCreate,

                                        @LayoutRes int layout,
                                        int idMsg, int idMsg2, int idBtnYes,
                                        int idBtnNo, int idBtnClose) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        if (dialog.getWindow() == null) return null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (vi == null) return null;

        View view = vi.inflate(layout, null);
        TextView tvMsg = view.findViewById(idMsg);
        TextView tvMsg2 = view.findViewById(idMsg2);
        TextView btnYes = view.findViewById(idBtnYes);
        TextView btnNo = view.findViewById(idBtnNo);
        ImageView btnClose = view.findViewById(idBtnClose);

        setText(msgRes, msgString, tvMsg);

        setText(msg2Res, msg2String, tvMsg2);

        setDialogButton(btnYesRes, btnYesString, listenerYes, dialog, btnYes);

        setDialogButton(btnNoRes, btnNoString, listenerNo, dialog, btnNo);

        if (btnClose != null) {
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (listenerClose != null) listenerClose.onClick(view);
                }
            });
        }

        if (callback != null) {
            callback.onInflated(view);
        }

        dialog.setContentView(view);
        if (showOnCreate) dialog.show();

        return dialog;
    }

    private static void setDialogButton(@StringRes int btnTextRes, String btnTextString, final View.OnClickListener listener, final Dialog dialog, TextView btn) {
        if (btn != null) {
            if (btnTextRes != 0 || btnTextString != null) {
                setText(btnTextRes, btnTextString, btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(view);
                    }
                });
            } else {
                btn.setVisibility(View.GONE);
            }
        }
    }

    private static void setText(@StringRes int textRes, String textString, TextView tv) {
        if (tv != null) {
            if (textRes != 0) {
                tv.setText(textRes);
            } else {
                tv.setText(textString);
            }
        }
    }

    public interface DialogEvents {
        void onInflated(View dialogView);
    }

//    public static AlertDialog openGPSSettingsDialog(final Context context, final SettingsDialogListener listener) {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        dialog.setCancelable(false);
//        dialog.setTitle(R.string.msg_no_gps);
//        dialog.setMessage(R.string.msg_need_gps);
//        dialog.setPositiveButton(R.string.btn_change_settings,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface di, int i) {
//                        if (listener != null)
//                            listener.onGoingToSettings(true);
//                        context.startActivity(new Intent(
//                                Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        di.dismiss();
//                    }
//                });
//        dialog.setNegativeButton(R.string.btn_close,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface di, int i) {
//                        if (listener != null)
//                            listener.onGoingToSettings(false);
//                        di.dismiss();
//
//                    }
//                });
//        return dialog.show();
//    }

//    public static void showInfoDialog(Context ctx, int msgRes, int btnYesRes, int btnNoRes,
//                                      final View.OnClickListener listenerYes, final View.OnClickListener listenerNo) {
//        final Dialog dialog = new Dialog(ctx);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = vi.inflate(R.layout.info_dialog, null);
//        TextView msg = (TextView) view.findViewById(R.id.tvDialogMessage);
//        TextView btnYes = (TextView) view.findViewById(R.id.btnYes);
//        TextView btnNo = (TextView) view.findViewById(R.id.btnNo);
//        msg.setText(msgRes);
//        if (btnYesRes != 0) {
//            btnYes.setText(btnYesRes);
//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                    if (listenerYes != null)
//                        listenerYes.onClick(view);
//                }
//            });
//            RippleUtils.setRippleEffectLessRounded(btnYes);
//        } else {
//            btnYes.setVisibility(View.GONE);
//        }
//        if (btnNoRes != 0) {
//            btnNo.setText(btnNoRes);
//            btnNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                    if (listenerNo != null) {
//                        listenerNo.onClick(view);
//                    }
//                }
//            });
//            RippleUtils.setRippleEffectLessRounded(btnNo);
//        } else {
//            btnNo.setVisibility(View.GONE);
//        }
//        dialog.setContentView(view);
//        dialog.show();
//    }

//    public interface SettingsDialogListener {
//        void onGoingToSettings(boolean flag);
//    }
}
