package pl.agh.gateway.application.rest;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.agh.gateway.application.rest.url.URLProvider;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RestClient {

    private final RestTemplate restTemplate;
    private final URLProvider urlProvider;
    private final Log logger = LogFactory.getLog(getClass());

    public <T> T get(MicroService ms, String url, Class<T> type) {
        String baseURL = urlProvider.getBaseURL(ms);
        String fullUrl = baseURL + url;
        try {
            logger.info(String.format("START GET: MS=[%s] URL=[%s] TYPE=[%s]", ms, fullUrl, type.getName()));
            return restTemplate.getForObject(fullUrl, type);
        } finally {
            logger.info(String.format("END   GET: MS=[%s] URL=[%s] TYPE=[%s]", ms, fullUrl, type.getName()));
        }
    }

    public <T> T post(MicroService ms, String url, JSONObject jsonObject,  Class<T> type) {
        String baseURL = urlProvider.getBaseURL(ms);
        String fullUrl = baseURL + url;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        try {
            logger.info(String.format("START POST: MS=[%s] URL=[%s] TYPE=[%s]", ms, fullUrl, type.getName()));
            return restTemplate.postForObject(fullUrl, request, type);
        } finally {
            logger.info(String.format("END   POST: MS=[%s] URL=[%s] TYPE=[%s]", ms, fullUrl, type.getName()));
        }
    }

    public void put(MicroService ms, String url, JSONObject jsonObject,  String username) {
        String baseURL = urlProvider.getBaseURL(ms);
        String fullUrl = baseURL + url + "{username}";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        try {
            logger.info(String.format("START PUT: MS=[%s] URL=[%s] username=[%s]", ms, fullUrl, username));
            restTemplate.put(fullUrl, request, params);
        } finally {
            logger.info(String.format("END   PUT: MS=[%s] URL=[%s] username=[%s]", ms, fullUrl, username));
        }
    }
}
