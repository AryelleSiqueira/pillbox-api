package com.ufes.pic2pillbox.service;

import com.ufes.pic2pillbox.dto.pillbox.AlarmDTO;
import com.ufes.pic2pillbox.dto.pillbox.PillboxConfigDTO;
import com.ufes.pic2pillbox.exception.NoAssociatedUserException;
import com.ufes.pic2pillbox.model.Alarm;
import com.ufes.pic2pillbox.model.Slot;
import com.ufes.pic2pillbox.model.User;
import com.ufes.pic2pillbox.repository.CodeRepository;
import com.ufes.pic2pillbox.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PillboxConfigService {

    private final SlotRepository slotRepository;

    private final CodeRepository codeRepository;


    public PillboxConfigDTO getConfig() {
        final int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        codeRepository.findByUserId(userId)
                .orElseThrow(() -> new NoAssociatedUserException("No associated user."));

        final List<Slot> slots = slotRepository.findAllByUserId(userId);

        if (slots.isEmpty()) {
            return PillboxConfigDTO.builder().build();
        }
        final Map<String, String> slotsNames = new HashMap<>();
        final Map<Integer, Alarm> alarms = new HashMap<>();

        slots.forEach(slot -> {
            slotsNames.put(slot.getSlotNumber().name(), slot.getName());
            slot.getAlarms().forEach(alarm -> alarms.put(alarm.getId(), alarm));
        });

        final List<AlarmDTO> alarmDTOList = alarms.values().stream().map(alarm ->
            AlarmDTO.builder()
                    .hour(alarm.getHour())
                    .minute(alarm.getMinute())
                    .slots(alarm.getSlots().stream().map(Slot::getSlotNumber).collect(Collectors.toList()))
                    .build()
        ).collect(Collectors.toList());

        return PillboxConfigDTO.builder()
                .slots(slotsNames)
                .alarms(alarmDTOList)
                .build();
    }

}
