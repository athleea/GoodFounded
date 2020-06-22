package com.athleea.goodfounded;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    ViewGroup viewGroup;
    Button filterBtn;
    Context context;
    CheckBox[] foodList = new CheckBox[17];
    ArrayList<String> typeList;

    GoogleMap map;
    MapView mapView;

    boolean isPageOpen = false;
    Animation upAnim;
    Animation downAnim;
    LinearLayout slidePage;
    Button applyBtn;
    Button pageCancel;
    Button searchBtn;

    double left;
    double right;
    double top;
    double bottom;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        initView();

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
            addMarkerWithFiltering();
        });

        searchBtn.setOnClickListener(view -> {
            addMarkerWithFiltering();
        });

        return viewGroup;
    }

    void initView(){
        slidePage = viewGroup.findViewById(R.id.slidePage);

        filterBtn = viewGroup.findViewById(R.id.filterBtn);
        applyBtn = viewGroup.findViewById(R.id.applyBtn);
        pageCancel = viewGroup.findViewById(R.id.pageCancel);
        searchBtn = viewGroup.findViewById(R.id.searchBtn);

        context = getContext();

        for (int i = 0; i < foodList.length; i++) {
            String n = "list" + i;
            int resID = getResources().getIdentifier(n, "id", getActivity().getPackageName());

            foodList[i] = viewGroup.findViewById(resID);
        }

        mapView = viewGroup.findViewById(R.id.map);

        upAnim = AnimationUtils.loadAnimation(context, R.anim.translate_up);
        downAnim = AnimationUtils.loadAnimation(context, R.anim.translate_down);
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();

        upAnim.setAnimationListener(animListener);
        downAnim.setAnimationListener(animListener);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.56, 126.97)));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);

        // 카메라 시점 이동
        googleMap.setOnCameraMoveListener(() -> {
            searchBtn.setVisibility(View.VISIBLE);
            VisibleRegion vr = googleMap.getProjection().getVisibleRegion();
            left = vr.latLngBounds.southwest.longitude;
            top = vr.latLngBounds.northeast.latitude;
            right = vr.latLngBounds.northeast.longitude;
            bottom = vr.latLngBounds.southwest.latitude;

        });
    }


    public ArrayList<String> checkTypeList() {
        boolean[] checkList = new boolean[]{false};
        String[] type = getResources().getStringArray(R.array.sectors);

        for (int i = 0; i < foodList.length; i++) {
            if (foodList[i].isChecked()) {
                checkList[i] = true;
            }
        }
        ArrayList<String> checkTypeList = new ArrayList<>();
        for (int i = 0; i < checkList.length; i++) {
            if (checkList[i]) {
                checkTypeList.add(type[i]);
            }
        }
        return checkTypeList;


    }

    public void onAddMarker(List<Restaurant> list) {
        for (int i = 0; i < list.size(); i++) {
            LatLng position = new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude());
            MarkerOptions myMarker = new MarkerOptions().position(position);
            this.map.addMarker(myMarker);
        }


//        // 반경 1KM원
//        CircleOptions circle1KM = new CircleOptions().center(position) //원점
//                .radius(1000)      //반지름 단위 : m
//                .strokeWidth(0f)  //선너비 0f : 선없음
//                .fillColor(Color.parseColor("#880000ff")); //배경색

        //마커추가

//        //원추가
//        this.googleMap.addCircle(circle1KM);
    }

    public void addMarkerWithFiltering() {
        typeList = checkTypeList();
        for (int i = 0; i < typeList.size(); i++) {
            try {
                List<Restaurant> list = XMLParsing.db.restaurantDao().search(typeList.get(i), left, right, top, bottom);
                if (!list.isEmpty()) {
                    Log.e("XML", "주소 없음");
                } else {
                    onAddMarker(list);
                    Log.e("XML", "AddMarker");
                }
            } catch (NullPointerException e) {
                Log.e("XML", e.toString());
            }
        }
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
            Log.e("Animation", "Animation Start");
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
            Log.e("Animation", "Animation repeat");
        }

    }


}
