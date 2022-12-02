package tech.tomberg.taskscheduler.entity;

import jakarta.persistence.*;
import javafx.scene.control.Button;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 50)
    private String header;

    @Column
    private String description;

    private LocalDateTime deadline;

    private boolean done;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @ToString.Exclude
    private User user;

    @Transient
    private Button update;
}