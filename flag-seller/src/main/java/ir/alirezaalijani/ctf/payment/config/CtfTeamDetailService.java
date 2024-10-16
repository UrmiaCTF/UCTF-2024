package ir.alirezaalijani.ctf.payment.config;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface CtfTeamDetailService {
    Optional<UserDetails> loadTeamNameAndToken(String teamName, String token);
}
