package dio.springsecurityjwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dio.springsecurityjwt.model.User;
import dio.springsecurityjwt.repository.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public void createUser(User user) {
    String pass = user.getPassword();
    // criptografando antes de salvar no banco
    user.setPassword(passwordEncoder.encode(pass));
    userRepository.save(user);
  }
}
