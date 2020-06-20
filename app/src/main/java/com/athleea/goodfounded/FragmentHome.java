package com.athleea.goodfounded;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentHome extends Fragment {
    ViewGroup viewGroup;
    Button foodRM;
    Button placeRM;

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        foodRM = (Button) viewGroup.findViewById(R.id.food_recommend);
        placeRM = (Button) viewGroup.findViewById(R.id.place_recommend);

        foodRM.setOnClickListener(view -> startActivity(new Intent(getActivity(), RecommendFood.class)));
        placeRM.setOnClickListener(view -> startActivity(new Intent(getActivity(), RecommendPlace.class)));


        return viewGroup;
    }



}
