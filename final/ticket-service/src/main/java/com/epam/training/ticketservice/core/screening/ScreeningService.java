package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface ScreeningService {

    List<ScreeningDto> getScreeningList();

    Optional<Screening> createScreening(ScreeningDto screening);

    void deleteScreening(String movie, String room, LocalDateTime screeningDate);
}
