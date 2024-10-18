package com.dp.billapp.service;

import com.dp.billapp.model.GoldRate;

import java.util.List;

public interface GoldRateService {
    GoldRate saveGoldrate(GoldRate goldrate);
    List<GoldRate> getGoldRate();
    GoldRate updateGoldrate(GoldRate goldrate);
}
