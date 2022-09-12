package com.example.g_store.model;

import java.io.Serializable;
import java.util.Objects;

public class games implements Serializable {
    String id ,gamecategorie, gamedescription,gamename,gameimageurl;
    double gameprice;

    public games(){}

    public games(String id, String gamecategorie, String gamedescription, String gamename, String gameimageurl, double gameprice) {
        this.id = id;
        this.gamecategorie = gamecategorie;
        this.gamedescription = gamedescription;
        this.gamename = gamename;
        this.gameimageurl = gameimageurl;
        this.gameprice = gameprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGamecategorie() {
        return gamecategorie;
    }

    public void setGamecategorie(String gamecategorie) {
        this.gamecategorie = gamecategorie;
    }

    public String getGamedescription() {
        return gamedescription;
    }

    public void setGamedescription(String gamedescription) {
        this.gamedescription = gamedescription;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getGameimageurl() {
        return gameimageurl;
    }

    public void setGameimageurl(String gameimageurl) {
        this.gameimageurl = gameimageurl;
    }

    public double getGameprice() {
        return gameprice;
    }

    public void setGameprice(double gameprice) {
        this.gameprice = gameprice;
    }

    @Override
    public boolean equals(Object o) {
       return id.equals(((games)o).id);
    }

}
