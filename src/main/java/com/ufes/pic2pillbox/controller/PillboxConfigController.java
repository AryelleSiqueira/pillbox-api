package com.ufes.pic2pillbox.controller;

import com.ufes.pic2pillbox.dto.app.AppConfigDTO;
import com.ufes.pic2pillbox.dto.pillbox.PillboxConfigDTO;
import com.ufes.pic2pillbox.service.AppConfigService;
import com.ufes.pic2pillbox.service.PillboxConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/config", produces = "application/json")
public class PillboxConfigController {

    final PillboxConfigService pillboxCfgService;

    final AppConfigService appConfigService;


    @GetMapping
    public ResponseEntity<PillboxConfigDTO> getConfig() {
        return ResponseEntity.ok(pillboxCfgService.getConfig());
    }

    @GetMapping("/app")
    public ResponseEntity<AppConfigDTO> getAppConfig() {
        return ResponseEntity.ok(appConfigService.getConfig());
    }

    @PostMapping("/app")
    public Map<String, String> configure(@RequestBody AppConfigDTO appConfig) {
        return appConfigService.configure(appConfig);
    }
}
