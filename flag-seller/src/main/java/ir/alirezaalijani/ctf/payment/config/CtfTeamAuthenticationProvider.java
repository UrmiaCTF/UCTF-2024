package ir.alirezaalijani.ctf.payment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CtfTeamAuthenticationProvider implements AuthenticationProvider {

    private final CtfTeamDetailService userDetailService;
    private final Environment env;
    public CtfTeamAuthenticationProvider(CtfTeamDetailService userDetailService,
                                         Environment env) {
        this.userDetailService = userDetailService;
        this.env = env;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        Optional<UserDetails> user;
        log.debug("try authenticate user with ctf detail service.");
        if (env.acceptsProfiles(Profiles.of("dev"))) {
            log.info("Web application auth with dev mod");
            user = Optional.of(User.builder()
                    .username(String.valueOf(1))
                    .password("password")
                    .authorities("USER").build());
        }else {
            user = userDetailService.loadTeamNameAndToken(username,password);
        }

        if (user.isPresent()) {
            return new UsernamePasswordAuthenticationToken(user.get().getUsername(), password, user.get().getAuthorities());
        } else {
            throw new AuthenticationException("Authentication failed") {};
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
