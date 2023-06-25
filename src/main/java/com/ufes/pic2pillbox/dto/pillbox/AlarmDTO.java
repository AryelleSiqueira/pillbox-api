package com.ufes.pic2pillbox.dto.pillbox;

import com.ufes.pic2pillbox.model.SlotNumber;
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
public class AlarmDTO {

    private int hour;

    private int minute;

    private SlotNumber[] slots;
}
