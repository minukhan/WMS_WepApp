package site.autoever.userservice.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.autoever.userservice.user.application.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<User> findAllByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.role) = LOWER(:role)")
    List<User> findAllByRole(@Param("role") String role);

    @Query("SELECT u FROM User u WHERE LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
    List<User> findAllByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findAllByName(@Param("name") String name);

    @Query("SELECT u FROM User u")
    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.role) = LOWER(:role)")
    Page<User> findAllByRole(@Param("role") String role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%'))")
    Page<User> findAllByPhoneNumber(@Param("phoneNumber") String phoneNumber, Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> findAllByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<User> findAllByEmail(@Param("email") String email, Pageable pageable);
}
