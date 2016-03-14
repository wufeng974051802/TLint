package com.gzsll.hupu.api.game;

import com.gzsll.hupu.bean.LoginResult;
import com.gzsll.hupu.bean.ThreadListResult;
import com.gzsll.hupu.bean.UserResult;
import com.gzsll.hupu.components.retrofit.GsonConverterFactory;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.helper.RequestHelper;
import com.gzsll.hupu.helper.SettingPrefHelper;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/10.
 */
public class GameApi {
    static final String BASE_URL = "http://games.mobileapi.hupu.com/1/7.0.7/";

    private GameService mGameService;
    private RequestHelper mRequestHelper;
    private SettingPrefHelper mSettingPrefHelper;
    private UserStorage mUserStorage;

    public GameApi(RequestHelper mRequestHelper, SettingPrefHelper mSettingPrefHelper, UserStorage mUserStorage, OkHttpClient mOkHttpClient) {
        this.mRequestHelper = mRequestHelper;
        this.mSettingPrefHelper = mSettingPrefHelper;
        this.mUserStorage = mUserStorage;
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(mOkHttpClient).baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mGameService = retrofit.create(GameService.class);
    }

    public Observable<LoginResult> login(String userName, String passWord) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("username", userName);
        params.put("password", passWord);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.login(params, mRequestHelper.getDeviceId()).subscribeOn(Schedulers.io());

    }

    public Observable<UserResult> getUserInfo(String uid) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("puid", uid);
        String sign = mRequestHelper.getRequestSign(params);
        params.put("sign", sign);
        return mGameService.getUserInfo(params, mRequestHelper.getDeviceId()).subscribeOn(Schedulers.io());
    }

    public Observable<ThreadListResult> getCollectList(int page) {
        Map<String, String> params = mRequestHelper.getHttpRequestMap();
        params.put("page", String.valueOf(page));
        String sign = mRequestHelper.getRequestSign(params);
        return mGameService.getCollectList(sign, params).subscribeOn(Schedulers.io());
    }

}