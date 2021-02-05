package inu.project.shareu.controller;

import inu.project.shareu.model.request.major.MajorSaveRequest;
import inu.project.shareu.model.request.major.MajorUpdateRequest;
import inu.project.shareu.service.MajorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @PostMapping("/majors")
    public ResponseEntity saveMajor(@ModelAttribute MajorSaveRequest majorSaveRequest){

        majorService.saveMajor(majorSaveRequest);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/majors/{id}")
    public ResponseEntity updateMajor(@PathVariable Long id,
                                      @ModelAttribute MajorUpdateRequest majorUpdateRequest){
        majorService.updateMajor(id,majorUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/majors/{id}")
    public ResponseEntity deleteMajor(@PathVariable Long id){
        majorService.deleteMajor(id);

        return ResponseEntity.ok().build();
    }
}
