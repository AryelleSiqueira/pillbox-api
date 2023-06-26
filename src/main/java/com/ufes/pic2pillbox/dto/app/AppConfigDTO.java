package com.ufes.pic2pillbox.dto.app;

import com.ufes.pic2pillbox.dto.pillbox.SnoozeConfigDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppConfigDTO {

    private Map<String, SlotDTO> slots;

    private SnoozeConfigDTO snoozeConfig;
}
