package com.futurist_labs.android.base_library.repository;

import android.support.annotation.RestrictTo;

import com.futurist_labs.android.base_library.utils.SystemUtils;

/**
 * Created by Galeen on 12/21/2017.
 * Factory to create right Repository for app and test
 * Must be created in Application.onCreate
 */

public class RepositoryFactory<T> {
    private static RepositoryFactory instance;
    private T mockRepo, appRepo;

    /**
     * This must be called only in application.onCreate once
     *
     * @param mockRepo
     * @param appRepo
     */
    public static <T> void  init(T appRepo, T mockRepo){
        if (instance != null) return;
        instance = new RepositoryFactory<>(appRepo,mockRepo);
    }

    private RepositoryFactory(T appRepo, T mockRepo) {
        if (instance != null) return;
        instance = this;
        this.mockRepo = mockRepo;
        this.appRepo = appRepo;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setTestRepository(T dataRepository) {
        appRepo = dataRepository;
        mockRepo = dataRepository;
    }

    public T provide() {
        //if unit testing return mock repository
        if (SystemUtils.isRunningTest() && mockRepo != null) {
            return mockRepo;
        } else {
            return appRepo;
        }
    }

    public static RepositoryFactory getInstance() {
        return instance;
    }
}
