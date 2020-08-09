package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.view.RsEventView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@Repository
@Transactional
public interface RsEventRepository extends CrudRepository<RsEventDto, Integer> {
    @Override
    List<RsEventDto>  findAll();
    @Transactional
    void deleteAllById(int rsEventId);

    @Query("select rs from RsEventDto rs where rs.id >= :start and rs.id <= :end")
    List<RsEventDto> findAllByStartAndEnd(int start, int end);
}
