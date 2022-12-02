package tech.tomberg.taskscheduler.tool;

import javafx.scene.control.Alert;

public class AlertTools {
    public static void sendAlert(Alert.AlertType alertType,
                                 String title,
                                 String headerText,
                                 String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }
}
