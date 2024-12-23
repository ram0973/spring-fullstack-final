package ra.web.page.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //@Query("SELECT p FROM User p WHERE p.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(@Param(value = "email") String email);

    //Optional<User> findById(int id);

    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);
}
