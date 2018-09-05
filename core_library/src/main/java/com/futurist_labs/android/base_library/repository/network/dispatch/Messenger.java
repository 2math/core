package com.futurist_labs.android.base_library.repository.network.dispatch;

import java.util.ArrayList;

/**
 * Created by Galeen on 4.7.2017 г..
 * Singleton object which takes care to keep MessengerEvents callbacks for a specific
 data and update all callbacks when are done.
 The logic is, since we prefetch some data for example myContacts, when you open a screen where my contacts has to be
 in use the app loads the current saved from PersistenceManager and checks the Messenger if myContacts are currently
 downloading from the server. If so will attach to the Messenger a MessengerEvents callback which will be updated
 when my contacts are received from the server. This way we save redundancy of
 calling the same service which is already in progress.
 */

public class Messenger {
    private static Messenger instance;

    private Messenger() {
    }

    public static Messenger getInstance() {
        if (instance == null)
            instance = new Messenger();
        return instance;
    }

    private void notifyTagsDone(ArrayList<MessengerEvents> events, MessengerResult result) {
        for (MessengerEvents messengerEvents : events) {
            if (messengerEvents != null)
                messengerEvents.onReady(result);
        }
        events.clear();
    }


    public static void clearAll() {
        instance = null;
    }

    private ArrayList<MessengerEvents> myTripsEvents = new ArrayList<>();

    public boolean  isGettingTrips;

    public void addToMyTripsEvents(MessengerEvents messengerEvents) {
        this.myTripsEvents.add(messengerEvents);
    }

    public void notifyMyMyTripsAreDone(Object res) {
        notifyTagsDone(myTripsEvents,new MessengerResult(true,res));
    }



}
