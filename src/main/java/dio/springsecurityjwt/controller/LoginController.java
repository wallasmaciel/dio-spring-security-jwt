package dio.springsecurityjwt.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dio.springsecurityjwt.dto.Login;
import dio.springsecurityjwt.dto.Secao;
import dio.springsecurityjwt.model.User;
import dio.springsecurityjwt.repository.UserRepository;
import dio.springsecurityjwt.security.JWTCreator;
import dio.springsecurityjwt.security.JWTObject;
import dio.springsecurityjwt.security.SecurityConfig;

@RestController
public class LoginController {
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private SecurityConfig securityConfig;
  @Autowired
  private UserRepository userRepository;

  @PostMapping("/login")
  public Secao logar(@RequestBody Login login) {
    User user = userRepository.findByUserName(login.getUsername());
    if (user != null) {
      boolean passwordOk = passwordEncoder.matches(login.getPassword(), user.getPassword());
      if (!passwordOk) throw new RuntimeException("Senha inválida para o login: " + login.getUsername());
      Secao secao = new Secao();
      secao.setLogin(user.getUsername());

      JWTObject jwtObject = new JWTObject();
      jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
      jwtObject.setExpiration(new Date(System.currentTimeMillis() + SecurityConfig.EXPIRATION));
      jwtObject.setRoles(user.getRoles());

      secao.setToken(JWTCreator.create(SecurityConfig.PREFIX, SecurityConfig.KEY, jwtObject));
      return secao;
    } else {
      throw new RuntimeException("Erro ao tentar fazer login.");
    }
  }
}
