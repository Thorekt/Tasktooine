package com.thorekt.tasktooine_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Entity
@Table (name = "projects")
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class Project {
   
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column (nullable = false, length = 100)
    private String name;

    @Column (length = 500)
    private String description;

    @OneToMany (mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskList> taskLists;

}
