package com.dp.billapp.serviceImpl;

import com.dp.billapp.daoService.GoldRateDaoService;
import com.dp.billapp.model.GoldRate;
import com.dp.billapp.service.GoldRateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class GoldRateServiceImpl implements GoldRateService {
    GoldRateDaoService goldrateDaoService;
    @Override
    public GoldRate saveGoldrate(GoldRate goldrate) {
        return goldrateDaoService.saveGoldrate(goldrate);
    }

    @Override
    public List<GoldRate> getGoldRate() {
        List<GoldRate> rates = goldrateDaoService.getGoldRate();
        return rates;
    }

    @Override
    public GoldRate updateGoldrate(GoldRate goldrate) {

        return goldrateDaoService.updateGoldrate(goldrate);
    }
}
