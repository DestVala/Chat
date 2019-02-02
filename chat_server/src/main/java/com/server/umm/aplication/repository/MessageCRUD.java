package com.server.umm.aplication.repository;

import com.server.umm.aplication.entity.message.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageCRUD extends CrudRepository<Message, UUID>{
   Optional<Message> findBySenderAndRecipientAndTextMsgAndDateMsg(UUID sender, UUID recipient, String text, Timestamp date);
   List<Message> findBySender(UUID sender);
   List<Message> findByRecipient(UUID recipient);
   List<Message> findBySenderAndRecipientOrSenderAndRecipient(UUID sender, UUID recipient,UUID recipientt, UUID senderr);
   List<Message> findBySenderAndRecipient(UUID sender, UUID recipient);
   Message findByIdentifier(UUID identifier);
   Message findByTextMsg(String textMsg);


   @Modifying
   @Transactional
   @Query(value = "update message set message_status = 1 where recipient =:uuid and message_status = 0", nativeQuery = true)
   void setMessageStatusReceivedByRecipient(@Param("uuid") String uuid);

   @Modifying
   @Transactional
   @Query(value = "update message set message_status = 2 " +
           "where recipient =:uuidRecipient and sender =:uuidSender", nativeQuery = true)
   void setMessageStatusReadBySenderAndRecipient(@Param("uuidSender") String uuidSender,@Param("uuidRecipient") String uuidRecipient);

}
