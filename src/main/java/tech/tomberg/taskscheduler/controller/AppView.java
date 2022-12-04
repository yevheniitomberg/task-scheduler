package tech.tomberg.taskscheduler.controller;

import tech.tomberg.taskscheduler.entity.Task;
import tech.tomberg.taskscheduler.entity.User;
import tech.tomberg.taskscheduler.repository.TaskRepository;
import tech.tomberg.taskscheduler.repository.UserRepository;
import tech.tomberg.taskscheduler.tool.AlertTools;
import tech.tomberg.taskscheduler.tool.FXMLTools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppView {
    @FXML private AnchorPane pane;
    @FXML private Label username;
    @FXML private Button refresh, buttonUpdate, buttonCancel, buttonLogOut;
    @FXML private TableView<Task> taskActive;
    @FXML private TableColumn<Task, Long> taskId;
    @FXML private TableColumn<Task, String> header;
    @FXML private TableColumn<Task, String> description;
    @FXML private TableColumn<Task, LocalDateTime> deadline;
    @FXML private TableColumn<Task, Button> manage;
    @FXML private TableView<Task> taskActive1;
    @FXML private TableColumn<Task, Long> taskId1;
    @FXML private TableColumn<Task, String> header1;
    @FXML private TableColumn<Task, String> description1;
    @FXML private TableColumn<Task, LocalDateTime> deadline1;
    @FXML private TableColumn<Task, Button> manage1;
    @FXML private TextField taskUpdateId;
    @FXML private ComboBox<Time> taskUpdateTime;
    @FXML private DatePicker taskUpdateDate;
    @FXML private CheckBox taskUpdateIsDone;
    @FXML private TextField taskUpdateHeader;
    @FXML private TextArea taskUpdateDescription;
    ObservableList<Task> tasks;
    User user = null;

    @Autowired
    ConfigurableApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    public void loadData() {
        if (user == null) {
            Stage stage = (Stage) refresh.getScene().getWindow();
            Object[] objects = (Object[]) stage.getUserData();
            for (Object object : objects) {
                user = (User) object;
            }
            username.setText(user.getUsername());
        }
        tasks = FXCollections.observableList(setUpButtons(taskRepository.findAllByUserAndDone(user, false)));
        setUpTasks(taskActive, taskId, header, description, deadline, manage, tasks);
        tasks = FXCollections.observableList(setUpButtons(taskRepository.findAllByUserAndDone(user, true)));
        setUpTasks(taskActive1, taskId1, header1, description1, deadline1, manage1, tasks);
    }

    public void setUpTasks( TableView<Task> taskActive,
                            TableColumn<Task, Long> taskId,
                            TableColumn<Task, String> header,
                            TableColumn<Task, String> description,
                            TableColumn<Task, LocalDateTime> deadline,
                            TableColumn<Task, Button> manage,
                            ObservableList<Task> tasks
    ) {
        taskId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        header.setCellValueFactory(new PropertyValueFactory<>("Header"));
        description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("Deadline"));
        deadline.setCellFactory(col -> new TableCell<Task, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(String.format(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            }
        });
        manage.setCellValueFactory(new PropertyValueFactory<>("Update"));
        taskActive.setItems(tasks);
        taskActive.getSortOrder().add(deadline);
    }
    public List<Task> setUpButtons(List<Task> tasks) {
        for (Task task : tasks) {
            Button button = new Button("Show");
            button.setOnAction(evt -> {
                try {
                    showUpdateTaskDialog(task);
                } catch (IOException e) {
                    System.out.println("Error");
                }
            });
            task.setUpdate(button);
        }
        return tasks;
    }

    public void showUpdateTaskDialog(Task task) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(pane.getScene().getWindow());
        FXMLTools.setSceneAndDropBeans(context,
                                    "/templates/TaskDialog.fxml",
                                        stage,
                                        "Task"
                );
        if (task.getId() == null) {
            taskUpdateTime.setItems(FXCollections.observableList(loadTimes()));
            taskUpdateIsDone.setDisable(true);
        } else {
            setUpWindowForUpdate(task);
        }
        stage.show();
    }

    public void setUpWindowForUpdate(Task task) {
        taskUpdateHeader.lengthProperty();
        taskUpdateId.setText(task.getId().toString());
        taskUpdateDate.setValue(task.getDeadline().toLocalDate());
        taskUpdateIsDone.setSelected(task.isDone());
        taskUpdateDescription.setText(task.getDescription());
        taskUpdateHeader.setText(task.getHeader());
        taskUpdateTime.setItems(FXCollections.observableList(loadTimes()));
        taskUpdateTime.setValue(new Time(task.getDeadline().getHour(), 0, 0));
    }

    public void updateOnClick(ActionEvent actionEvent) {
        if (taskUpdateId.getText().isBlank()) {
            createUpdateTask("create");
        } else {
            createUpdateTask("update");
        }
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        buttonCancel.getScene().getWindow().hide();
    }

    public List<Time> loadTimes() {
        List<Time> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            times.add(new Time(i, 0, 0));
        }
        return times;
    }

    public void createUpdateTask(String action) {
        if (action.equals("update")) {
            Task task = taskRepository.getById(Long.valueOf(taskUpdateId.getText()));
            makeTaskChanges(task);
        } else if (action.equals("create")) {
            makeTaskChanges(new Task());
        }
    }

    public void createTask(ActionEvent actionEvent) throws IOException {
        showUpdateTaskDialog(new Task());
    }

    public void makeTaskChanges(Task task) {
        try {
            if (taskUpdateId.getText().isBlank()) {
                task.setUser(user);
            }
            LocalDate date = taskUpdateDate.getValue();
            task.setDeadline(LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), taskUpdateTime.getValue().getHours(), 0, 0));
            task.setDone(taskUpdateIsDone.isSelected());
            task.setDescription(taskUpdateDescription.getText());
            task.setHeader(taskUpdateHeader.getText());
            taskRepository.save(task);
            loadData();
            buttonCancel.getScene().getWindow().hide();
        } catch (Exception e) {
            AlertTools.sendAlert(Alert.AlertType.WARNING, "Warning", "Error while saving the task", "Wrong input statements!");
        }
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        user = null;
        FXMLTools.setSceneAndDropBeans(context, "/templates/MainView.fxml", (Stage) buttonLogOut.getScene().getWindow(), "Task Manager");
    }
}
