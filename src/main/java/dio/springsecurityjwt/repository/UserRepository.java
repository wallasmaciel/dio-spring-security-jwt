package dio.springsecurityjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dio.springsecurityjwt.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
  @Query("SELECT e FROM User e JOIN FETCH e.roles WHERE e.username = (:username)")
  User findByUserName(@Param("username") String username);

  // boolean existsByUserName(String username);
}
