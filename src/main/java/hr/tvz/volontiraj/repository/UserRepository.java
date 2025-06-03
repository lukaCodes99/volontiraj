package hr.tvz.volontiraj.repository;

import hr.tvz.volontiraj.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
