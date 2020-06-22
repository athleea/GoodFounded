package com.athleea.goodfounded;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Restaurant {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "지역")
    private String place;
    @ColumnInfo(name = "가게명")
    private String name;
    @ColumnInfo(name = "주소")
    private String address;
    @ColumnInfo(name = "업태")
    private String type;
    @ColumnInfo(name = "경도")
    private double latitude;
    @ColumnInfo(name = "위도")
    private double longitude;

    public Restaurant(String place, String name, String address, String type, double latitude, double longitude) {
        this.place = place;
        this.name = name;
        this.address = address;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlace() {
        return place;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", place='" + place + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

