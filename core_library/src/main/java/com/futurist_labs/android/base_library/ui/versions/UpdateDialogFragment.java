package com.futurist_labs.android.base_library.ui.versions;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.futurist_labs.android.base_library.R;
import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.futurist_labs.android.base_library.utils.IntentUtils;
import com.futurist_labs.android.base_library.utils.versions.Versions;
import com.futurist_labs.android.base_library.views.font_views.FontTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateDialogFragment extends DialogFragment {
    private static final String ARG_VERSIONS = "param1";
    private Callback callback;
    private Versions versions;
    private FontTextView tvNextTime;
    private FontTextView tvGoToStore;

    public UpdateDialogFragment() {
        // Required empty public constructor
    }

    public static UpdateDialogFragment newInstance(Versions versions) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_VERSIONS, versions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            versions = getArguments().getParcelable(ARG_VERSIONS);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_dialog, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setCancelable(false);

        tvNextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callback != null) {
                    callback.onDismiss();
                }
            }
        });

        tvGoToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openAppInPlayStore(getContext(),
                        BaseLibraryConfiguration.getInstance().APPLICATION_ID);
                if (callback != null) {
                    callback.goToStore();
                }
                dismiss();
            }
        });
    }

    private void initView(View view) {
        tvNextTime = view.findViewById(R.id.tvNextTime);
        tvGoToStore = view.findViewById(R.id.tvGoToStore);
    }


    public interface Callback {
        void onDismiss();

        void goToStore();
    }
}
