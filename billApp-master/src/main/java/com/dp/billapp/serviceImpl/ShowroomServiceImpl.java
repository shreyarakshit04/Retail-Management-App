package com.dp.billapp.serviceImpl;

import com.dp.billapp.daoService.ShowroomDaoService;
import com.dp.billapp.model.Showroom;
import com.dp.billapp.service.ShowroomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShowroomServiceImpl implements ShowroomService {

    private ShowroomDaoService showroomDaoService;
    @Override
    public Showroom save(Showroom showroom) {
        return showroomDaoService.saveShowroomDetails(showroom);
    }

    @Override
    public List<Showroom> getAllShowroom() {
        return showroomDaoService.getAllShowroom();
    }

    @Override
    public Showroom update(Showroom showroom) {
        return showroomDaoService.updateShowroomDetails(showroom);
    }

    @Override
    public String delete(long id) {
        showroomDaoService.deleteShowroomDetails(id);
        return "Showroom Details Deleted";
    }

    @Override
    public Optional<Showroom> getShowroomById(long id) {
        return showroomDaoService.getShowroomById(id);
    }


}
