package com.server.umm.aplication.entity.user;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"nick"})})
public class User {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID userId = UUID.randomUUID();
	@Column(name = "nick", unique = true, nullable = false)
	private String nick;
	@Column(nullable = true)
	private String statusText;
	@Column(nullable = false)
	private UserStatus userStatus;
	@Column(nullable = false)
	private String userPassword;
	@Column(nullable = false)
	private boolean logStatus;

}
