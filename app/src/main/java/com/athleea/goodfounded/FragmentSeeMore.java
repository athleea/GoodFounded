package com.athleea.goodfounded;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentSeeMore extends Fragment {
    ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_seemore, container, false);

        Button btnSend = (Button)viewGroup.findViewById(R.id.send_btn_seemore);
        btnSend.setOnClickListener(view -> startActivity(new Intent(getActivity(), SendActivity.class)));

        return viewGroup;
    }
}
