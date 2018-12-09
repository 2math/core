package com.futurist_labs.android.base_library.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.futurist_labs.android.base_library.R;
import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.futurist_labs.android.base_library.utils.photo.HttpImageGetter;
import com.futurist_labs.android.base_library.views.font_views.FontHelper;

import java.util.UUID;

//import com.google.android.gms.maps.model.BitmapDescriptor;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by Galeen on 6.1.2017 г..
 * SystemUtils
 */
public class SystemUtils {
    /**
     * <uses-permission android:NAME="android.permission.READ_PHONE_STATE"/>
     */
    public static String getIMEINumber(Context context) {
//        TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        return mngr.getDeviceId();
        return UUID.randomUUID().toString();
    }
    public static void hideKeyboard(Fragment fragment, View v) {
        if(v == null && fragment.getView()!=null){
            v = fragment.getView().getRootView();
        }
        if (v == null) {
            v = new View(fragment.getContext());
        }
        hideKeyboard(v,fragment.getContext());
    }

    public static void hideKeyboard(View v, Context activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            LogUtils.d("hideKeyboard : "+imm.hideSoftInputFromWindow(v.getWindowToken(),  0));
        }
        if(v!=null){
            v.clearFocus();
        }
    }
    public static void hideKeyboard(Activity activity, View v) {
        hideKeyboard(v,activity.getBaseContext());
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus() == null ? activity.findViewById(android.R.id.content) : activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        hideKeyboard(activity,view);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //Manifest.permission.ACCESS_FINE_LOCATION
    public static boolean checkPermission(Activity ctx, String permission, int request_code) {
        if (ActivityCompat.checkSelfPermission(ctx, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(ctx,
                    new String[]{permission},
                    request_code);
            return false;
        } else {
            return true;

        }

    }

    public static void scrollToView(final ScrollView scrollView, final LinearLayout llParent, final View viewToShow) {
        long delay = 1000; //delay to let finish with possible modifications to ScrollView
        scrollView.postDelayed(new Runnable() {
            public void run() {
                Rect textRect = new Rect(); //coordinates to scroll to
                viewToShow.getHitRect(textRect); //fills textRect with coordinates of View relative to its parent (LinearLayout)
                scrollView.requestChildRectangleOnScreen(llParent, textRect, false); //ScrollView will make sure, the given textRect is visible
            }
        }, delay);
    }

    public static void scrollToView(final View scrollView, final View view) {
        if(scrollView==null || view ==null)return;
        view.requestFocus();
        final Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        if (!view.getLocalVisibleRect(scrollBounds)) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    int toScroll = getRelativeTop(view) - getRelativeTop(scrollView);
                    ((ScrollView) scrollView).smoothScrollTo(0, toScroll-120);
                }
            });
        }
    }

    private static int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView()) return myView.getTop();
        else return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    public static void expandToolbar(Activity activity, int appBarLayoutID, int rootLayoutID) {
        AppBarLayout appBarLayout = (AppBarLayout) activity.findViewById(appBarLayoutID);
        CoordinatorLayout rootLayout = (CoordinatorLayout) activity.findViewById(rootLayoutID);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout
                .getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.setTopAndBottomOffset(0);
            behavior.onNestedPreScroll(rootLayout, appBarLayout, null, 0, 1, new int[2]);
        }
    }

    public static boolean setStatusBarColorForActivity(Activity ctx, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ctx.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
            window.setStatusBarColor(ContextCompat.getColor(ctx, color));
            return true;
        } else
            return false;
    }

    public static Point getScreenSize(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //We get width and height in pixels here
//        width = size.x;
//        height = size.y;
        return size;
    }

    public static double getScreenInchSize(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width=dm.widthPixels;
//        int height=dm.heightPixels;
        int dens = dm.densityDpi;
//        double wi=(double)width/(double)dens;
//        double hi=(double)height/(double)dens;
////        double wi=(double)width/dm.xdpi;
////        double hi=(double)height/dm.ydpi;
//        double x = Math.pow(wi,2);
//        double y = Math.pow(hi,2);
//        return  Math.sqrt(x+y);
        return Math.sqrt(((dm.widthPixels / dm.xdpi) * (dm.widthPixels / dm.xdpi))
                + ((dm.heightPixels / dm.ydpi) * (dm.heightPixels / dm.ydpi)));
    }

    public static int getStatusBarHeight(Context context) {
        int result = 40;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    //uncomment imports too
   /* public static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }*/


    private static Boolean isRunningTest;

    public static synchronized boolean isRunningTest() {
        if (null == isRunningTest) {
            try {
                Class.forName(BaseLibraryConfiguration.getInstance().APPLICATION_ID + ".ExampleUnitTest");
                isRunningTest = true;
            } catch (ClassNotFoundException e) {
                isRunningTest = false;
            }
        }
        return isRunningTest;
    }

    public static synchronized Spanned parseHtml(String html) {
        return parseHtml(html, null);
    }

    public static synchronized Spanned parseHtml(String html, HttpImageGetter imageGetter) {
        Spanned spannedText;
        if (Build.VERSION.SDK_INT < 24) {
            spannedText = Html.fromHtml(html, imageGetter, null);
        } else {
            spannedText = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        }
        return spannedText;
    }

    public static CharSequence stripHtml(String s) {
        return Html.fromHtml(s).toString().replace((char) 65532, (char) 32).trim();
//        .replace('\n', (char) 32)
//                .replace((char) 160, (char) 32)
    }

    public static String fixHtmlImages(String html, String url){
        String search = "src=\"";
        String prefix = "http";
        int index = html.indexOf(search);
        while (index != -1) {
            index = index+search.length();
            if(index>html.length()) break;
            if(!html.startsWith(prefix,index)) {
                html = html.substring(0, index)
                        + url
                        + html.substring(index);
            }
            index = html.indexOf(search, index);
        }
        return html;
    }

    public static void loadData(WebView webView, String html, int fontType) {
        if (html == null) return;
        int intColor = BaseLibraryConfiguration.getInstance().getApplication().getResources().getColor(R.color.colorPrimary);
        String hexColor = String.format("#%06X", (0xFFFFFF & intColor));

        String htmlString = FontHelper.setCustomFontForWebView(html,
                fontType, 14, null,hexColor);

        htmlString = SystemUtils.fixHtmlImages(htmlString, "https://www.codehospitality.co.uk");

        webView.loadDataWithBaseURL("", htmlString, "text/html", "UTF-8", "");
    }

    public static String setCustomUrlColorForWebView(String htmlString, int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        return setCustomUrlColorForWebView(htmlString, hexColor);
    }

    public static String setCustomUrlColorForWebView(String htmlString, String hexColor) {
        return "<style>img{display: inline;height: auto;max-width: 100%;}a{color: "
                + hexColor
                + "}</style>"
                + htmlString;
    }

    /**
     * This works only with allCaps = false
     */
    public static void setUnderLineTextView(TextView textView) {
        SpannableString ul = new SpannableString(textView.getText());
        ul.setSpan(new UnderlineSpan(), 0, textView.getText().length(), 0);
        textView.setText(ul);
    }

    /**
     * Will add notification channel if not added already.
     * It will be done on OS >= Oreo 27
     * @param context context
     * @param channelId unique id
     * @param channelName name to show in settings
     * @param importance level to NotificationManager.IMPORTANCE_HIGH
     */
    public static void addNotificationChannel(Context context,String channelId, String channelName, int importance){
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null
                    && notificationManager.getNotificationChannel(channelId) == null) {
                android.app.NotificationChannel notificationChannel = new android.app.NotificationChannel(channelId, channelName, android.app.NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    /**
     * same as {@link #addNotificationChannel(Context, String, String, int)}, but with NotificationManager.IMPORTANCE_HIGH
     * @param context context
     * @param channelId unique id
     * @param channelName name to show in settings
     */
    public static void addNotificationChannel(Context context,String channelId, String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SystemUtils.addNotificationChannel(context, channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        }
    }

    public static void hideSystemUI(Activity activity) {
        if(activity == null) return;
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI(Activity activity) {
        if(activity == null) return;
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
