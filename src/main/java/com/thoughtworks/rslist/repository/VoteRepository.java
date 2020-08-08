package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.VoteDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VoteRepository extends PagingAndSortingRepository<VoteDto, Integer> {
    @Query(value = "select * from vote v ", nativeQuery = true)
    List<VoteDto> findAll();

    @Query(value = "select * from vote v where v.user_id = :userId and v.rs_event_id = :rsEventId", nativeQuery = true)
    List<VoteDto> findVoteFromUserIdAndRsEventId(int userId, int rsEventId);

    @Query(value = "select * from vote v where v.user_id = :userId and v.rs_event_id = :rsEventId", nativeQuery = true)
    List<VoteDto> findVoteFromUserIdAndRsEventIdAndPage(int userId, int rsEventId, Pageable page);

    @Query(value = "select  * from vote v where v.local_date_time >= :localDateTimeStart and v.local_date_time <= :localDateTimeEnd", nativeQuery = true)
    List<VoteDto> findByStartTimeAndEndTime(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);
}
