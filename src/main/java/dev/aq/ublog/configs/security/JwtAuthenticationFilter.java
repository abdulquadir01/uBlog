package dev.aq.ublog.configs.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final UserDetailsService userDetailsService;
  private final JwtTokenHelper tokenHelper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

//    1. get token
    String requestToken = request.getHeader("Authorization");

//    token starts with "Bearer <some-string>"
    log.info("requestToken: \n {} ", requestToken);

    String username = null;
    String token = null;

    if (requestToken != null && requestToken.startsWith("Bearer")) {

      token = requestToken.substring(7);
      log.info("originalToken: \n {}", token);

      try {
        username = tokenHelper.getUsernameFromToken(token);
        log.info("Username : {}", username);
      } catch (IllegalArgumentException ex) {
        log.info("Unable to get JWT token. \n Error message: {}", ex.getMessage());
      } catch (ExpiredJwtException ex) {
        log.info("JWT token has expired. \n Error message: {}", ex.getMessage());
      } catch (MalformedJwtException ex) {
        log.info("Invalid jwt. \n Error message: {}", ex.getMessage());
      }

    } else {
      log.info("Token doesn't starts with Bearer");
    }

//     validating token after retrieval
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      if (Boolean.TRUE.equals(tokenHelper.validateToken(token, userDetails))) {

//        set authentication
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

      } else {
        log.info("Invalid token");
      }
    } else {
      log.info("Username is null or context is not null");
    }

    filterChain.doFilter(request, response);

  }
}
