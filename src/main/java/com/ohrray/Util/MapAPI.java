package com.ohrray.Util;

import com.ohrray.domain.MapDTO;
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
import java.util.ArrayList;
import java.util.List;

public class MapAPI {
    /*x,y 좌표변경해야할 변수---------------------------------*/
    static final int NX = 149; // X축 격자점 수
    static final int NY = 253; // Y축 격자점 수
    static float lon, lat, x, y;
    static float lon1 , lat1 , x1 , y1 ;

    static class LamcParameter {
        float Re; // 사용할 지구반경 [ km ]
        float grid; // 격자간격 [ km ]
        float slat1; // 표준위도 [degree]
        float slat2; // 표준위도 [degree]
        float olon; // 기준점의 경도 [degree]
        float olat; // 기준점의 위도 [degree]
        float xo; // 기준점의 X좌표 [격자거리]
        float yo; // 기준점의 Y좌표 [격자거리]
        int first; // 시작여부 (0 = 시작)
    }
    /*x,y 좌표변경해야할 변수---------------------------------*/

    //지도 API --------------------------------------------------------------------------
    public List<MapDTO> getMapInformation() throws IOException, ParseException {
        /*산림청 공공데이터 JSON 파싱 */
        StringBuilder urlBuilder = new StringBuilder("https://api.odcloud.kr/api/15112801/v1/uddi:72bf80fc-1a93-4193-a6db-8a547d7c3333"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "= "); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("page","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("perPage","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(),"UTF-8"));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        //System.out.println(sb.toString());
        String result = sb.toString();

        //가장 큰 JSONObject를 가져온다
        JSONParser jsonParser = new JSONParser();
        // 배열을 가져옵니다.
        JSONObject jObject = (JSONObject) jsonParser.parse(result);

        //데이터 부분만 파싱
        JSONArray jArray = (JSONArray) jsonParser.parse(jObject.get("data").toString());

        List<MapDTO> mapList = new ArrayList<>();
        for (Object data:jArray){
            JSONObject resultData = (JSONObject) data;
            //경도 위도 받아와서 x,y좌표로 변경
            changeToXY(Float.parseFloat(String.valueOf(resultData.get("Y좌표"))),Float.parseFloat(String.valueOf(resultData.get("X좌표"))));
            MapDTO mapDTO = new MapDTO((String)resultData.get("명산_이름"),
                    (String)resultData.get("명산_소재지"),
                    Float.parseFloat(String.valueOf(resultData.get("명산_높이"))),
                    (String)resultData.get("특징_및_선정_이유"),
                    Float.parseFloat(String.valueOf(resultData.get("X좌표"))),
                    Float.parseFloat(String.valueOf(resultData.get("Y좌표"))),
                    x,y);
            mapList.add(mapDTO);
        }
        return mapList;
        /*산림청 공공데이터 JSON 파싱 ------------------------끝끝*/
    }

    private void changeToXY(float lngTemp, float latTemp){
        LamcParameter map = new LamcParameter();
        //경도와 위도 설정
        lon = lngTemp;
        lat = latTemp;

        // 단기예보 지도 정보 설정
        map.Re = 6371.00877f; // 지도반경
        map.grid = 5.0f; // 격자간격 (km)
        map.slat1 = 30.0f; // 표준위도 1
        map.slat2 = 60.0f; // 표준위도 2
        map.olon = 126.0f; // 기준점 경도
        map.olat = 38.0f; // 기준점 위도
        map.xo = 210 / map.grid; // 기준점 X좌표
        map.yo = 675 / map.grid; // 기준점 Y좌표
        map.first = 0;

        // 단기예보
        mapConv(lon, lat, x, y, 0, map);

    }

    static void mapConv(float lonTemp, float latTemp, float x2, float y2, int code, LamcParameter map) {
        if (code == 0) {
            lon1 = lonTemp;
            lat1 = latTemp;
            lamcProj(lon1, lat1, x1, y1, 0, map);
            x = (int) (x1 + 1.5);
            y = (int) (y1 + 1.5);
        }
    }

    static void lamcProj(float lonTemp, float latTemp, float xTemp, float yTemp, int code, LamcParameter map) {
        double PI = 0, DEGRAD=0, RADDEG=0;
        double re=0, olon=0, olat=0, sn=0, sf=0, ro=0;
        double slat1=0, slat2=0, alon=0, alat=0, xn=0, yn=0, ra=0, theta=0;

        if (map.first == 0) {
            PI = Math.asin(1.0) * 2.0;
            DEGRAD = PI / 180.0;
            RADDEG = 180.0 / PI;

            re = map.Re / map.grid;
            slat1 = map.slat1 * DEGRAD;
            slat2 = map.slat2 * DEGRAD;
            olon = map.olon * DEGRAD;
            olat = map.olat * DEGRAD;

            sn = Math.tan(PI * 0.25 + slat2 * 0.5) / Math.tan(PI * 0.25 + slat1 * 0.5);
            sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
            sf = Math.tan(PI * 0.25 + slat1 * 0.5);
            sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
            ro = Math.tan(PI * 0.25 + olat * 0.5);
            ro = re * sf / Math.pow(ro, sn);
            map.first = 1;
        }
        if(code == 0){
            ra = Math.tan(PI * 0.25 + latTemp * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            theta = lonTemp * DEGRAD - olon;
            if (theta > PI) theta -= 2.0 * PI;
            if (theta < -PI) theta += 2.0 * PI;
            theta *= sn;
            x1 = (float) (ra * Math.sin(theta)) + map.xo;
            y1 = (float) (ro - ra * Math.cos(theta)) + map.yo;
        }
    }
    //지도 API .END--------------------------------------------------------------------------
}
