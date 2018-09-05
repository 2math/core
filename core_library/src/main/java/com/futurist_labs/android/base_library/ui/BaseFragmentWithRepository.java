package com.futurist_labs.android.base_library.ui;

import com.futurist_labs.android.base_library.repository.RepositoryFactory;


/**
 * Created by Galeen on 12/22/2017.
 * Has BaseUI object to implement the main logic for show/hide progress, errors and messages
 * Each fragment with presenter as BaseViewModel should extend this one to inherit BaseUI functionality
 * T instance of Repository interface
 * In the app create new AppBasedFragment which will extend from this one and will set Repository type
 */

public class BaseFragmentWithRepository<T> extends BaseFragment {
    public T repository = (T) RepositoryFactory.getInstance().provide();

}
