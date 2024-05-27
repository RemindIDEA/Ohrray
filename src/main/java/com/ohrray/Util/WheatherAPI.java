package com.ohrray.Util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WheatherAPI {

    //날씨 API -------------------------------------------------------------------------
    public void getWeather() throws IOException, ParseException {
        /*기상청 공공데이터 JSON 파싱 ----------------------------*/
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("290", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode("20240423", "UTF-8")); /*‘21년 6월 28일발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode("2300", "UTF-8")); /*05시 발표*/
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode("37", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode("126", "UTF-8")); /*예보지점의 Y 좌표값*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        //System.out.println(sb.toString());

        String result = String.valueOf(sb);

        //System.out.println("result = " + result);
        //가장 큰 JSONObject를 가져온다

        JSONParser jsonParser = new JSONParser();
        // 배열을 가져옵니다.
        JSONObject jObject = (JSONObject) jsonParser.parse(result);
        jObject = (JSONObject) jsonParser.parse(jObject.get("response").toString());
        jObject = (JSONObject) jsonParser.parse(jObject.get("body").toString());
        jObject = (JSONObject) jsonParser.parse(jObject.get("items").toString());
        //System.out.println("jObject = " + jObject);

        //데이터 부분만 파싱
        JSONArray jArray = (JSONArray) jsonParser.parse(jObject.get("item").toString());
        //System.out.println("jArray = " + jArray);
        for (Object data : jArray) {
            JSONObject jData = (JSONObject) data;
            System.out.println("jData해당날짜 = " + jData.get("fcstDate"));
            System.out.println("data.기준시간!!! = " + jData.get("fcstTime"));
            System.out.println("jData.무슨내용을 발표한건지 = " + jData.get("category"));
            System.out.println("jData.발표에대한 값 = " + jData.get("fcstValue"));
            System.out.println();
            System.out.println();
        }

//
//        List<MapDTO> mapList = new ArrayList<>();
//
//        MapController mapController = new MapController();
//        mapController.insertMapInform(mapList);
        /*기상청 공공데이터 JSON 파싱 ----------------------------*/
    }
    //날씨 API .END----------------------------------------------------------------------
}
