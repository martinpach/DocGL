package com.wdfeww.docgl.data.remote;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://wdfeww.myqnapcloud.com:8085/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}