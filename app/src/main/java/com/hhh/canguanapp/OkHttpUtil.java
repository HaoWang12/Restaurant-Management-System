package com.hhh.canguanapp;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {
    public static String baseurl = "http://192.168.0.113:8080/CanGuanServer/";

    public static void loginWithOkHttp(String account, String password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("loginAccount",account)
                .add("loginPassword",password)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"LoginServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void registerWithOkHttp(String account, String password,String name,String statue, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("registerAccount",account)
                .add("registerPassword",password)
                .add("registerName",name)
                .add("registerStatue",statue)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"RegisterServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findUser(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindUserServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void altUser(String username, String password,String name,String statue, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .add("name",name)
                .add("statue",statue)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"AltUserServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteUser(String username, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"DeleteUserServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void altCai(String id, String name, String price, String types, String note,String newprice, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .add("name",name)
                .add("price",price)
                .add("types",types)
                .add("note",note)
                .add("newprice",newprice)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"AltCaiServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteCai(String id, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"DeleteCaiServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findCai(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindCaiServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addCai(String name, String price, String types, String note,String newprice, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("name",name)
                .add("price",price)
                .add("types",types)
                .add("note",note)
                .add("newprice",newprice)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddCaiServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void altDesk(String id, String number, String total, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .add("number",number)
                .add("total",total)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"AltDeskServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteDesk(String id, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"DeleteDeskServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findDesk(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindDeskServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addDesk(String number, String total,String username, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("number",number)
                .add("total",total)
                .add("total",total)
                .add("username",username)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddDeskServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findGou(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindGouServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addGou(String did, String cid,String name,String price,String types,String note,String username,String newprice, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("did",did)
                .add("cid",cid)
                .add("name",name)
                .add("price",price)
                .add("types",types)
                .add("note",note)
                .add("newprice",newprice)
                .add("username",username)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddGouServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteGou(String id, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"DeleteGouServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteAllGou(String did, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("did",did)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"DeleteAllGouServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteAllDesk(String number, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("number",number)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"DeleteAllDeskServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findRecord(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindRecordServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addRecord(String name, String price,String number,String time, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("name",name)
                .add("price",price)
                .add("number",number)
                .add("time",time)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddRecordServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findBank(String username,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindBankServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addBank(String username, String money, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .add("money",money)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddBankServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void altBank(String username, String money, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .add("money",money)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"AltBankServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void altRoom(String id,String phone, String number,String time,String note, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .add("number",number)
                .add("phone",phone)
                .add("time",time)
                .add("note",note)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"AltRoomServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteRoom(String id, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"DeleteRoomServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findRoom(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindRoomServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addRoom(String phone, String number,String time,String note, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("number",number)
                .add("phone",phone)
                .add("time",time)
                .add("note",note)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddRoomServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addCommunication(String username, String content, String time, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .add("content",content)
                .add("time",time)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddCommunicationServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void findCommunication(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindCommunicationServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findComment(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindCommentServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addComment(String commentId, String username, String comment, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("commentId",commentId)
                .add("username",username)
                .add("comment",comment)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddCommentServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void altCaiGou(String id,String content, String money,String time,String note, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .add("content",content)
                .add("money",money)
                .add("time",time)
                .add("note",note)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"AltCaiGouServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteCaiGou(String id, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"DeleteCaiGouServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findCaiGou(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindCaiGouServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addCaiGou(String content, String money,String time,String note, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("content",content)
                .add("money",money)
                .add("time",time)
                .add("note",note)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddCaiGouServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }













    public static void addWei(String addresskey, String address,String price,String detail, String pic, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("addresskey",addresskey)
                .add("address",address)
                .add("pic",pic)
                .add("price",price)
                .add("detail",detail)
                .build();
        Request request = new Request.Builder()
                .url(baseurl+"AddWeiServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findWei(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindWeiServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteWei(String id, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"DeleteWeiServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findUserByUsername(String username, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .build();
        Request request = new Request.Builder()
                .url( baseurl+"FindUserByUsernameServlet")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
