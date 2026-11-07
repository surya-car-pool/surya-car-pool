package com.surya.carpool.owner.model;

import java.time.LocalDateTime;

import com.surya.carpool.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "owner_terms_acceptance")
public class OwnerTermsAcceptance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@Column(name = "terms_version", nullable = false, length = 50)
	private String termsVersion;

	@Column(nullable = false)
	private boolean accepted = true;

	@Column(name = "accepted_on", nullable = false, updatable = false)
	private LocalDateTime acceptedOn;

	@Column(name = "ip_address", length = 100)
	private String ipAddress;

	protected OwnerTermsAcceptance() {
	}

	public OwnerTermsAcceptance(User owner, String termsVersion, String ipAddress) {
		this.owner = owner;
		this.termsVersion = termsVersion;
		this.ipAddress = ipAddress;
	}

	@PrePersist
	private void setAcceptedOn() {
		if (acceptedOn == null) {
			acceptedOn = LocalDateTime.now();
		}
	}

	public LocalDateTime getAcceptedOn() {
		return acceptedOn;
	}
}
