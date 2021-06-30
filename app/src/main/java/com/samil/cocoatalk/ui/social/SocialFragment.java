package com.samil.cocoatalk.ui.social;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samil.cocoatalk.R;
import com.samil.cocoatalk.databinding.SocialFragmentBinding;

public class SocialFragment extends Fragment {

    private SocialViewModel socialViewModel;
    private SocialFragmentBinding binding;

    public static SocialFragment newInstance() {
        return new SocialFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        socialViewModel = new ViewModelProvider()
        return inflater.inflate(R.layout.social_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        socialViewModel = new ViewModelProvider(this).get(SocialViewModel.class);
        // TODO: Use the ViewModel
    }

}