package com.futurist_labs.android.base_library.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.futurist_labs.android.base_library.repository.RepositoryFactory;

/**
 * Created by Galeen on 6/8/2018.
 */
public abstract class BaseFragmentWithViewModelAndRepository<T, // repository
        K extends BaseViewModelWithRepository<T>> // ViewModel
        extends BaseFragmentWithViewModel<K> {
    public T repository = (T) RepositoryFactory.getInstance().provide();
//    public K viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getViewModelClass() != null) {
//            if (viewModelFromActivity()) {
//                if (this.getActivity() == null) return;
//                viewModel = ViewModelProviders.of(this.getActivity()).get(getViewModelClass());
//            } else {
//                viewModel = ViewModelProviders.of(this).get(getViewModelClass());
//            }
//            getBaseUI().setBaseViewModel(viewModel);
//            setObservers();
//        }
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setObservers();
//    }

//    /**
//     * @return the class of your ViewModel , like OrderViewModel.class
//     */
//    protected abstract Class<K> getViewModelClass();
//
//    /**
//     * Hook your observers here.
//     * This method will be called after viewModel and baseUI are initialized
//     */
//    protected abstract void setObservers();
//
//    /**
//     * @return true if you want your ViewModel to be hooked to the host activity
//     * false to the fragment
//     * by default return false
//     */
//    protected boolean viewModelFromActivity() {
//        return false;
//    }
}
