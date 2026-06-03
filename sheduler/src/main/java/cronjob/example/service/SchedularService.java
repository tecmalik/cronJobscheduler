package cronjob.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SchedularService {
    @Autowired
    private UserServices userService;

    @Scheduled(cron = "*/10 * * * * *")
    public void scheduledTask() {
        System.out.println("Scheduled task Clock triggered! Checking database for PENDING or RETRYING records... ");
        userService.processPendingUsersCron();
        System.out.println(" Turn completed. Waiting for next interval...");

    }


}
