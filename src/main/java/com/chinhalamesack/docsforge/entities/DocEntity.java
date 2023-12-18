package com.chinhalamesack.docsforge.entities;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity(name="doc")
public class DocEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	@Column(columnDefinition = "LONGTEXT")
	private String text;
	@CreationTimestamp
	private LocalDateTime createdAt;


}
