package tech.tomberg.taskscheduler.controller;

import tech.tomberg.taskscheduler.repository.UserRepository;
import tech.tomberg.taskscheduler.tool.FXMLTools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MainView {

    @FXML
    private AnchorPane pane;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfigurableApplicationContext context;

    public void registerOnClick(ActionEvent actionEvent) throws IOException {
        FXMLTools.setSceneAndDropBeans(context, "/templates/RegistrationView.fxml", (Stage) pane.getScene().getWindow(), "Registration");
    }

    public void loginOnClick(ActionEvent actionEvent) throws IOException{
        FXMLTools.setSceneAndDropBeans(context, "/templates/LoginView.fxml", (Stage) pane.getScene().getWindow(), "Login");
    }
}
