package com.rigo.noo.naver;

import com.rigo.noo.ApplicationDefine;
import com.rigo.noo.util.AppLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kbg82 on 2017-01-14.
 */

public class NAPI {
    private static String TAG = NAPI.class.getSimpleName();

    public static String getAdress(double aLongitude , double aLatitude) {
        try {
            AppLog.i(TAG, "aLongitude : " + aLongitude);
            AppLog.i(TAG, "aLatitude : " + aLatitude);
            String apiURL = "https://openapi.naver.com/v1/map/reversegeocode?encoding=utf-8&coordType=latlng&query=" + aLongitude +","+ aLatitude; //json
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", ApplicationDefine.NAVER_CLIENT_ID);
            con.setRequestProperty("X-Naver-Client-Secret", ApplicationDefine.NAVER_CLIENT_SECRET);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            AppLog.i(TAG, "response.toString() : " + response.toString());
            JSONObject pJSONObject = new JSONObject(response.toString());
            AppLog.i(TAG, "result : " + pJSONObject.getString("result"));
            pJSONObject = new JSONObject(pJSONObject.getString("result"));
            AppLog.i(TAG, "items : " + pJSONObject.getString("items"));
            JSONArray pJSONArray = new JSONArray(pJSONObject.getString("items"));
            String address = ((JSONObject) pJSONArray.get(0)).getString("address");

            return address;

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {

        }
        return null;
    }

}
