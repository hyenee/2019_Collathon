package com.collathon.janolja.ui.pick;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.collathon.janolja.R;

public class PickFragment extends Fragment {

    private PickViewModel pickViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        pickViewModel =
                ViewModelProviders.of(this).get(PickViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pick, container, false);
        final TextView textView = root.findViewById(R.id.text_pick);
        pickViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}