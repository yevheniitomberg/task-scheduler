package tech.tomberg.taskscheduler;

import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<TaskMangerApplication.StageReadyEvent> {
    @Override
    public void onApplicationEvent(TaskMangerApplication.StageReadyEvent event) {
        Stage stage = event.getStage();
    }
}
