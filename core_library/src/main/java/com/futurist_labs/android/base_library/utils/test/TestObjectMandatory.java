package com.futurist_labs.android.base_library.utils.test;

/**
 * Created by Galeen on 2019-08-13.
 */
public class TestObjectMandatory {
    private int position;
    @CoreAnnotations.MandatoryForTests
    @CoreAnnotations.CheckEmptyStringTests
    private String id;
    private Long time;
    private boolean isFree;

    public TestObjectMandatory(int position, String id, Long time, boolean isFree) {
        this.position = position;
        this.id = id;
        this.time = time;
        this.isFree = isFree;
    }
}
