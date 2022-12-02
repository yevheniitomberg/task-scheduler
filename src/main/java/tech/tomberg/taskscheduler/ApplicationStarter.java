package tech.tomberg.taskscheduler;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationStarter {

    public static void main(String[] args) {
        Application.launch(TaskMangerApplication.class, args);
    }

}
