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
    @ColumnInfo(name = "업태명")
    private String type;
    @ColumnInfo(name = "폐업여부")
    private String closure;


    public Restaurant(String place, String name, String address, String type, String closure) {
        this.place = place;
        this.name = name;
        this.address = address;
        this.type = type;
        this.closure = closure;
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

    public String getClosure() {
        return closure;
    }

    public void setClosure(String closure) {
        this.closure = closure;
    }

    public String getPlace() {
        return place;
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
                ", closure='" + closure + '\'' +
                '}';
    }
}
