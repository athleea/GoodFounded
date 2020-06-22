package com.athleea.goodfounded;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class XMLParsing {

    public static AppDatabase db;
    private Context context;

    Geocoder geocoder;

    String name;
    String type;
    String address;
    String closure;
    double latitude;
    double longitude;

    String KEY = "614c7472726174683130336579416b48";

    String[] placeName = {"gangnam", "gangbuk", "gangseo", "sb", "yongsan",
            "ddm", "nowon", "gwanak", "junggu", "gangdong",
            "yangcheon", "seongdong", "jongno", "gwangjin", "jdp",
            "jungnang", "seocho", "dobong", "geumcheon", "songpa",
            "mapo", "guro", "sdm", "ep", "dongjak"};

    String[] urlList = {"http://openapi.gangnam.go.kr:8088/" + KEY + "/xml/GnFoodHygieneBizRestaurant/",//강남구
            "http://openAPI.gangbuk.go.kr:8088/" + KEY + "/xml/GbFoodHygieneBizRestaurant/",             //강북구
            "http://openapi.gangseo.seoul.kr:8088/" + KEY + "/xml/GangseoFoodHygieneBizRestaurant/",    //강서구
            "http://openapi.sb.go.kr:8088/" + KEY + "/xml/SbFoodHygieneBizRestaurant/",                 //성북구
            "http://openapi.yongsan.go.kr:8088/" + KEY + "/xml/YsFoodHygieneBizRestaurant/",            //용산구

            "http://openapi.ddm.go.kr:8088/" + KEY + "/xml/DongdeamoonFoodHygieneBizRestaurant/",   //동대문구
            "http://openapi.nowon.go.kr:8088/" + KEY + "/xml/NwFoodHygieneBizRestaurant/",          //노원구
            "http://openapi.gwanak.go.kr:8088/" + KEY + "/xml/GaFoodHygieneBizRestaurant/",         //관악구
            "http://openapi.junggu.seoul.kr:8088/" + KEY + "/xml/JungguFoodHygieneBizRestaurant/",  //중구
            "http://openapi.gd.go.kr:8088/" + KEY + "/xml/GdFoodHygieneBizRestaurant/",             //강동구

            "http://openapi.yangcheon.go.kr:8088/" + KEY + "/xml/YcFoodHygieneBizRestaurant/",      //양천구
            "http://openAPI.sd.go.kr:8088/" + KEY + "/xml/SdFoodHygieneBizRestaurant/",             //성동구
            "http://openAPI.jongno.go.kr:8088/" + KEY + "/xml/JongnoFoodHygieneBizRestaurant/",     //종로구
            "http://openAPI.gwangjin.go.kr:8088/" + KEY + "/xml/GwangjinFoodHygieneBizRestaurant/", //광진구
            "http://openAPI.ydp.go.kr:8088/+" + KEY + "/xml/YdpFoodHygieneBizRestaurant/",          //영등포구

            "http://openAPI.jungnang.seoul.kr:8088/" + KEY + "/xml/JungnangFoodHygieneBizRestaurant/",//중랑구
            "http://openAPI.seocho.go.kr:8088/" + KEY + "/xml/ScFoodHygieneBizRestaurant/",         //서초구
            "http://openAPI.dobong.go.kr:8088/" + KEY + "/xml/DobongFoodHygieneBizRestaurant/",     //도봉구
            "http://openAPI.geumcheon.go.kr:8088/" + KEY + "/xml/GeumcheonFoodHygieneBizRestaurant/",//금천구
            "http://openAPI.songpa.seoul.kr:8088/" + KEY + "/xml/SpFoodHygieneBizRestaurant/",      //송파구

            "http://openAPI.mapo.go.kr:8088/" + KEY + "/xml/MpFoodHygieneBizRestaurant/",           //마포구
            "http://openAPI.guro.go.kr:8088/" + KEY + "/xml/GuroFoodHygieneBizRestaurant/",         //구로구
            "http://openAPI.sdm.go.kr:8088/" + KEY + "/xml/SeodaemunFoodHygieneBizRestaurant/",     //서대문구
            "http://openAPI.ep.go.kr:8088/" + KEY + "/xml/EpFoodHygieneBizRestaurant/",             //은평구
            "http://openAPI.dongjak.go.kr:8088/" + KEY + "/xml/DjFoodHygieneBizRestaurant/"};        //동작구




    public XMLParsing(Context context) {
        this.context = context;
    }

    void parsing() {

        db = AppDatabase.getInstance(context);
        geocoder = new Geocoder(context);

        try {

            for (int i = 0; i < urlList.length; i++) {
                int total = 1;

                for (int j = 1; j < total + 1; j += 1000) {

                    URL url = new URL(urlList[i] + j + "/" + (j + 999));
                    Log.e("XML", url.toString());
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
                                    Log.e("XML", "주소 없음");
                                } else {
                                    latitude = list.get(0).getLatitude();
                                    longitude = list.get(0).getLongitude();
                                    new InsertAsyncTask(db.restaurantDao()).execute(new Restaurant(placeName[i], name, address, type, latitude, longitude)); //값 DB 저장
                                    Log.e("XML", "DB save");
                                }
                            }
                        }
                        parserEvent = parser.next();
                    }
                }
            }
        } catch (IOException | XmlPullParserException e) {
            Log.e("XML", e.toString());
        }
    }

    public List<Restaurant> search(String type, double left, double right, double top, double bottom) {
        return db.restaurantDao().search(type, left, right, top, bottom);
    }

    private static class InsertAsyncTask extends AsyncTask<Restaurant, Void, Void> {

        private RestaurantDao mRestaurantDao;

        public InsertAsyncTask(RestaurantDao todoDao) {
            this.mRestaurantDao = todoDao;
        }

        @Override
        protected Void doInBackground(Restaurant... restaurants) {
            mRestaurantDao.insert(restaurants[0]);
            return null;
        }
    }
}
