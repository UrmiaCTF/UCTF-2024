package ir.alirezaalijani.ctf.payment.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CtfTeamDetailServiceImpl implements CtfTeamDetailService {

    private final RestTemplate restTemplate;
    @Value("${ctfd.url}")
    private String ctfdUrl;

    public CtfTeamDetailServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<UserDetails> loadTeamNameAndToken(String teamName, String token) {
        log.debug("prepare new get request to ctf service.");
        String url = ctfdUrl + "/api/v1/teams/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + token);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<TeamResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, TeamResponse.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                log.debug("request status was OK.");
                if (response.hasBody()) {
                    var body = response.getBody();
                    if (body != null && body.success && body.data != null && body.data.name.equals(teamName)) {
                        log.debug("generate user details for team {} data {}",teamName,body);
                        return Optional.of(User.builder()
                                .username(String.valueOf(body.data.id))
                                .password(token)
                                .authorities("USER").build());
                    }
                }
            }
        }catch (RestClientException e){
            log.error("ctf team verification failed :{}",e.getMessage());
        }
        return Optional.empty();
    }


    @Data
    private static class TeamResponse {
        private boolean success;
        private TeamData data;

        @Data
        private static class TeamData {
            private String affiliation;
            private String email;
            private String name;
            private String country;
            private int id;
            private String website;
            private String oauth_id;
            private List<String> fields;
            private String bracket_id;
            private int captain_id;
            private List<Integer> members;
            private String place;
            private int score;
        }
    }

}
