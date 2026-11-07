package com.surya.carpool.owner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surya.carpool.owner.model.OwnerTermsAcceptance;
import com.surya.carpool.owner.repository.OwnerTermsAcceptanceRepository;
import com.surya.carpool.user.model.ApprovalStatus;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

@Service
public class OwnerTermsService {

	private final OwnerTermsAcceptanceRepository acceptanceRepository;
	private final UserRepository userRepository;
	private final String currentTermsVersion;

	public OwnerTermsService(OwnerTermsAcceptanceRepository acceptanceRepository,
			UserRepository userRepository,
			@Value("${surya.owner-terms.version:2026-11-07}") String currentTermsVersion) {
		this.acceptanceRepository = acceptanceRepository;
		this.userRepository = userRepository;
		this.currentTermsVersion = currentTermsVersion;
	}

	public String getCurrentTermsVersion() {
		return currentTermsVersion;
	}

	public boolean hasAcceptedCurrentTerms(User owner) {
		return acceptanceRepository.existsByOwner_IdAndTermsVersionAndAcceptedTrue(owner.getId(),
				currentTermsVersion);
	}

	public OwnerTermsAcceptance getCurrentAcceptance(User owner) {
		return acceptanceRepository
				.findTopByOwner_IdAndTermsVersionAndAcceptedTrueOrderByAcceptedOnDesc(owner.getId(),
						currentTermsVersion)
				.orElse(null);
	}

	@Transactional
	public void acceptCurrentTerms(User owner, String ipAddress) {
		User account = userRepository.findById(owner.getId())
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));
		if (account.getRole() != Role.OWNER && account.getRole() != Role.USER) {
			throw new IllegalArgumentException("Only customer or owner accounts can accept the owner agreement");
		}
		if (account.getRole() == Role.USER) {
			account.setRole(Role.OWNER);
			account.setApprovalStatus(ApprovalStatus.PENDING);
			userRepository.save(account);
			owner.setRole(Role.OWNER);
			owner.setApprovalStatus(ApprovalStatus.PENDING);
		}
		if (!hasAcceptedCurrentTerms(account)) {
			acceptanceRepository.save(new OwnerTermsAcceptance(account, currentTermsVersion, ipAddress));
		}
	}
}
