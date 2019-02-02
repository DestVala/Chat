package com.chat.smm.entity.message;

import lombok.*;
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
public class Message {

	private UUID identifier;
	private UUID sender;
	private UUID recipient;
	private String textMsg;
	private Timestamp dateMsg = Timestamp.valueOf(LocalDateTime.now());
	private MessageStatus messageStatus;
}
