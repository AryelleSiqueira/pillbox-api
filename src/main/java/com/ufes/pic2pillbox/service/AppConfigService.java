package com.ufes.pic2pillbox.service;

import com.ufes.pic2pillbox.dto.app.AppConfigDTO;
import com.ufes.pic2pillbox.dto.app.SlotDTO;
import com.ufes.pic2pillbox.dto.app.AlarmDTO;
import com.ufes.pic2pillbox.dto.pillbox.SnoozeConfigDTO;
import com.ufes.pic2pillbox.model.Alarm;
import com.ufes.pic2pillbox.model.Slot;
import com.ufes.pic2pillbox.model.SlotNumber;
import com.ufes.pic2pillbox.model.User;
import com.ufes.pic2pillbox.repository.AlarmRepository;
import com.ufes.pic2pillbox.repository.SlotRepository;
import com.ufes.pic2pillbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppConfigService {

    //private final UserRepository userRepository;

    private final SlotRepository slotRepository;

    private final AlarmRepository alarmRepository;


    public AppConfigDTO getConfig() {
        final int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        final List<Slot> slots = slotRepository.findAllByUserId(userId);

        if (slots.isEmpty()) {
            return AppConfigDTO.builder().build();
        }
        //final User user = slots.get(0).getUser();
        final Map<String, SlotDTO> slotsMap = new HashMap<>();

        slots.forEach(slot -> {
            final List<AlarmDTO> alarms = slot.getAlarms().stream().map(alarm ->
                    AlarmDTO.builder().hour(alarm.getHour()).minute(alarm.getMinute()).build()
            ).collect(Collectors.toList());

            slotsMap.put(slot.getSlotNumber().name(), SlotDTO.builder()
                    .name(slot.getName())
                    .alarms(alarms)
                    .build());
        });

        return AppConfigDTO.builder()
                .slots(slotsMap)
                //.snoozeConfig(SnoozeConfigDTO.builder().interval(user.getSnoozeInterval()).repeat(user.getSnoozeRepeat()).build())
                .build();
    }

    @Transactional
    public Map<String, String> configure(AppConfigDTO appConfig) {
        final int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        //final User user = userRepository.findById(userId).orElse(new User());
        //user.setSnoozeInterval(appConfig.getSnoozeConfig().getInterval());
        //user.setSnoozeRepeat(appConfig.getSnoozeConfig().getRepeat());

        final List<Slot> oldSlots = slotRepository.findAllByUserId(userId);
        deleteSlotsAndAlarms(oldSlots);

        final List<Slot> slots = new ArrayList<>();
        final List<Alarm> alarms = new ArrayList<>();
        final Map<Integer, List<Alarm>> alarmsBySlotId = new HashMap<>();

        appConfig.getSlots().forEach((slotNumber, slot) -> {
            final Slot s = Slot.builder()
                    .name(slot.getName())
                    .slotNumber(SlotNumber.valueOf(slotNumber))
                    .user(User.builder().id(userId).build())
                    .build();

            final Slot newSlot = slotRepository.save(s);
            slots.add(newSlot);

            slot.getAlarms().forEach(alarm -> {
                Alarm newAlarm = Alarm.builder()
                        .hour(alarm.getHour())
                        .minute(alarm.getMinute())
                        .build();

                if (!alarms.contains(newAlarm)) {
                    newAlarm = alarmRepository.save(newAlarm);
                    alarms.add(newAlarm);
                } else {
                    newAlarm = alarms.get(alarms.indexOf(newAlarm));
                }
                alarmsBySlotId.computeIfAbsent(newSlot.getId(), k -> new ArrayList<>());
                alarmsBySlotId.get(newSlot.getId()).add(newAlarm);
            });
        });
        slots.forEach(slot -> {
            slot.setAlarms(alarmsBySlotId.get(slot.getId()));
            slotRepository.save(slot);
        });

        return Map.ofEntries(
                Map.entry("message", "Configuração salva com sucesso!"),
                Map.entry("status", "success"));
    }

    private void deleteSlotsAndAlarms(List<Slot> slots) {
        if (!slots.isEmpty()) {
            final List<Integer> deletedAlarms = new ArrayList<>();

            slots.forEach(slot -> {
                slot.getAlarms().forEach(alarm -> {
                    if (!deletedAlarms.contains(alarm.getId())) {
                        alarmRepository.delete(alarm);
                        deletedAlarms.add(alarm.getId());
                    }
                });
                slotRepository.delete(slot);
            });
        }
    }
}
