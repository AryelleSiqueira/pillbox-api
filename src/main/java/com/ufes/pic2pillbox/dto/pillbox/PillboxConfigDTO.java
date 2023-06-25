package com.ufes.pic2pillbox.dto.pillbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PillboxConfigDTO {

    private Map<String, String> slots;

    private List<AlarmDTO> alarms;

    private SnoozeConfigDTO snoozeConfig;
}
