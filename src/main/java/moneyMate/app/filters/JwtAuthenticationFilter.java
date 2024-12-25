package moneyMate.app.filters;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import moneyMate.app.entity.User;
import moneyMate.app.service.JwtService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull  HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader("Authorization");
        if(jwtToken == null || !jwtService.isTokenValid(jwtToken.substring(7))) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = jwtToken.startsWith("Bearer ")? jwtToken.substring(7): jwtToken;
        String subject = jwtService.extractSubject(jwtToken);
        User user = (User) userDetailsService.loadUserByUsername(subject);
        var context = SecurityContextHolder.getContext();
        if(user != null && context.getAuthentication() == null) {
            var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(request);
            context.setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
