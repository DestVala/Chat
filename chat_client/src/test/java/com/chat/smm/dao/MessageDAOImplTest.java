package com.chat.smm.dao;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_10;


public class MessageDAOImplTest {
//
//    EmbeddedMysql mysqld;
//
//    @Before
//    public void before() {
//        MysqldConfig config = aMysqldConfig(v5_7_10).withPort(4545).withUser("test", "test").build();
//        mysqld = anEmbeddedMysql(config).addSchema("chatdb", classPathScript("NewFixedDBProject.sql")).start();
//    }
//
//    @After
//    public void after() {
//        mysqld.stop();
//    }
//
//    @Test
//    public void save() {
//        MessageDAOImpl messageDAO = new MessageDAOImpl("test", "test", "localhost", 4545, "chatdb");
//        Message message = Message.builder()
//                .text("Piwo?")
//                .identifier(UUID.randomUUID())
//                .recipient("Jan Kowala")
//                .sender("Sylwester")
//                .date(Timestamp.valueOf(LocalDateTime.now().withNano(0)))
//                .build();
//        messageDAO.save(message);
//        List<Message> messageList = messageDAO.getAll();
//
//        Assert.assertTrue("True if insert was correct", messageDAO.save(message));
//        Assert.assertEquals(message, messageList.get(0));
//    }
//
//    @Test
//    public void getAll() {
//        MessageDAOImpl messageDAO = new MessageDAOImpl("test", "test", "localhost", 4545, "chatdb");
//        List<Message> listOfMessage1 = messageDAO.getAll();
//        Message message = Message.builder()
//                .text("Dzień dobry, miłego dnia!")
//                .identifier(UUID.randomUUID())
//                .recipient("Paweł Pawełkowski")
//                .sender("Zenon Zenonowski")
//                .date(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        messageDAO.save(message);
//        List<Message> listOfMessage2 = messageDAO.getAll();
//        Assert.assertEquals(listOfMessage1.size() + 1, listOfMessage2.size());
//    }
//
//    @Test
//    public void getMessageFromSender() {
//        MessageDAOImpl messageDAO = new MessageDAOImpl("test", "test", "localhost", 4545, "chatdb");
//        List<Message> listOfMessage1 = messageDAO.getMessageFromSender("Malinaaa");
//        Message message = Message.builder()
//                .text("Abcdef")
//                .identifier(UUID.randomUUID())
//                .recipient("Andrzej Kropka")
//                .sender("Malinaaa")
//                .build();
//        messageDAO.save(message);
//        List<Message> listOfMessage2 = messageDAO.getMessageFromSender("Malinaaa");
//        Assert.assertEquals(listOfMessage1.size() + 1, listOfMessage2.size());
//    }
//
//    @Test
//    public void getMessageFromRecipient() {
//        MessageDAOImpl messageDAO = new MessageDAOImpl("test", "test", "localhost", 4545, "chatdb");
//        List<Message> messageFromDB1 = messageDAO.getMessageFromRecipient("Jan Janowicz");
//        Message message = Message.builder()
//                .text("qwerty")
//                .identifier(UUID.randomUUID())
//                .recipient("Jan Janowicz")
//                .sender("Piotr Piotrowicz")
//                .build();
//        messageDAO.save(message);
//        List<Message> messageFromDB2 = messageDAO.getMessageFromRecipient("Jan Janowicz");
//        Assert.assertEquals(messageFromDB1.size() + 1, messageFromDB2.size());
//    }
}