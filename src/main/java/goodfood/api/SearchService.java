package goodfood.api;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class SearchService {
    private Gson gson;
    private JsonParser jsonParser;

    @PostConstruct
    private void setup() {
        gson = new Gson();
        jsonParser = new JsonParser();
    }

    public String searchPlace(String keyword) {

        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encoding fail!", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" +keyword+ "&display=5&start=1&sort=random";//json결과
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", "sObE15VrLzBnkVUolhdD");
        requestHeaders.put("X-Naver-Client-Secret", "0VDugqIuLz");
        String responseBody = get(apiURL, requestHeaders);

        return responseBody;
    }

    public MapData convertData(String responseBody) {
        MapData converterData = gson.fromJson(responseBody, MapData.class);

        return converterData;
    }


    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {     //정상호출
                return readBody(con.getInputStream());

            } else {
                return readBody(con.getErrorStream());          //Error
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);

        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " +apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " +apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body, StandardCharsets.UTF_8);

        try (BufferedReader lineReader = new BufferedReader(streamReader) ) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }

    }


}
