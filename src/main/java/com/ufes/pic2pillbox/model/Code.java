package com.ufes.pic2pillbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "code")
public class Code {

    @Id
    @Column(name = "code", unique = true, nullable = false)
    private Integer code;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
