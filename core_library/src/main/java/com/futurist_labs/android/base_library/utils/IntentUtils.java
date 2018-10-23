package com.futurist_labs.android.base_library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import java.util.List;

/**
 * Created by Galeen on 3.5.2016 г..
 */
public class IntentUtils {
    /**
     * Please use {@link IntentUtils#openDialer(Context, String)} instead
     *
     * @param atv   activity
     * @param phone the phone number to be populated
     */
    @Deprecated
    public static void openDailer(Context atv, String phone) {
        openDialer(atv, phone);
    }

    /**
     * Opens call app with the phone number populated
     *
     * @param atv   activity
     * @param phone the phone number to be populated
     */
    public static void openDialer(Context atv, String phone) {
        String uri = "tel:" + phone;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        atv.startActivity(intent);
    }

    public static void openBrowser(Context context, String url) {
        if (url == null) return;
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static boolean openInBrowser(Activity atv, String url, String chooserTitle) {
        if (url == null) return false;
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (browserIntent.resolveActivity(atv.getPackageManager()) != null) {
            if (chooserTitle == null) chooserTitle = "Select app :";
            atv.startActivity(Intent.createChooser(browserIntent, chooserTitle));
            return true;
        } else
            return false;
    }

    public static boolean sendSMS(Context atv, String number, String body, String chooserTitle) {
        Intent intent;
        if (number != null) {
            Uri smsUri = Uri.parse("smsto:" + number);
            intent = new Intent(Intent.ACTION_SENDTO, smsUri);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("vnd.android-dir/mms-sms");
        }
        intent.putExtra("sms_body", body);
        if (intent.resolveActivity(atv.getPackageManager()) != null) {
            atv.startActivity(Intent.createChooser(intent, chooserTitle));
            return true;
        } else
            return false;
    }

    public static boolean sendEmailMessage(Context atv, String subject, String body, String chooserTitle) {
        return sendEmailMessage(atv, "", subject, body, chooserTitle);
    }

    public static boolean sendEmailMessage(Context atv, String eMail, String subject, String body, String chooserTitle) {
        return sendEmailMessage(atv, eMail, subject, body, null, chooserTitle, null);
    }

    public static boolean sendEmailMessage(Context atv, String eMail, String subject, String body, String htmlBody, String chooserTitle, Uri attachmentUri) {
        return sendEmailMessageToContacts(atv, subject, body, htmlBody, chooserTitle, new String[]{eMail}, null, null, attachmentUri);
    }

    /**
     * Starts an Android email intent.
     * The presenterCallback, subject, and bodyText fields are required.
     * You can pass a null field for the chooserTitle, to, cc, and bcc fields if
     * you don't want to specify them.
     */
    public static boolean sendEmailMessageToContacts(Context context,
                                                     String subject,
                                                     String bodyText,
                                                     String htmlBody,
                                                     String chooserTitle,
                                                     String[] toRecipients,
                                                     String[] ccRecipients,
                                                     String[] bccRecipients,
                                                     Uri attachmentUri) {
        Intent mailIntent = new Intent();
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (htmlBody != null) {
            mailIntent.putExtra(Intent.EXTRA_HTML_TEXT, htmlBody);// SystemUtils.parseHtml(bodyText));
            mailIntent.putExtra(Intent.EXTRA_TEXT, SystemUtils.parseHtml(htmlBody));
        } else {
            mailIntent.putExtra(Intent.EXTRA_TEXT, bodyText);
        }

        if (attachmentUri != null) {
            mailIntent.setAction(Intent.ACTION_SEND);
            mailIntent.setType("message/rfc822");
            mailIntent.putExtra(Intent.EXTRA_STREAM, attachmentUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                List<ResolveInfo> resInfoList =
                        context.getPackageManager()
                                .queryIntentActivities(mailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    context.grantUriPermission(packageName, attachmentUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }
        } else {
            mailIntent.setAction(Intent.ACTION_SENDTO);
            // For only email app should handle this intent
            mailIntent.setData(Uri.parse("mailto:"));
        }

        if (toRecipients != null) {
            mailIntent.putExtra(Intent.EXTRA_EMAIL, toRecipients);
        }
        if (ccRecipients != null) {
            mailIntent.putExtra(Intent.EXTRA_CC, ccRecipients);
        }
        if (bccRecipients != null) {
            mailIntent.putExtra(Intent.EXTRA_BCC, bccRecipients);
        }
        if (chooserTitle == null) chooserTitle = "Send Email";
        if (mailIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(mailIntent, chooserTitle));
            return true;
        } else
            return false;
    }

    /**
     * @param place ="latitude,longitude"
     */
    public static boolean openNavigationWithChooser(Context atv, String place, String chooserTitle) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + place);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
            atv.startActivity(mapIntent);
            return true;
        } else {
            mapIntent.setPackage(null);//remove restriction to Google Maps
            if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
                atv.startActivity(Intent.createChooser(mapIntent, chooserTitle));
                return true;
            } else {
                mapIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://maps.google.com/maps?daddr=" + place));// to:[address3] to: [address4]"));
                if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
                    atv.startActivity(Intent.createChooser(mapIntent, chooserTitle));
                    return true;
                } else {
                    mapIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Uri.encode("waze://?ll=" + place + "&navigate=yes", "@#&=*+-_.,:!?()/~'%")));// to:[address3] to: [address4]"));
                    if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
                        atv.startActivity(Intent.createChooser(mapIntent, chooserTitle));
                        return true;
                    } else
                        return false;
                }
            }
        }
    }

    public static boolean openNavigationFromToWithChooser(Context atv, String addressFrom, String addressTo, String
            chooserTitle) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/maps?saddr=" + addressFrom + "&daddr=" + addressTo));// to:[address3] to: [address4]"));
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
            atv.startActivity(mapIntent);
            return true;
        } else {
            mapIntent.setPackage(null);
            if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
                atv.startActivity(Intent.createChooser(mapIntent, chooserTitle));
                return true;
            } else
                return false;
        }
    }

    /*Example for place "Taronga+Zoo,+Sydney+Australia"
    google.navigation:q=a+street+address
    google.navigation:q=latitude,longitude*/
    public static boolean openGoogleNavigation(Context atv, String place) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + place);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
            atv.startActivity(mapIntent);
            return true;
        } else
            return false;
    }

    public static boolean openGoogleNavigation(Context atv, String addressFrom, String addressTo) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/maps?saddr=" + addressFrom + "&daddr=" + addressTo));// to:[address3] to: [address4]"));
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
            atv.startActivity(mapIntent);
            return true;
        } else
            return false;
