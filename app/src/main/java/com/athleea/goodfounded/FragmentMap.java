package com.athleea.goodfounded;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;



public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    ViewGroup viewGroup;
    Button filterBtn;
    Context context;
    CheckBox []foodList = new CheckBox[17];

    GoogleMap map;
    MapView mapView;

    boolean isPageOpen = false;
    Animation upAnim;
    Animation downAnim;
    LinearLayout slidePage;
    Button applyBtn;
    Button pageCancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        slidePage = viewGroup.findViewById(R.id.slidePage);

        filterBtn = viewGroup.findViewById(R.id.filterBtn);
        applyBtn = viewGroup.findViewById(R.id.applyBtn);
        pageCancel = viewGroup.findViewById(R.id.pageCancel);

        for (int i = 0; i < foodList.length; i++) {
            String n = "list" + i;
            int resID = getResources().getIdentifier(n, "id", getActivity().getPackageName());

            foodList[i] = viewGroup.findViewById(resID);
        }

        context = getContext();


        mapView = (MapView) viewGroup.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);


        filterBtn.setOnClickListener(view -> {
            slidePage.setVisibility(View.VISIBLE);
            slidePage.startAnimation(upAnim);
        });
        pageCancel.setOnClickListener(view -> {
            slidePage.setVisibility(View.GONE);
            slidePage.startAnimation(downAnim);
        });
        applyBtn.setOnClickListener(view -> {
            slidePage.setVisibility(View.GONE);
            slidePage.startAnimation(downAnim);
        });


        upAnim = AnimationUtils.loadAnimation(context, R.anim.translate_up);
        downAnim = AnimationUtils.loadAnimation(context, R.anim.translate_down);
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();

        upAnim.setAnimationListener(animListener);
        downAnim.setAnimationListener(animListener);


        return viewGroup;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.56,126.97)));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);

    }

    //현재 위치 버튼 이벤트
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(context, "현재 위치로 이동합니다.", Toast.LENGTH_SHORT).show();
        return false;
    }


    //필터 메뉴 슬라이드
    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                isPageOpen = false;
            } else {
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }


}
