package com.ufes.pic2pillbox.controller;

import com.ufes.pic2pillbox.dto.pillbox.PillboxConfigDTO;
import com.ufes.pic2pillbox.service.PillboxConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/config", produces = "application/json")
public class PillboxConfigController {

    final PillboxConfigService pillboxCfgService;


    @GetMapping
    public ResponseEntity<PillboxConfigDTO> getConfig2() {
        return ResponseEntity.ok(pillboxCfgService.getConfig());
    }
}
