package com.athleea.goodfounded;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;


public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    ViewGroup viewGroup;
    Button filterBtn;
    Context context;
    CheckBox[] foodList = new CheckBox[15];
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

        searchBtn.setOnClickListener(view -> addMarkerWithFiltering());

        return viewGroup;
    }

    void initView() {
        slidePage = viewGroup.findViewById(R.id.slidePage);

        filterBtn = viewGroup.findViewById(R.id.filterBtn);
        applyBtn = viewGroup.findViewById(R.id.applyBtn);
        pageCancel = viewGroup.findViewById(R.id.pageCancel);
        searchBtn = viewGroup.findViewById(R.id.searchBtn);


        context = getContext();

        //체크박스 연결
        for (int i = 0; i < foodList.length; i++) {
            String n = "chk" + i;
            int resID = context.getResources().getIdentifier(n, "id", context.getPackageName());
            Log.e("DB", String.valueOf(resID));
            foodList[i] = viewGroup.findViewById(resID);
        }

        mapView = viewGroup.findViewById(R.id.map);

        upAnim = AnimationUtils.loadAnimation(context, R.anim.translate_up);
        downAnim = AnimationUtils.loadAnimation(context, R.anim.translate_down);
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();

        upAnim.setAnimationListener(animListener);
        downAnim.setAnimationListener(animListener);


    }

    //구글 api
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map = googleMap;
        map.setOnMapLoadedCallback(() -> {
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.56, 126.97)));
            map.animateCamera(CameraUpdateFactory.zoomTo(9));
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
        });


        map.setOnCameraIdleListener(() -> {
            searchBtn.setVisibility(View.VISIBLE);
            VisibleRegion vr = map.getProjection().getVisibleRegion();
            left = vr.latLngBounds.southwest.longitude;
            top = vr.latLngBounds.northeast.latitude;
            right = vr.latLngBounds.northeast.longitude;
            bottom = vr.latLngBounds.southwest.latitude;
            Log.e("DB", String.valueOf(left) + String.valueOf(right) + String.valueOf(top) + String.valueOf(bottom));
        });

    }

    //필터메뉴에서 체크된 업태 확인
    public ArrayList<String> checkTypeList() {
        Log.e("DB", "checkTypeList 호출");

        boolean[] checkList = new boolean[15];
        ArrayList<String> checkTypeList = new ArrayList<>();
        String[] type = getResources().getStringArray(R.array.sectors);

        for (int i = 0; i < foodList.length; i++) {
            if (foodList[i] != null && foodList[i].isChecked()) {
                checkList[i] = true;
                Log.e("DB", "checklist : true");
            }
        }

        for (int i = 0; i < checkList.length; i++) {
            if (checkList[i]) {
                checkTypeList.add(type[i]);
            }
        }
        return checkTypeList;


    }

    //DB에서 경도,위도를 가져와 마커만들기
    public void onAddMarker(double latitude, double longitude) {
        LatLng position = new LatLng(latitude, longitude);
        Log.e("DB", String.valueOf(latitude));
        MarkerOptions myMarker = new MarkerOptions().position(position);
        this.map.addMarker(myMarker);
    }

    //DB에서 가져온 데이터를 이용하여 지도에 마커 표시
    public void addMarkerWithFiltering() {
        map.clear();
        Log.e("DB", "마커표시 호출");
        typeList = checkTypeList();
        for (int i = 0; i < typeList.size(); i++) {
            Log.e("DB", String.valueOf(left));
            Cursor cursor = MainActivity.database.rawQuery(QueryManager.SELECT_RESTAURANT_SEARCH(typeList.get(i), left, right, top, bottom), null);
            Log.e("DB", "Select 호출");
            int readCount = cursor.getCount();

            for (int j = 0; j < readCount; j++) {
                cursor.moveToNext();
                onAddMarker(cursor.getDouble(3), cursor.getDouble(4));
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
