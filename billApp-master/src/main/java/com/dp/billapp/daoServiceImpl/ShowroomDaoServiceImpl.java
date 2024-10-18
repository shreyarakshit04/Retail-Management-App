package com.dp.billapp.daoServiceImpl;

import com.dp.billapp.daoService.ShowroomDaoService;
import com.dp.billapp.model.Showroom;
import com.dp.billapp.repository.ShowroomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShowroomDaoServiceImpl implements ShowroomDaoService {

    private ShowroomRepository showroomRepository;
    @Override
    public Showroom saveShowroomDetails(Showroom showroom) {
        return showroomRepository.save(showroom);
    }

    @Override
    public List<Showroom> getAllShowroom() {
        return showroomRepository.findAll();
    }

    @Override
    public Showroom updateShowroomDetails(Showroom showroom) {
        return showroomRepository.save(showroom);
    }

    @Override
    public String deleteShowroomDetails(long id) {
        showroomRepository.deleteById(id);
        return "Showroom Details Deleted";
    }

    @Override
    public Optional<Showroom> getShowroomById(long id) {
        return showroomRepository.findById(id);
    }
}
