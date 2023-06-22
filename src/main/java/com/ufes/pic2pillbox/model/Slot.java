package com.ufes.pic2pillbox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ufes.pic2pillbox.enums.SlotNumber;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "slot")
public class Slot {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "slot_number", nullable = false)
    private SlotNumber slotNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "alarm_slot",
            joinColumns = @JoinColumn(name = "slot_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "alarm_id", nullable = false))
    private List<Alarm> alarms;
}
