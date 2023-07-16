package com.ufes.pic2pillbox.service;

import com.ufes.pic2pillbox.dto.pillbox.AlarmDTO;
import com.ufes.pic2pillbox.dto.pillbox.PillboxConfigDTO;
import com.ufes.pic2pillbox.exception.NoAssociatedUserException;
import com.ufes.pic2pillbox.model.Alarm;
import com.ufes.pic2pillbox.model.Code;
import com.ufes.pic2pillbox.model.Slot;
import com.ufes.pic2pillbox.model.User;
import com.ufes.pic2pillbox.repository.CodeRepository;
import com.ufes.pic2pillbox.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PillboxConfigService {

    private final SlotRepository slotRepository;

    private final CodeRepository codeRepository;

    private final JwtService jwtService;


    public PillboxConfigDTO getConfig(String token) {
        final int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        //int code = jwtService.extractAllClaims(token.substring(7)).get("code", Integer.class);
        log.info("Code: {}", token);

//        codeRepository.findById(code)
//                .map(Code::getUser)
//                .orElseThrow(() -> new NoAssociatedUserException("No associated user."));

        final List<Slot> slots = slotRepository.findAllByUserId(userId);

        if (slots.isEmpty()) {
            return PillboxConfigDTO.builder().build();
        }
        final Map<String, String> slotsNames = new HashMap<>();
        final Map<Integer, Alarm> alarms = new HashMap<>();

        slots.sort(Comparator.comparing(Slot::getSlotNumber));
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
        ).sorted((a1, a2) -> {
            if (a1.getHour() != a2.getHour()) {
                return a1.getHour() - a2.getHour();
            }
            return a1.getMinute() - a2.getMinute();
        }).collect(Collectors.toList());

        return PillboxConfigDTO.builder()
                .slots(slotsNames)
                .alarms(alarmDTOList)
                .build();
    }

}
