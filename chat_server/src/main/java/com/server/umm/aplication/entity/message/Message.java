package com.server.umm.aplication.entity.message;

import lombok.*;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.ValueGenerationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Message {

//	@Id
//	@GeneratedValue
//	@Column( columnDefinition = "uuid-char", updatable = false )
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID identifier = UUID.randomUUID();
	@Column(nullable = false)
	@Type(type="uuid-char")
	private UUID sender;
	@Column(nullable = false)
	@Type(type="uuid-char")
	private UUID recipient;
	@Column(nullable = false)
	private String textMsg;
	@Column(nullable = false)
	private Timestamp dateMsg = Timestamp.valueOf(LocalDateTime.now());
	@Column(nullable = false)
	private MessageStatus messageStatus;
}
