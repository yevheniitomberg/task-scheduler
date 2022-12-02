package tech.tomberg.taskscheduler.controller;

import tech.tomberg.taskscheduler.entity.User;
import tech.tomberg.taskscheduler.repository.UserRepository;
import tech.tomberg.taskscheduler.tool.AlertTools;
import tech.tomberg.taskscheduler.tool.FXMLTools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
public class RegistrationView {
    @FXML private TextField fullName;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private PasswordField passwordRepeat;
    @FXML private AnchorPane pane;

    @Autowired
    ConfigurableApplicationContext context;

    @Autowired
    private UserRepository userRepository;


    public void tryToRegister(ActionEvent actionEvent) throws IOException{
        boolean usernameExists = userRepository.existsByUsername(username.getText());
        boolean usernameIsRight = Pattern.matches("[a-zA-Z0-9]{8,50}", username.getText());
        boolean passwordsEquals = password.getText().equals(passwordRepeat.getText());
        boolean fullNameIsRight = fullName.getText().length() >= 8 && fullName.getText().length() < 50;

        if (usernameExists) {
            AlertTools.sendAlert(Alert.AlertType.WARNING, "Warning", "Failed to register account", "Username already exists");
        }
        if (!passwordsEquals) {
            AlertTools.sendAlert(Alert.AlertType.WARNING, "Warning", "Failed to register account", "Passwords don't match");
        }
        if (!fullNameIsRight) {
            AlertTools.sendAlert(Alert.AlertType.WARNING, "Warning", "Failed to register account", "Full name must have 8-50 characters");
        }
        if (!usernameIsRight) {
            AlertTools.sendAlert(Alert.AlertType.WARNING, "Warning", "Failed to register account", "Username must have 8-50 characters and only letters and digits");
        }
        if (!usernameExists && passwordsEquals && fullNameIsRight && usernameIsRight) {
            userRepository.save(User.builder()
                            .fullName(fullName.getText())
                            .username(username.getText())
                            .password(password.getText())
                            .build());
            FXMLTools.setSceneAndDropBeans(context, "/templates/MainView.fxml", (Stage) pane.getScene().getWindow(), "Task Manager");
        }
    }

    public void backOnClick(ActionEvent actionEvent) throws IOException {
        FXMLTools.setSceneAndDropBeans(context, "/templates/MainView.fxml", (Stage) pane.getScene().getWindow(), "Task Manager");
    }
}
