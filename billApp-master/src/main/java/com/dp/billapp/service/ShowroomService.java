package com.dp.billapp.service;

import com.dp.billapp.model.Showroom;

import java.util.List;
import java.util.Optional;

public interface ShowroomService {
    Showroom save(Showroom showroom);
    List<Showroom> getAllShowroom();
    Showroom update(Showroom showroom);
    String delete(long id);
    Optional<Showroom> getShowroomById(long id);
}
