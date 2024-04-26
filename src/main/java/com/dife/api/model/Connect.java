package com.dife.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(
		name = "connect",
		uniqueConstraints = @UniqueConstraint(columnNames = {"from_member_id", "to_member_id"}))
public class Connect {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "from_member_id", referencedColumnName = "id")
	private Member fromMember;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "to_member_id", referencedColumnName = "id")
	private Member toMember;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ConnectStatus status;

	@CreationTimestamp private LocalDateTime createdAt;
}
