package com.divforce.cr.uiservice.web;

import com.divforce.cr.uiservice.model.LoginRequest;
import com.divforce.cr.uiservice.model.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/security/login")
@RequiredArgsConstructor
public class LoginController {
    private final RestTemplate restTemplate;
    @Value("${oauth-client-id}")
    private String oidcClient;
    @Value("${oauth-client-secret}")
    private String oidcSecret;
    @Value("${oauth-client-endpoint}")
    private String endpoint;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    LoginResponse login(@RequestBody LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", loginRequest.getUsername());
        map.add("password", loginRequest.getPassword());
        map.add("grant_type", "password");
        map.add("client_id", oidcClient);
        map.add("client_secret", oidcSecret);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        return restTemplate.postForObject(
                endpoint,
                entity,
                LoginResponse.class);
    }
}
