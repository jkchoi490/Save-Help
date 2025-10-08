package com.save_help.Save_Help.map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoMapService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${kakao.api-key}")
    private String REST_API_KEY;
    /**
     * 센터 이름(또는 주소)으로 위도, 경도 좌표 조회
     */
    public double[] getCoordinatesByName(String name) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" +
                UriUtils.encode(name, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", REST_API_KEY);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");

        if (documents == null || documents.isEmpty()) {
            throw new IllegalArgumentException("해당 이름으로 위치를 찾을 수 없습니다: " + name);
        }

        Map<String, Object> doc = documents.get(0);
        double longitude = Double.parseDouble((String) doc.get("x")); // 경도
        double latitude = Double.parseDouble((String) doc.get("y"));  // 위도

        return new double[]{latitude, longitude};
    }

}
