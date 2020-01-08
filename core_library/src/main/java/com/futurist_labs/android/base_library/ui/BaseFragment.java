package com.futurist_labs.android.base_library.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Created by Galeen on 12/22/2017.
 * Has BaseUI object to implement the main logic for show/hide progress, errors and messages
 * Each fragment with presenter as BaseViewModel should extend this one to inherit BaseUI functionality
 * Also notifies the BaseActivity on view created that is on top
 * If you have repository extend from BaseFragmentWithRepository
 * If tou have a ViewModel too extend from BaseFragmentWithViewModelAndRepository
 */

public class BaseFragment extends Fragment {
    private BaseUI baseUI;
    private Events eventsCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseUI = new BaseUI(this, getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            eventsCallback = (Events) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseUI.setParentView(view);
        if(eventsCallback!=null) eventsCallback.setTopFragment(this);
        if(BaseLibraryConfiguration.getInstance().getCrashReporter()!=null){
            BaseLibraryConfiguration.getInstance().getCrashReporter().addEvent(getClass().getName()+".onViewCreated");
        }
    }

    @Override
    public void onDetach() {
        baseUI.hideProgresses();
        eventsCallback=null;
        super.onDetach();
    }

    public BaseUI getBaseUI() {
        return baseUI;
    }

    public interface Events{
        void setTopFragment(BaseFragment topFragment);
    }

    /**
     * BaseActivity will ask the top fragment if can handle the onBackPressed
     * you can override this method in your fragment to handle it.
     * Your Activity(extended from BaseActivity) should have it's backPressed method like :
     * <pre>
     * {@code
     *  public void onBackPressed() {
     *           if(!onBackPressed(someData)){
     *               super.onBackPressed();
     *           }
     *       }
     * }
     * </pre>
     * @param data some data to examine
     * @return false to call super(aka pop), true to stop the backPressed is handled
     */
    public boolean onBackPressed(Object data){
        return false;
    }
}
