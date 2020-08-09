package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.UserDto;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<UserDto, Integer> {
    @Override
    List<UserDto> findAll();
    @Transactional
    UserDto getAllById(int userId);
    @Transactional

    @Modifying
    @Query (value = "delete from user where user.id = :userId", nativeQuery = true)
    void deleteAccordingId(int userId);
}
