package com.futurist_labs.android.base_library.utils;

/**
 * Created by Galeen on 2/9/2018.
 * Facade to Crashlitics
 */

public interface CrashReporter {


    /**
     * Log non-fatal exceptions.
     * Crashlytics only stores the most recent 8 exceptions in a given app session.
     * If your app throws more than 8 exceptions in a session, older exceptions are lost.
     *
     * @param exception exception to log
     */
      void logException(Exception exception);

    /**
     * When a crash is reported, this logs will be sent with it.
     * To avoid slowing down your app, Crashlytics limits logs to 64kB.
     * Crashlytics deletes older log entries if a session's logs go over that limit.
     *
     * @param tag  tag
     * @param body msg
     */
      void log(String tag, String body);

    /**
     * When we have a crash reported, it will indicate which user has it
     *
     * @param identifier user's DB id
     */
    void setUserIdentifier(String identifier);

    /**
     * When we have a crash reported, this values will be sent with it
     * Crashlytics supports a maximum of 64 key/value pairs. Once you reach this threshold,
     * additional values are not saved. Each key/value pair can be up to 1 kB in size.
     *
     * @param key   key
     * @param value json
     */
    void setValue(String key, String value) ;

    /**
     * Marks an event in session. Use this text to tag a session with an event name. Later you can filter sessions
     * where your user passed through this checkpoint, for bettering understanding user experience and behavior.
     *
     * @param eventName name
     */
    void addEvent(java.lang.String eventName);

    /**
     * Set a custom name for the current screen. Useful for applications that don't use more than one Activity.
     * This name is displayed for a given screenshot, and will override the name of the current Activity.
     *
     * @param name ScreenName
     */
    void setScreenName(java.lang.String name);

    /**
     * Log network event
     * @param uri link
     * @param method String
     * @param code int
     * @param startTimeMillis long
     * @param endTimeMillis long
     * @param requestSize int
     * @param responseSize int
     * @param errorMessage String for error
     * @param json String response
     */
    void addNetworkEvent(java.net.URI uri, java.lang.String method, int code, long startTimeMillis, long endTimeMillis, long requestSize, long responseSize, java.lang.String errorMessage, String json);
}
