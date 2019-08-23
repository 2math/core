package com.futurist_labs.android.base_library.utils.test;

/**
 * Created by Galeen on 2019-08-13.
 */
public class TestObjectExcludes {
    private int position;
    private String id;
    @Tester.ExcludeFromTests
    private Long time;
    private boolean isFree;

    public TestObjectExcludes(int position, String id, Long time, boolean isFree) {
        this.position = position;
        this.id = id;
        this.time = time;
        this.isFree = isFree;
    }
}
