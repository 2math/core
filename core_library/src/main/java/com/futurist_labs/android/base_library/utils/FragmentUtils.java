package com.futurist_labs.android.base_library.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import com.futurist_labs.android.base_library.R;
import com.futurist_labs.android.base_library.ui.BaseActivity;


/**
 * FragmentUtils
 *
 * @author Galeen
 * @since 9/11/13
 */
public class FragmentUtils {


    public static FragmentBuilder createBuilder(FragmentActivity activity) {
        return new FragmentBuilder(activity);
    }

    public static FragmentBuilder createBuilder(Fragment fragment) {
        return new FragmentBuilder(fragment);
    }


    public static class FragmentBuilder {
        private Fragment fragment;
        private FragmentActivity activity;
        private int containerId;
        private boolean shouldAddToBackStack = true;
        private int enterAnimation = 0;
        private int exitAnimation = 0;
        private int popEnterAnimation = 0;
        private int popExitAnimation = 0;
        private boolean justAddToBackStack = false;


        public FragmentBuilder(FragmentActivity activity) {
            this.activity = activity;
        }

        public FragmentBuilder(Fragment fragment) {
            this.fragment = fragment;
        }

        public FragmentBuilder containerId(int containerId) {
            this.containerId = containerId;
            return this;
        }

        public FragmentBuilder addToBackStack(boolean shouldAddToBackStack) {
            this.shouldAddToBackStack = shouldAddToBackStack;
            return this;
        }

        /**
         * This will only add/replace the fragment to the BackStack not going to process of creation.
         * It is helpful when you have to create flow of fragments at once and instead of creating the
         * fragment and replacing it with another will be just added to the BackStack for redirection.
         * If the fragment needs some spacial data you may wont to create it first and replaced it.
         *
         * @param justAddToBackStack default value is false
         * @return
         */
        public FragmentBuilder justAddToBackstack(boolean justAddToBackStack) {
            this.justAddToBackStack = justAddToBackStack;
            return this;
        }

        public FragmentBuilder setAnimations(int enter, int exit, int returnEnter, int returnExit) {
            this.enterAnimation = enter;
            this.exitAnimation = exit;
            this.popEnterAnimation = returnEnter;
            this.popExitAnimation = returnExit;
            return this;
        }

        public FragmentBuilder setPushAnimations() {
            return setAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in,
                    R.anim.push_right_out);
        }

        public void add(Fragment fragment, String tag) {
            if(isActivityStopped()) return;
            FragmentTransaction ft = getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation,
                            popExitAnimation);
            ft.add(containerId, fragment, tag);
            if (shouldAddToBackStack) ft.addToBackStack(tag);
            ft.setAllowOptimization(justAddToBackStack);//setReorderingAllowed(justAddToBackStack);//API level 26
            ft.commit();
        }

        public void add(Fragment fragment) {
            add(fragment, fragment.getClass().getSimpleName());
        }

        public void replace(Fragment fragment, String tag) {
            replace(fragment, tag, null);
        }

        public void replace(Fragment fragment, String tag, ImageView sharedImageView) {
            if(isActivityStopped()) return;
            FragmentTransaction ft =
                    getFragmentManager().beginTransaction();
            if (sharedImageView != null) {
                ft.addSharedElement(sharedImageView, ViewCompat.getTransitionName(sharedImageView));
            } else {
                ft.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation,
                        popExitAnimation);
            }
            ft.replace(containerId, fragment, tag);
            if (shouldAddToBackStack) ft.addToBackStack(tag);
            ft.setAllowOptimization(justAddToBackStack);//setReorderingAllowed(justAddToBackStack);//API level 26
            ft.commit();
        }

        public void replace(Fragment fragment) {
            replace(fragment, fragment.getClass().getSimpleName());
        }


        public void pop() {
            if(isActivityStopped()) return;
            FragmentManager ft = getFragmentManager();
            ft.popBackStack();
        }

        public void remove(Fragment fragment) {
            if(isActivityStopped()) return;
            FragmentManager ft = getFragmentManager();
            ft.beginTransaction().remove(fragment).commit();
        }

        public void popTo(String tag, boolean inclusive) {
            if(isActivityStopped()) return;
            FragmentManager ft = getFragmentManager();
            ft.popBackStack(tag, inclusive ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
        }

        public void popImmediate() {
            if(isActivityStopped()) return;
            FragmentManager ft = getFragmentManager();
            ft.popBackStackImmediate();
        }

        public void popToImmediate(String tag, boolean inclusive) {
            if(isActivityStopped()) return;
            FragmentManager ft = getFragmentManager();
            Fragment fragment = ft.findFragmentByTag(tag);
            ft.popBackStackImmediate(fragment.getId(), inclusive ? FragmentManager.POP_BACK_STACK_INCLUSIVE : 0);
        }

        public Fragment find(String tag) {
            FragmentManager ft = getFragmentManager();
            return ft.findFragmentByTag(tag);
        }

        private FragmentManager getFragmentManager() {
            return activity != null ? activity.getSupportFragmentManager() : fragment.getChildFragmentManager();
        }

        public Fragment find(int id) {
            FragmentManager ft = getFragmentManager();
            return ft.findFragmentById(id);
        }

        public FragmentBuilder clearBackStack() {
            FragmentManager manager = getFragmentManager();
            if (manager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            return this;
        }

        private boolean isActivityStopped() {
            Activity a;
            if (activity != null) {
                a = activity;
            } else if (fragment != null) {
                a = fragment.getActivity();
            }else{
                return true;
            }
            if(a instanceof BaseActivity){
                return !((BaseActivity) a).isActivityResumed();
            }
            if(a!=null){
                return a.isFinishing();
            }
//            return a.getWindow().getDecorView().getRootView().isShown();
            return true;
        }

        public void clearBackStackUnder(Fragment fragment) {
//            FragmentManager manager = getFragmentManager();
//            if (manager.getBackStackEntryCount() > 0) {
//                int fragmentPosition = getIndex(fragment.getClass().getSimpleName());
//                for (int i = 0; i < fragmentPosition; i++) {
//                    FragmentManager.BackStackEntry frag = manager.getBackStackEntryAt(i);
//                    manager.popBackStack(frag.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                }
//            }
        }

        private int getIndex(String tagName) {
            FragmentManager manager = getFragmentManager();
            for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                if (manager.getBackStackEntryAt(i).getName().equalsIgnoreCase(tagName)) {
                    return i;
                }
            }
            return -1;
        }
    }

}
