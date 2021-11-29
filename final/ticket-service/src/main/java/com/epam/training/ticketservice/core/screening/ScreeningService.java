package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    List<ScreeningDto> getScreeningList();

    Object createScreening(ScreeningDto screening);

    void deleteScreening(String movie, String room, LocalDateTime screeningDate);
}
