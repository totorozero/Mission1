package Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;

import static dao.WifiDAO.insertPublicWifi;

public class APIService {
    private static final String baseUrl = "http://openapi.seoul.go.kr:8088" /*URL*/
            + "/" + "6d66536f636a6d69383250564a7a75" /*인증키*/
            + "/" + "json" /*요청파일타입 (xml,xmlf,xls,json) */
            + "/" + "TbPublicWifiInfo"; /*서비스명 (대소문자 구분 필수입니다.)*/
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static int totalWifiCount() throws IOException {
        int start = 1, end = 1;

        URL url = new URL(baseUrl + "/" + start + "/" + end);

        Request.Builder builder = new Request.Builder().url(url).get();
        Response response = okHttpClient.newCall(builder.build()).execute();

        JsonElement jsonElement = JsonParser.parseString(response.body().string());

        return jsonElement.getAsJsonObject().get("TbPublicWifiInfo")
                .getAsJsonObject().get("list_total_count")
                .getAsInt();
    }

    public static int getPublicWifiJson() throws IOException {
        int totalCnt = totalWifiCount();
        int start = 1, end = 1;
        int count = 0;

        for (int i = 0; i <= totalCnt / 1000; i++) {
            start = 1 + (1000 * i);
            end = (i + 1) * 1000;

            URL url = new URL(baseUrl + "/" + start + "/" + end);

            Request.Builder builder = new Request.Builder().url(url).get();
            Response response = okHttpClient.newCall(builder.build()).execute();

            JsonElement jsonElement = JsonParser.parseString(response.body().string());

            JsonArray jsonArray = jsonElement.getAsJsonObject().get("TbPublicWifiInfo")
                    .getAsJsonObject().get("row")
                    .getAsJsonArray();

            count += insertPublicWifi(jsonArray);
        }

        return count;
    }
}
