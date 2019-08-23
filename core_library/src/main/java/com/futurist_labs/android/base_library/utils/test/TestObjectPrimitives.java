package com.futurist_labs.android.base_library.utils.test;

/**
 * Created by Galeen on 2019-08-13.
 */
public class TestObjectPrimitives {
    @Tester.CheckDefaultPrimitiveInTests
    private int position;
    private String id;
    private Long time;
    @Tester.CheckDefaultPrimitiveInTests
    private boolean isFree;

    public TestObjectPrimitives(int position, String id, Long time, boolean isFree) {
        this.position = position;
        this.id = id;
        this.time = time;
        this.isFree = isFree;
    }
}
