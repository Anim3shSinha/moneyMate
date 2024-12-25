package moneyMate.app.repository;

import moneyMate.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String > {
    User findByUsernameIgnoreCase (String username);
}
