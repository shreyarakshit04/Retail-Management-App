package com.dp.billapp.controller;


import com.dp.billapp.model.Showroom;
import com.dp.billapp.service.ShowroomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value="/showroom")
@AllArgsConstructor

public class ShowroomController {

    private ShowroomService showroomService;

    @PostMapping("/save")
    public ResponseEntity<?> saveShowroom(@RequestBody Showroom showroom){
        log.info("#  showroom - {}", showroom);
        if(showroom == null)
            return new ResponseEntity<>("Invalid input", HttpStatus.BAD_REQUEST);
       Showroom details =showroomService.save(showroom);
       return ResponseEntity.ok(details);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllShowroom(){
        List<Showroom> allShowroom = showroomService.getAllShowroom();
        return ResponseEntity.ok(allShowroom);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> findShowroomById( @PathVariable long id){
       Optional<Showroom> showroomOptional =showroomService.getShowroomById(id);
       if(showroomOptional.isEmpty())
           return new ResponseEntity<>("Showroom doesn't exist",HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(showroomOptional);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateShowroom(@RequestBody Showroom showroom){
        return ResponseEntity.ok(showroomService.update(showroom));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteShowroom(@PathVariable long id){
        Optional<Showroom> showroom =showroomService.getShowroomById(id);
        if(showroom.isEmpty())
            return new ResponseEntity<>("Showroom doesn't exist,can't be deleted",HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(showroomService.delete(id));
    }
}
