package tech.tomberg.taskscheduler.tool;

import tech.tomberg.taskscheduler.entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class FXMLTools {
    public static void setSceneAndDropBeans(ConfigurableApplicationContext context,
                                            String url,
                                            Stage stage,
                                            String title,
                                            User... user) throws IOException {
        FXMLLoader loader = new FXMLLoader(FXMLTools.class.getResource(url));
        loader.setControllerFactory(context::getBean);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setUserData(user);
        stage.setScene(new Scene(loader.load()));
    }
}
