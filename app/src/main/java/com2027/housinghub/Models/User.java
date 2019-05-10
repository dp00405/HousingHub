package com2027.housinghub.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * implements Parceble if I need to
 */
public class User {

    public String firstName, surname, email, phone, userType;



    /**
     *
     * @param firstName
     * @param surname
     * @param email
     * @param userType
     */
    public User(String firstName, String surname, String email, String userType) {
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.userType = userType;
    }

    /**
     * Default Constructor
     */
    public User(){

    }


//    protected User(Parcel in) {
//        user_id = in.readString();
//        email = in.readString();
//        username = in.readString();
//        password = in.readString();
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     *
     */
//    public static final Creator<User> CREATOR = new Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };



//    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(user_id);
//        parcel.writeString(email);
//        parcel.writeString(username);
//        parcel.writeString(password);
//    }
}
