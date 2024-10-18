package com.dp.billapp.daoServiceImpl;

import com.dp.billapp.daoService.GoldRateDaoService;
import com.dp.billapp.model.GoldRate;
import com.dp.billapp.repository.GoldrateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GoldRateDaoServiceImpl implements GoldRateDaoService {

    GoldrateRepository goldrateRepository;
    @Override
    public GoldRate saveGoldrate(GoldRate goldrate) {
        return goldrateRepository.save(goldrate);
    }

    @Override
    public List<GoldRate> getGoldRate() {
        return goldrateRepository.findAll();
    }

    @Override
    public GoldRate updateGoldrate(GoldRate goldrate) {
        return goldrateRepository.save(goldrate);
    }
}
