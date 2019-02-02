package com.server.umm.aplication.repository;

import com.server.umm.aplication.entity.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCRUD extends CrudRepository<User, UUID> {
    /**
     * Interface to create User DB operation - actually use 'String name' as unique field
     * TODO - include id as main save parameter instead name
     * */

    List<User> findAll();
    Optional<User> findByUserId(UUID id);
    Optional<User> findByNick(String nick);
    Optional<User> findByNickAndUserPassword(String nick, String pass);
    void deleteByUserId(UUID id);
    void deleteByNick(String nick);

    @Modifying
    @Transactional
    @Query(value = "update user set log_status = 1 where nick =:nickName", nativeQuery = true)
    void updateUserByNick(@Param("nickName") String nickName);

    @Modifying
    @Transactional
    @Query(value = "update user set log_status = 0 where nick =:nickName", nativeQuery = true)
    void updateLogoutUserStatus(@Param("nickName") String nickName);
}
