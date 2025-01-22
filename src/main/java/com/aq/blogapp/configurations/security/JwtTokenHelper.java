package com.aq.blogapp.configurations.security;

import com.aq.blogapp.constants.UBlogConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenHelper {
  private final Logger logger = LoggerFactory.getLogger(JwtTokenHelper.class);

  private static final String JWT_SECRET = "jwtTokenKey";

  //  retrieving username from token
  public String getUsernameFromToken(String token) {

    logger.info("In getUsernameFromToken() -> token : {} ", token);
    String usernameFromClaims = getClaimFromToken(token, Claims::getSubject);
    logger.info("username from getUserNameFromToken : {}", usernameFromClaims);

    return usernameFromClaims;
  }


  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  //  get all data from the token - header, body & signature
  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  //    secret key is used to retrieve info - We're parsing the token to get the info within the token
  private Claims getAllClaimsFromToken(String token) {
//  Older way: before Jwts:0.12
    /**
     return Jwts.parser()
     .setSigningKey(jwtSecret)
     .parseClaimsJws(token)
     .getBody();
    */

//    from Jwts 0.12 onwards
    return Jwts.parser()
      .verifyWith(getSecretKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  private SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
  }

  //    to check token has expired or not
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  //    to generate token for user
  public String tokenGenerator(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  /**
   * steps to create a token -
   * 1. Define claims of the token, like Issuer, Expiration, Subject and ID
   * 2. Sign the JWT using the HS512 algo and secret key.
   * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-16
   * compacting of the JWT to a URL-safe string
   *
   * @param claims
   * @param username
   * @return jwt token
   */


  private String doGenerateToken(Map<String, Object> claims, String username) {
//   Older way: before Jwts:0.12
    /**return Jwts.builder()
     .setClaims(claims)
     .setSubject(username)
     .setIssuedAt(new Date(System.currentTimeMillis()))
     .setExpiration(new Date(System.currentTimeMillis() + UBlogConstants.JWT_TOKEN_VALIDITY * 100))
     .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
     .compact();
     */

    return Jwts.builder()
      .claims(claims)
      .subject(username)
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date(System.currentTimeMillis() + UBlogConstants.JWT_TOKEN_VALIDITY * 100))
      .signWith(getSecretKey(), Jwts.SIG.HS256)
      .compact();

  }


  //    validate token
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

}
