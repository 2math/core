package com.criapp_studio.coreapp.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.criapp_studio.coreapp.R;
import com.criapp_studio.coreapp.repository.Repository;
import com.futurist_labs.android.base_library.ui.BaseFragmentWithRepository;
import com.futurist_labs.android.base_library.utils.EtValidator;
import com.futurist_labs.android.base_library.utils.RippleUtils;
import com.futurist_labs.android.base_library.utils.SystemUtils;
import com.futurist_labs.android.base_library.views.font_views.FontTextInputEditTextView;
import com.futurist_labs.android.base_library.views.font_views.FontTextInputLayout;
import com.futurist_labs.android.base_library.views.font_views.FontTextView;

public class LoginFragment extends BaseFragmentWithRepository<Repository> {


    private OnFragmentInteractionListener mListener;
    private FontTextInputLayout ilUsername;
    private FontTextInputEditTextView etUsername;
    private FontTextInputLayout ilPass;
    private FontTextInputEditTextView etPass;
    private FontTextView tvLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        setListeners();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * type oclf to fast get new setOnClickListener, rr/rc/rs to set ripple
     */
    private void setListeners() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null
                        && (
//                                EtValidator.isValidPassword(getActivity(), etPass, ilPass, getString(R.string.err_short_pass))
//                        &
                        EtValidator.isValidEmail(getActivity(), etUsername, ilUsername, getString(R.string.err_invalid_email)))) {
                    SystemUtils.hideKeyboard(etPass, getActivity());
                    mListener.doLogin(etUsername.toText(), etPass.toText());
                }
            }
        });

        RippleUtils.setRippleEffectSquare(tvLogin);
    }

    private void initView(View view) {
        ilUsername = view.findViewById(R.id.ilUsername);
        etUsername = view.findViewById(R.id.etUsername);
        ilPass = view.findViewById(R.id.ilPass);
        etPass = view.findViewById(R.id.etPass);
        tvLogin = view.findViewById(R.id.tvLogin);
    }

    public interface OnFragmentInteractionListener {
        void doLogin(String username, String password);
    }
}
