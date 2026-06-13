package com.vamsi.task_manager.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = true)
	private String description;

	@Column(nullable = false)
	private LocalDate dueDate;

	@Column(nullable = false)
	private Priority priority;

	@Column(nullable = false)
	private Status status;
}
