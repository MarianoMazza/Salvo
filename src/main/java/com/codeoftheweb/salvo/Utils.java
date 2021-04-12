package com.codeoftheweb.salvo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public static Map<String, Object> MakeMap(String key, String message) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, message);
        return map;
    }

    public static ResponseEntity<Map<String, Object>> ResponseWithMap(String key, String message, HttpStatus status) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, message);
        return new ResponseEntity<>(map, status);
    }
}
