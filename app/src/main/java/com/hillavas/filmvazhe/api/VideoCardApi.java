package com.hillavas.filmvazhe.api;


import com.hillavas.filmvazhe.utils.AndroidUtils;
import com.hillavas.filmvazhe.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by ArashJahani on 15/05/2015.
 */

public class VideoCardApi {


    private String apikey = "0";

    private String apiPath = "http://videokart.ir/api/v1/%s";
    public static String imagesPath = "http://static.videokart.ir/cover/%s.jpg";
    public static String videoPath="http://videokart.ir:1935/clips/_definst_/%s/480.mp4/manifest.mpd";//"http://mashinz.xyz:1935/clips/_definst_/%s/480.mp4/manifest.mpd";
    public static String clipCover="http://static.videokart.ir/clip/%s/screenshot.jpg";
    public static String bookCover="http://static.videokart.ir/book/%s.jpg";

    String url;

    public VideoCardApi() {

    }

    private void execute(String type, String method, AsyncHttpResponseHandler callback, Object... params) {

        apikey = MyApplication.getSharedPreferences().getString("apiToken", "0");

//        if (method.contains("register") || method.contains("login")) {
//            apikey = "0";
//        }

        url = String.format(apiPath, method);

        RequestParams requestParams = new RequestParams();
        for (int i = 1; i < params.length; i += 2) {

            if (params[i] == null) {
                continue;
            }
            String key = String.valueOf(params[i - 1]);
            String value = String.valueOf(params[i]);
            requestParams.put(key, value);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", apikey);
        client.setMaxRetriesAndTimeout(2, 4000);

        if (type.equals("post")) {
            client.post(url, requestParams, callback);
        } else {
            client.get(url, requestParams, callback);
        }
    }

    public void UploadFile(final String filePath, AsyncHttpResponseHandler callback) {

        url = String.format(apiPath, "post/image/upload");

        apikey = MyApplication.getSharedPreferences().getString("apiToken", "0");

        AsyncHttpClient httpclient = new AsyncHttpClient();
        httpclient.addHeader("Authorization", apikey);

        httpclient.setMaxRetriesAndTimeout(5, 30000);
        File myFile = new File(filePath);
        RequestParams params = new RequestParams();
        try {
            params.put("file", myFile);
        } catch (FileNotFoundException e) {
        }
        httpclient.post(MyApplication.getContext(), url, params, callback);
    }

    public void hello(AsyncHttpResponseHandler callback) {
        execute("post", "hello", callback, "uuid", AndroidUtils.getDeviceID(), "device_name", AndroidUtils.getDeviceName(), "os_version", AndroidUtils.getAndroidVersion(), "app_version", AndroidUtils.getAppVersionCode());
    }

    public void getMovies(Integer type,Integer start,Integer limit,AsyncHttpResponseHandler callback) {
        execute("post", "movie/list", callback,"type",type,"start",start,"limit",limit);
    }
    public void getBook(AsyncHttpResponseHandler callback) {
        execute("get", "book/list", callback);
    }
    public void getLesson(Integer bookId, AsyncHttpResponseHandler callback) {
        execute("post", "book/lesson/list", callback,"book_id",bookId);
    }
    public void getLessonWords(Integer lessonId, AsyncHttpResponseHandler callback) {
        execute("post", "book/word/list", callback,"lesson_id",lessonId);
    }

    public void getMovieWords(Integer movieId,Integer wordId,AsyncHttpResponseHandler callback) {
        execute("post", "clip/list", callback,"movie_id",movieId,"word_id",wordId);
    }

    public void getMovieSubtitle(Integer movieId,AsyncHttpResponseHandler callback) {
        execute("post", "clip/subtitle", callback,"clip_id",movieId);
    }

    public void searchWord(String text,Integer limit,AsyncHttpResponseHandler callback) {
        execute("post", "movie/dialogue/search", callback,"text",text,"limit",limit);
    }

    public void setLike(Integer clipId,AsyncHttpResponseHandler callback) {
        execute("post", "clip/like", callback,"clip_id",clipId);
    }

    public void getWordInfo(String word,AsyncHttpResponseHandler callback) {
        execute("post", "book/word/info", callback,"name",word);
    }
    public void getWordExample(int id,AsyncHttpResponseHandler callback) {
        execute("post", "book/word/examples", callback,"id",id);
    }
    public void searchBookWord(String word, AsyncHttpResponseHandler callback) {
        execute("post", "book/word/search", callback,"query",word);
    }

    public void sendTicket(String message, AsyncHttpResponseHandler callback) {
        execute("post", "ticket/create", callback, "message", message);
    }

    public void editAccount(String name,AsyncHttpResponseHandler callback){
        execute("post", "account/edit", callback, "fullname", name);

    }

    public void setView(Integer clipId,AsyncHttpResponseHandler callback){
        execute("post", "clip/view", callback, "clip_id", clipId);

    }

    public void setStudy(Integer clipId,AsyncHttpResponseHandler callback) {
        execute("post", "word/study", callback,"word_id",clipId);
    }

    public void searchSimple(String query,AsyncHttpResponseHandler callback) {
        execute("post", "book/word/search_simple", callback,"query",query);
    }

    public void userFavoriteClips( AsyncHttpResponseHandler callback) {
        execute("post", "clip/list/liked", callback);
    }









}
