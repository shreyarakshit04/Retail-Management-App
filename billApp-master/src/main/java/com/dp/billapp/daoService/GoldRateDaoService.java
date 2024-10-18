package com.dp.billapp.daoService;

import com.dp.billapp.model.GoldRate;

import java.util.List;

public interface GoldRateDaoService {
    GoldRate saveGoldrate(GoldRate goldrate);
    List<GoldRate> getGoldRate();
    GoldRate updateGoldrate(GoldRate goldrate);

}
