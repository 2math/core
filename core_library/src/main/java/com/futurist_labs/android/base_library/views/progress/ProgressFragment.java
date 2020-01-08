package com.futurist_labs.android.base_library.views.progress;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futurist_labs.android.base_library.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {
    private int lastState = -1;
    private AppProgressView progress;

    /**
     *Example how to use it
     * Add in the layout as :
     * <fragment android:id="@+id/progressFrag"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:name="com.futurist_labs.codehospitality.views.progress.ProgressFragment"/>

     Get reference in code as :
     FrameLayout progress = (FrameLayout) FragmentUtils.createBuilder(this).find(R.id.progressFrag).getView();
     */
    public ProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = view.findViewById(R.id.progressView);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onResume() {
        super.onResume();
        if (lastState == -1) {
            progress.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(lastState);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onPause() {
        super.onPause();
        lastState = progress.getVisibility();
        progress.setVisibility(View.INVISIBLE);
    }
}
