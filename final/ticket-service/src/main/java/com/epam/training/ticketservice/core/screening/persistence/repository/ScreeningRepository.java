package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    List<Screening> findByRoomName(String roomName);

    @Transactional
    void deleteByMovieTitleAndRoomNameAndScreeningDate(String title, String roomName, LocalDateTime date);
}
