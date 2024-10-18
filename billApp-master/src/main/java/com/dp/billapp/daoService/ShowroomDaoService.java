package com.dp.billapp.daoService;

import com.dp.billapp.model.Showroom;

import java.util.List;
import java.util.Optional;

public interface ShowroomDaoService {
    Showroom saveShowroomDetails(Showroom showroom);
    List<Showroom> getAllShowroom();
    Showroom updateShowroomDetails(Showroom showroom);

    String  deleteShowroomDetails(long id);
    Optional<Showroom> getShowroomById(long id);
}
