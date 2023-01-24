package dio.springsecurityjwt.security;

import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

public class JWTCreator {
  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String ROLES_AUTHORITIES = "authorities";

  public static String create(String prefix, String key, JWTObject jwtObject) {
    String token = Jwts.builder().setSubject(jwtObject.getSubject()).setIssuedAt(jwtObject.getIssuedAt()).setExpiration(jwtObject.getExpiration())
      .claim(ROLES_AUTHORITIES, checkRoles(jwtObject.getRoles())).signWith(SignatureAlgorithm.HS512, key).compact();
    return prefix + " " + token;
  }

  public static JWTObject create(String token, String prefix, String key) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException {
    JWTObject jwtObject = new JWTObject();
    token = token.replace(prefix, "");
    Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    jwtObject.setSubject(claims.getSubject());
    jwtObject.setExpiration(claims.getExpiration());
    jwtObject.setIssuedAt(claims.getIssuedAt());
    jwtObject.setRoles((List) claims.get(ROLES_AUTHORITIES));
    return jwtObject;
  }

  private static List<String> checkRoles(List<String> roles) {
    return roles.stream().map(s -> "ROLE_".concat(s.replaceAll("ROLE_", "s"))).collect(Collectors.toList());
  }
}
