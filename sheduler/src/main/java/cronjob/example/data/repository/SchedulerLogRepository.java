package cronjob.example.data.repository;

import cronjob.example.data.model.SchedulerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SchedulerLogRepository
        extends JpaRepository<SchedulerLog, UUID> {

    @Query("""
           SELECT s
           FROM SchedulerLog s
           WHERE s.status IN ('PENDING', 'RETRYING')
           """)
    List<SchedulerLog> findPendingOrRetrying();
}