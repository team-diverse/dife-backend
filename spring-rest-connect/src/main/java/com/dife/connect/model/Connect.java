package com.dife.connect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "connect")
public class Connect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connect_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

}
