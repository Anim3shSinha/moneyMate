package moneyMate.app.repository;

import moneyMate.app.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, String> {
    boolean existsByCardNumber(double cardNumber);

    Optional<Card> findByOwnerUid(String uid);
}
