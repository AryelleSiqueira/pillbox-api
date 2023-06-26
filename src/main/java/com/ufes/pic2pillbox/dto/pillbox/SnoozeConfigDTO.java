package com.ufes.pic2pillbox.dto.pillbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SnoozeConfigDTO {

    private Integer interval;

    private Integer repeat;
}
