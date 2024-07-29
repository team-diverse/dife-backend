package com.dife.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Entity
@Component
@Table(name = "blacklistedMember")
public class BlacklistedMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Member blacklistOwner;

	@ManyToOne
	@JoinColumn(name = "blacklisted_member_id")
	private Member blacklistedMember;
}
