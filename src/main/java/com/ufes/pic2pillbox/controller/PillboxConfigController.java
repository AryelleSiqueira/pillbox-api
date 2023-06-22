package com.ufes.pic2pillbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pillbox", produces = "application/json")
public class PillboxConfigController {

    @GetMapping("/config")
    public String getConfig() {
        return "{\"slots\":{\"S1\":\"medicamento1\",\"S2\":\"medicamento2\",\"S3\":\"medicamento3\",\"S4\":\"medicamento4\",\"S5\":\"medicamento5\"},\"alarms\":[{\"time\":\"08:00\",\"slots\":[\"S1\",\"S2\"],\"lastStatus\":false},{\"time\":\"12:00\",\"slots\":[\"S1\",\"S3\",\"S4\"],\"lastStatus\":false},{\"time\":\"16:00\",\"slots\":[\"S1\",\"S2\"],\"lastStatus\":false},{\"time\":\"20:00\",\"slots\":[\"S1\",\"S3\",\"S5\"],\"lastStatus\":false},{\"time\":\"00:00\",\"slots\":[\"S1\",\"S2\"],\"lastStatus\":false}],\"snooze\":{\"timegap\":5,\"repeat_times\":2}}";
    }
}