//        atv.startActivity(intent);
//        return false;
    }

    /**
     * @param chooserTitle if = null than intents to GoogleMaps
     *                     Uri gmmIntentUri = Uri.parse("geo:-33.8666,151.1957");
     */
    public static boolean openMap(Context atv, String lat, String longitude, String chooserTitle) {
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        if (chooserTitle == null)
            mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
            if (chooserTitle == null)
                atv.startActivity(mapIntent);
            else
                atv.startActivity(Intent.createChooser(mapIntent, chooserTitle));
            return true;
        } else
            return false;
    }

    /**
     * @param chooserTitle if = null than intents to GoogleMaps
     *                     Uri gmmIntentUri = Uri.parse("geo:0,0?q=-33.8666,151.1957(Google+Sydney)");
     */
    public static boolean openMapWithMarker(Context atv, String lat, String longitude, String markerLabel, String
            chooserTitle) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + longitude + "(" + markerLabel + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        if (chooserTitle == null)
            mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
            if (chooserTitle == null)
                atv.startActivity(mapIntent);
            else
                atv.startActivity(Intent.createChooser(mapIntent, chooserTitle));
            return true;
        } else
            return false;
    }

    /**
     * @param chooserTitle if = null than intents to GoogleMaps
     *                     Uri gmmIntentUri = Uri.parse("geo:0,0?q=address(Google+Sydney)");
     */
    public static boolean openMapWithMarker(Context atv, String address, String chooserTitle) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        if (chooserTitle == null)
            mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(atv.getPackageManager()) != null) {
            if (chooserTitle == null)
                atv.startActivity(mapIntent);
            else
                atv.startActivity(Intent.createChooser(mapIntent, chooserTitle));
            return true;
        } else
            return false;
    }

    public static void openTwitter(Context atv, String userId) {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            atv.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + userId));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + userId));
        }
        atv.startActivity(intent);
    }


    public static void openInstagram(Context atv, String userId) {
        Intent intent = null;
        try {
            // get the Instagram app if possible
            atv.getPackageManager().getPackageInfo("com.instagram.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + userId));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Instagram app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + userId));
        }
        atv.startActivity(intent);
    }

    public static void shareTextUrlFacebook(Activity context, String url, int title) {
        Intent share = findFacebookClient(context);
        if (share == null) {
            openInBrowser(context, "http://facebook.com", context.getResources().getString(title));
        } else {
            if (url != null)
                share.putExtra(Intent.EXTRA_TEXT, url);
            context.startActivity(Intent.createChooser(share, context.getResources().getString(title)));
        }
    }

    public static void shareTextUrlTW(Activity context, String url, int title) {
        Intent share = findTwitterClient(context);
        if (share == null) {
            openInBrowser(context, "http://twitter.com", context.getResources().getString(title));
        } else {
            if (url != null)
                share.putExtra(Intent.EXTRA_TEXT, url);
            context.startActivity(Intent.createChooser(share, context.getResources().getString(title)));
        }
    }

    public static Intent findTwitterClient(Context context) {
        final String[] twitterApps = {
                // package // name - nb installs (thousands)
                "com.twitter.android", // official - 10 000
                "com.twidroid", // twidroid - 5 000
                "com.handmark.tweetcaster", // Tweecaster - 5 000
                "com.thedeck.android"}; // TweetDeck - 5 000 };
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.setType("text/plain");
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(
                tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (int i = 0; i < twitterApps.length; i++) {
            for (ResolveInfo resolveInfo : list) {
                String p = resolveInfo.activityInfo.packageName;
                if (p != null && p.startsWith(twitterApps[i])) {
                    tweetIntent.setPackage(p);
                    return tweetIntent;
                }
            }
        }
        return null;
    }

    public static Intent findFacebookClient(Context context) {
        final String[] twitterApps = {
                "com.facebook.katana"};
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.setType("text/plain");
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(
                tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (int i = 0; i < twitterApps.length; i++) {
            for (ResolveInfo resolveInfo : list) {
                String p = resolveInfo.activityInfo.packageName;
                if (p != null && p.startsWith(twitterApps[i])) {
                    tweetIntent.setPackage(p);
                    return tweetIntent;
                }
            }
        }
        return null;
    }

    public static void openAppInPlayStore(Context ctx, String appId) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(appId));
        intent.setPackage("com.android.vending");//Play store app
        if(intent.resolveActivity(ctx.getPackageManager()) == null){
            intent.setPackage(null);// open in browser
        }
        ctx.startActivity(intent);
    }
}
