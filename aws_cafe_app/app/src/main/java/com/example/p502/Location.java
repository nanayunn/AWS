package com.example.p502;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
    double leti;
    double longti;


    public Location(){

    }



    public Location(Parcel src){
    this.leti = src.readDouble();
    this.longti = src.readDouble();
    }
    public Location(double leti, double longti) {
        this.leti = leti;
        this.longti = longti;
    }

    public double getLeti() {
        return leti;
    }

    public void setLeti(double leti) {
        this.leti = leti;
    }

    public double getLongti() {
        return longti;
    }

    public void setLongti(double longti) {
        this.longti = longti;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Location[size];
        }
    };

    public void writeToParcel(Parcel dest, int flags){
        dest.writeDouble(leti);
        dest.writeDouble(longti);
    }



    @Override
    public int describeContents() {
        return 0;
    }


}
