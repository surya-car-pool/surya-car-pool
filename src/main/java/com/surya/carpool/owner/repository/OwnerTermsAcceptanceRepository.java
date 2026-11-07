package com.surya.carpool.owner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surya.carpool.owner.model.OwnerTermsAcceptance;

public interface OwnerTermsAcceptanceRepository extends JpaRepository<OwnerTermsAcceptance, Long> {

	boolean existsByOwner_IdAndTermsVersionAndAcceptedTrue(Long ownerId, String termsVersion);

	Optional<OwnerTermsAcceptance> findTopByOwner_IdAndTermsVersionAndAcceptedTrueOrderByAcceptedOnDesc(
			Long ownerId, String termsVersion);
}
