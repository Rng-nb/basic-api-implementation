package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.UserDto;
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
}
