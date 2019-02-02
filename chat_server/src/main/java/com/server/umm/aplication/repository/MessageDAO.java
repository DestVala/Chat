package com.server.umm.aplication.repository;

import com.server.umm.aplication.entity.message.Message;
import java.util.List;
import java.util.UUID;

public interface MessageDAO {
	boolean save(Message message);
	List<Message> getAll();
	List<Message> getMessageFromSender(UUID user);
	List<Message> getMessageFromRecipient(UUID user);
}
