package com.chat.smm.entity.user;

import lombok.*;


import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class User {
	private UUID userId = UUID.randomUUID();
	private String nick;
	private String statusText;
	private UserStatus userStatus;
	private String userPassword;
	private boolean logStatus;
}
