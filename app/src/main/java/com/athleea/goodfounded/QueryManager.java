package com.athleea.goodfounded;

public abstract class QueryManager {

    public static final String DATABASE_NAME = "Restaurant.db";
    public static final int VERSION = 1;

    public static final String CREATE_RESTAURANT_TABLE = "create table if not exists Restaurant(" +
            "id integer PRIMARY KEY autoincrement," +
            "place text," +
            "name text," +
            "address text," +
            "type text," +
            "latitude double," +
            "longitude double" +
            ")";

    public static final String INSERT_RESTAURANT(String place, String name, String address, String type, double latitude, double longitude) {
        return "insert into Restaurant (place,name,address,type,latitude,longitude) VALUES('" +
                place + "','" +
                name + "','" +
                address + "','" +
                type + "'," +
                latitude + "," +
                longitude + ")";
    }

    public static final String SELECT_RESTAURANT_SEARCH(String type, double left, double right, double top, double bottom) {
        return "SELECT " +
                "name," +
                "type," +
                "address, " +
                "latitude, " +
                "longitude " +
                "from Restaurant WHERE type like '" + type + "' AND latitude >=" + bottom + " AND latitude <="+ top + " AND longitude >="+ left+ " AND longitude <="+ right;
    }

}
