package com.chat.smm.dao;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_10;

/**
 * Created by Michal Ziolecki.
 */
public class UserDAOImplTest {
//
//    EmbeddedMysql mysqld;
//
//    @Before
//    public void before() {
//        MysqldConfig config = aMysqldConfig(v5_7_10).withPort(4545).withUser("test", "test").build();
//        mysqld = anEmbeddedMysql(config).addSchema("chatdb", classPathScript("NewFixedDBProject.sql")).start();
//    }
//
//
//    @After
//    public void after() {
//        mysqld.stop();
//    }
//
//
//    @Test
//    public void save() throws Exception {
//
//        UserDAOImpl dao = new UserDAOImpl("test", "test", "localhost" , 4545 , "chatdb" );
//        User user = User.builder().nick("lll").status(UserStatus.available).statusText("Hello").build();
//        dao.save(user);
//        List<User> userFromDb = dao.getUsers();
//        Assert.assertEquals(1, userFromDb.size());
//        User userFromDB = userFromDb.get(0);
//        Assert.assertEquals(user, userFromDB);
//    }
//
//    @Test
//    public void updateUser() throws Exception {
//        UserDAOImpl dao = new UserDAOImpl("test", "test", "localhost" , 4545 , "chatdb" );
//        User user = User.builder()
//                .nick( "JerzyPompka3" )
//                .statusText( "5Cytryny" )
//                .status( UserStatus.notDisturb )
//                .build();
//        dao.save(user);
//        User user2 = User.builder()
//                .nick( "JerzyPompka3" )
//                .statusText( "okon" )
//                .status( UserStatus.away )
//                .build();
//        dao.updateUser(user2);
//        List<User> usersFromDb = dao.getUsers();
//        User usersFromDB = usersFromDb.get(0);
//        Assert.assertEquals(user2, usersFromDB);
//        Assert.assertTrue( "True if update was correct", dao.updateUser( user ) );
//    }
//
//    @Test
//    public void getUsers() throws Exception {
//        // check number of users
//        UserDAOImpl dao = new UserDAOImpl("test", "test", "localhost" , 4545 , "chatdb" );
//        List<User> resultListOfUser1 = dao.getUsers();
//        int numberOfUsersBeforeChange = resultListOfUser1.size();
//        // insert few test users
//        User user1 = User.builder()
//                .nick( "Malina55" )
//                .statusText( "Cytryna" )
//                .status( UserStatus.away )
//                .build();
//        dao.save( user1 );
//        User user2 = User.builder()
//                .nick( "Malina66" )
//                .statusText( "Cytryna" )
//                .status( UserStatus.away )
//                .build();
//        dao.save( user2 );
//        // check new number of users
//        List<User> resultListOfUser2 = dao.getUsers();
//        Assert.assertEquals( numberOfUsersBeforeChange+2, resultListOfUser2.size() );
//    }
//
//    @Test
//    public void getOneUser() throws Exception {
//        UserDAOImpl dao = new UserDAOImpl("test", "test", "localhost" , 4545 , "chatdb" );
//        // object to test
//        User testUser = User.builder()
//                .nick( "Malina44" )
//                .statusText( "Cytryna" )
//                .status( UserStatus.away )
//                .build();
//        dao.save(testUser);
//        User userFromDB = dao.getOneUser( "Malina44" );
//        Assert.assertNotNull( userFromDB );
//        Assert.assertEquals( userFromDB.getStatus().toString(), testUser.getStatus().toString() );
//    }

}