package models;

import android.os.Parcel;

import java.util.Date;

import static android.R.attr.description;

/**
 * Created by robbi on 2/14/2017.
 */

public class Response {

    String id;
    String UserId;
    String Response1;
    String Response2;
    String Response3;
    String Response4;
    String Response5;
    String Date;

    public Response(){}

    public Response(String id, String UserId, String Response1, String Response2, String Response3, String Response4, String Response5, String Date){
        this.id = id;
        this.UserId = UserId;
        this.Response1 = Response1;
        this.Response2 = Response2;
        this.Response3 = Response3;
        this.Response4 = Response4;
        this.Response5 = Response5;
        this.Date = Date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getResponse1() {
        return Response1;
    }

    public void setResponse1(String response1) {
        Response1 = response1;
    }

    public String getResponse2() {
        return Response2;
    }

    public void setResponse2(String response2) {
        Response2 = response2;
    }

    public String getResponse3() {
        return Response3;
    }

    public void setResponse3(String response3) {
        Response3 = response3;
    }

    public String getResponse4() {
        return Response4;
    }

    public void setResponse4(String response4) {
        Response4 = response4;
    }

    public String getResponse5() {
        return Response5;
    }

    public void setResponse5(String response5) {
        Response5 = response5;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }


}
