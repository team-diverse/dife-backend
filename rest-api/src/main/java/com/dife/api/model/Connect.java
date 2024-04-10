package com.dife.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "connect", uniqueConstraints = @UniqueConstraint(columnNames = {"member1_id", "member2_id"}))
public class Connect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member1_id", referencedColumnName = "id")
    private Member member1;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member2_id", referencedColumnName = "id")
    private Member member2;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ConnectStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PreUpdate
    @PrePersist
    private void sortMember() {
        if (this.member1.getId() > this.member2.getId()) {
            Member temp = this.member1;
            this.member1 = this.member2;
            this.member2 = temp;
        }
    }
}
