package tech.tomberg.taskscheduler.controller;

import tech.tomberg.taskscheduler.repository.UserRepository;
import tech.tomberg.taskscheduler.tool.AlertTools;
import tech.tomberg.taskscheduler.tool.FXMLTools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginView {
    @FXML private AnchorPane pane;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton;

    @Autowired
    ConfigurableApplicationContext context;

    @Autowired
    UserRepository userRepository;

    public void tryToLogin(ActionEvent actionEvent) throws IOException {
        if (userRepository.existsByUsernameAndPassword(username.getText(), password.getText())) {
            FXMLTools.setSceneAndDropBeans(context, "/templates/AppView.fxml", (Stage) pane.getScene().getWindow(), "Task Manager", userRepository.findByUsername(username.getText()));
        } else {
            AlertTools.sendAlert(Alert.AlertType.WARNING, "Warning", "Login failed", "Incorrect username or password");
        }
    }

    public void backOnClick(ActionEvent actionEvent) throws IOException {
        FXMLTools.setSceneAndDropBeans(context, "/templates/MainView.fxml", (Stage) pane.getScene().getWindow(), "Task Manager");
    }

    public void initialize() {

    }
}
