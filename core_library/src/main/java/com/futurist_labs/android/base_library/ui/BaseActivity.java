package com.futurist_labs.android.base_library.ui;

import android.content.Intent;
import android.os.Bundle;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.futurist_labs.android.base_library.utils.CrashReporter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


/**
 * Created by Galeen on 12/20/2017.
 * Has BaseUI object to implement the main logic for show/hide progress, errors and messages
 * Each activity with presenter as BaseViewModel should extend this one to inherit BaseUI functionality
 * keep track of top fragment in main container
 */

public abstract class BaseActivity<T extends BaseViewModelWithRepository> extends AppCompatActivity implements BaseFragmentWithRepository.Events {
    public static final int REQUEST_CHECK_SETTINGS = 112;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 33;
    private BaseUI baseUI;
    public BaseFragment topFragment;
    public T activityViewModel;
    protected boolean isResumed = false, isStarted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isResumed = true;

        // Create a ViewModel the first time the system calls an presenterCallback's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first presenterCallback.
        baseUI = new BaseUI(this, findViewById(android.R.id.content), this);
        baseUI.setBaseViewModel(activityViewModel =
                ViewModelProviders.of(this).get(getViewModelClass()));
        setObservers();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        CrashReporter reporter = BaseLibraryConfiguration.getInstance().getCrashReporter();
        if(reporter!=null){
            reporter.setScreenName(getClass().getName());
            reporter.addEvent(this.getClass().getSimpleName()+".onCreate");
            reporter.setValue("savedInstanceState",savedInstanceState!=null?"not null":"null");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStarted = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStarted = false;
    }

    //    @Override
//    public void onBackPressed() {
//        if(!onBackPressed(someData)){
//            super.onBackPressed();
//        }
//    }


    public boolean onBackPressed(Object data) {
        return topFragment != null && topFragment.onBackPressed(data);
    }

    public BaseUI getBaseUI() {
        return baseUI;
    }

    @Override
    protected void onDestroy() {
        baseUI.hideProgresses();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void setTopFragment(BaseFragment topFragment) {
        this.topFragment = topFragment;
    }

    public BaseActivity getContext(){
        return this;
    }

    public boolean isActivityResumed() {
        return isResumed;
    }

    public void setResumed(boolean resumed) {
        isResumed = resumed;
    }

    public boolean isVisible() {
        return isStarted;
    }

    protected abstract void setObservers();

    protected abstract @NonNull
    Class<T> getViewModelClass();
}
