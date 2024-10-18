package com.dp.billapp.controller;

import com.dp.billapp.model.GoldRate;
import com.dp.billapp.service.GoldRateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value="/goldrate")
public class GoldRateController {

    GoldRateService goldrateService;
    @PostMapping("/save")
    public ResponseEntity<?> saveGoldRate(@RequestBody GoldRate goldrate){
        log.info("#  showroom - {}", goldrate);
        if(goldrate == null)
            return new ResponseEntity<>("Invalid input", HttpStatus.BAD_REQUEST);
        GoldRate rate = goldrateService.saveGoldrate(goldrate);
        return ResponseEntity.ok(rate);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getGoldRate(){
        List<GoldRate> rates = goldrateService.getGoldRate();
        return ResponseEntity.ok(rates.get(0));
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateGoldRate(@RequestBody GoldRate goldrate){
        return ResponseEntity.ok(goldrateService.updateGoldrate(goldrate));
    }
}
