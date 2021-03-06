package com.example.fellowtraveller;

import android.os.Parcel;
import android.os.Parcelable;

public class UserB  implements Parcelable {

    private int id;
    private String name;
    private String bag;
    private Double rate;
    private int num_of_travels_offered;
    private int num_of_travels_takespart;
    private String picture;


    protected UserB(Parcel in) {
        id = in.readInt();
        name = in.readString();
        bag = in.readString();
        if (in.readByte() == 0) {
            rate = null;
        } else {
            rate = in.readDouble();
        }
        num_of_travels_offered = in.readInt();
        num_of_travels_takespart = in.readInt();
        picture = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(bag);
        if (rate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rate);
        }
        dest.writeInt(num_of_travels_offered);
        dest.writeInt(num_of_travels_takespart);
        dest.writeString(picture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserB> CREATOR = new Creator<UserB>() {
        @Override
        public UserB createFromParcel(Parcel in) {
            return new UserB(in);
        }

        @Override
        public UserB[] newArray(int size) {
            return new UserB[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getBag() {
        return bag;
    }

    public void setBag(String bag) {
        this.bag = bag;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public int getNum_of_travels_offered() {
        return num_of_travels_offered;
    }

    public void setNum_of_travels_offered(int num_of_travels_offered) {
        this.num_of_travels_offered = num_of_travels_offered;
    }

    public int getNum_of_travels_takespart() {
        return num_of_travels_takespart;
    }

    public void setNum_of_travels_takespart(int num_of_travels_takespart) {
        this.num_of_travels_takespart = num_of_travels_takespart;
    }


}
