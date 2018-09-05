package com.futurist_labs.android.base_library.ui;

import com.futurist_labs.android.base_library.repository.RepositoryFactory;

/**
 * Created by Galeen on 12/21/2017.
 * if we have a repository extend from you ViewModel from here to have it ready
 * T instance of your Repository interface
 * In the app create new AppBasedViewModel which will extend from this one and will set Repository type
 * and add some app common logic
 */

public abstract class BaseViewModelWithRepository<T> extends BaseViewModel {
    protected T repository = (T) RepositoryFactory.getInstance().provide();


    public T getRepository() {
        return repository;
    }
}
