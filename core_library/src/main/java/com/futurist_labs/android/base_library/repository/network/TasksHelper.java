package com.futurist_labs.android.base_library.repository.network;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Galeen on 12.7.2017 г..
 * Keep references to presenterCallback tasks and on destroy cancels them
 */

public class TasksHelper {
    private ArrayList<AsyncTask> tasks = new ArrayList<>();

    public void addTask(AsyncTask task) {
        tasks.add(task);
    }

    /**
     * cancel all running tasks
     */
    public void clearTasks() {
        for (AsyncTask task : tasks) {
            if (task != null && !task.isCancelled())
                task.cancel(true);
        }
        tasks.clear();
    }
}
