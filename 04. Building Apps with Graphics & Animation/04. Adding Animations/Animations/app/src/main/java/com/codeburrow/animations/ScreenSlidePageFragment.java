package com.codeburrow.animations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Create instances of this fragment in the parent activity
 * whenever you need a new page to display to the user.
 */
public class ScreenSlidePageFragment extends Fragment {

    private static final String LOG_TAG = ScreenSlidePageFragment.class.getSimpleName();

    public ScreenSlidePageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        return rootView;
    }
}
