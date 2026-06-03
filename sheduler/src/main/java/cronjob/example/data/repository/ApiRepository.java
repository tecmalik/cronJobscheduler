package cronjob.example.data.repository;

import cronjob.example.data.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ApiRepository extends JpaRepository<User, UUID> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE pending_users SET retry_count = :retries, status = :status, error_message = :error WHERE id = CAST(:userId AS uuid)", nativeQuery = true)
    void UpdateRetryCountManual(@Param("logId") UUID logId,
                                @Param("retries") int retries,
                                @Param("status") String status,
                                @Param("error") String error
                                );
}
