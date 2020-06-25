package com.athleea.goodfounded;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    String KEY = "614c7472726174683130336579416b48";

    String[] placeName = {"gangnam", "gangbuk", "gangseo", "sb", "yongsan",
            "ddm", "nowon", "gwanak", "junggu", "gangdong",
            "yangcheon", "seongdong", "jongno", "gwangjin", "jdp",
            "jungnang", "seocho", "dobong", "geumcheon", "songpa",
            "mapo", "guro", "sdm", "ep", "dongjak"};

    String[] urlList = {
            //DB 생성 시 오랜 시간이 소요되어 테스트 시 음식점API는 한개만 적용

//           "http://openapi.gangnam.go.kr:8088/" + KEY + "/xml/GnFoodHygieneBizRestaurant/",        //강남구
//          "http://openAPI.gangbuk.go.kr:8088/" + KEY + "/xml/GbFoodHygieneBizRestaurant/",        //강북구
//          "http://openapi.gangseo.seoul.kr:8088/" + KEY + "/xml/GangseoFoodHygieneBizRestaurant/",//강서구
//          "http://openapi.sb.go.kr:8088/" + KEY + "/xml/SbFoodHygieneBizRestaurant/",             //성북구
//          "http://openapi.yongsan.go.kr:8088/" + KEY + "/xml/YsFoodHygieneBizRestaurant/",        //용산구
//
//          "http://openapi.ddm.go.kr:8088/" + KEY + "/xml/DongdeamoonFoodHygieneBizRestaurant/",   //동대문구
//          "http://openapi.nowon.go.kr:8088/" + KEY + "/xml/NwFoodHygieneBizRestaurant/",          //노원구
//          "http://openapi.gwanak.go.kr:8088/" + KEY + "/xml/GaFoodHygieneBizRestaurant/",         //관악구
//          "http://openapi.junggu.seoul.kr:8088/" + KEY + "/xml/JungguFoodHygieneBizRestaurant/",  //중구
//          "http://openapi.gd.go.kr:8088/" + KEY + "/xml/GdFoodHygieneBizRestaurant/",             //강동구
//
//          "http://openapi.yangcheon.go.kr:8088/" + KEY + "/xml/YcFoodHygieneBizRestaurant/",      //양천구
//          "http://openAPI.sd.go.kr:8088/" + KEY + "/xml/SdFoodHygieneBizRestaurant/",             //성동구
//          "http://openAPI.jongno.go.kr:8088/" + KEY + "/xml/JongnoFoodHygieneBizRestaurant/",     //종로구
//          "http://openAPI.gwangjin.go.kr:8088/" + KEY + "/xml/GwangjinFoodHygieneBizRestaurant/", //광진구
//          "http://openAPI.ydp.go.kr:8088/+" + KEY + "/xml/YdpFoodHygieneBizRestaurant/",          //영등포구
//
//          "http://openAPI.jungnang.seoul.kr:8088/" + KEY + "/xml/JungnangFoodHygieneBizRestaurant/",//중랑구
//          "http://openAPI.seocho.go.kr:8088/" + KEY + "/xml/ScFoodHygieneBizRestaurant/",         //서초구
//          "http://openAPI.dobong.go.kr:8088/" + KEY + "/xml/DobongFoodHygieneBizRestaurant/",     //도봉구
//          "http://openAPI.geumcheon.go.kr:8088/" + KEY + "/xml/GeumcheonFoodHygieneBizRestaurant/",//금천구
//          "http://openAPI.songpa.seoul.kr:8088/" + KEY + "/xml/SpFoodHygieneBizRestaurant/",      //송파구
//
//          "http://openAPI.mapo.go.kr:8088/" + KEY + "/xml/MpFoodHygieneBizRestaurant/",           //마포구
//          "http://openAPI.guro.go.kr:8088/" + KEY + "/xml/GuroFoodHygieneBizRestaurant/",         //구로구
//          "http://openAPI.sdm.go.kr:8088/" + KEY + "/xml/SeodaemunFoodHygieneBizRestaurant/",     //서대문구
//          "http://openAPI.ep.go.kr:8088/" + KEY + "/xml/EpFoodHygieneBizRestaurant/",             //은평구
            "http://openAPI.dongjak.go.kr:8088/" + KEY + "/xml/DjFoodHygieneBizRestaurant/"         //동작구구
    };

    Geocoder geocoder;
    Context mContext;

    String name;
    String type;
    String address;
    String closure;
    double latitude;
    double longitude;

    public DatabaseHelper(Context context) {
        super(context, QueryManager.DATABASE_NAME, null, QueryManager.VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(QueryManager.CREATE_RESTAURANT_TABLE);
        new InsertAsyncTask().execute(db);
        Log.d("DB", "테이블 생성");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //DB 생성 시 오랜 시간이 소요되어 버전관리를 어떻게 해야할지 고민이 됩니다..
    }

    private class InsertAsyncTask extends AsyncTask<SQLiteDatabase, Void, Void> {

        @Override
        protected Void doInBackground(SQLiteDatabase... sqLiteDatabases) {
            SQLiteDatabase db = sqLiteDatabases[0];
            geocoder = new Geocoder(mContext);
            try {

                for (int i = 0; i < urlList.length; i++) {
                    int total = 1;

                    for (int j = 1; j < total + 1; j += 1000) {

                        URL url = new URL(urlList[i] + j + "/" + (j + 999));
                        Log.e("DB", url.toString());
                        XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = parserCreator.newPullParser();

                        parser.setInput(url.openStream(), null);

                        int parserEvent = parser.getEventType();

                        while (parserEvent != XmlPullParser.END_DOCUMENT) {
                            if (parserEvent == XmlPullParser.START_TAG) {
                                if (parser.getName().equals("UPSO_NM")) {
                                    parser.next();
                                    name = parser.getText();
                                } else if (parser.getName().equals("SITE_ADDR_RD")) {
                                    parser.next();
                                    address = parser.getText();
                                } else if (parser.getName().equals("SNT_UPTAE_NM")) {
                                    parser.next();
                                    type = parser.getText();
                                } else if (parser.getName().equals("DCB_GBN_NM")) {
                                    parser.next();
                                    closure = parser.getText();
                                } else if (parser.getName().equals("list_total_count")) {
                                    parser.next();
                                    total = Integer.parseInt(parser.getText());
                                }
                            } else if (parserEvent == XmlPullParser.END_TAG && parser.getName().equals("row") && closure == null) {
                                List<Address> list = geocoder.getFromLocationName(address, 1);
                                if (list != null) {
                                    if (list.size() == 0) {
                                        Log.e("DB", "주소 없음");
                                    } else {
                                        latitude = list.get(0).getLatitude();
                                        longitude = list.get(0).getLongitude();
                                        db.execSQL(QueryManager.INSERT_RESTAURANT(placeName[i], name, address, type, latitude, longitude)); //값 DB 저장
                                        Log.e("DB", "Insert");
                                    }
                                }
                            }
                            parserEvent = parser.next();
                        }
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                Log.e("DB", e.toString());
            }
            return null;
        }
    }
}
