package dio.springsecurityjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dio.springsecurityjwt.model.User;
import dio.springsecurityjwt.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;
  @PostMapping
  public void postUser(@RequestBody User user) {
    userService.createUser(user);
  }
}
