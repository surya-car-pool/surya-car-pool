package com.surya.carpool.owner.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.surya.carpool.booking.model.BookingStatus;
import com.surya.carpool.booking.repository.BookingRepository;
import com.surya.carpool.car.repository.CarRepository;
import com.surya.carpool.owner.dto.OwnerDashboard;
import com.surya.carpool.user.model.User;

@Service
public class OwnerDashboardService {

	private final CarRepository carRepository;
	private final BookingRepository bookingRepository;
	private final OwnerTermsService ownerTermsService;

	public OwnerDashboardService(CarRepository carRepository, BookingRepository bookingRepository,
			OwnerTermsService ownerTermsService) {
		this.carRepository = carRepository;
		this.bookingRepository = bookingRepository;
		this.ownerTermsService = ownerTermsService;
	}

	public OwnerDashboard getDashboard(User owner) {
		var acceptance = ownerTermsService.getCurrentAcceptance(owner);
		BigDecimal earnings = bookingRepository.sumConfirmedEarningsByOwnerId(owner.getId());
		return new OwnerDashboard(owner.getName(), carRepository.countByOwner_Id(owner.getId()),
				bookingRepository.countByCar_Owner_IdAndStatus(owner.getId(), BookingStatus.ACTIVE),
				earnings == null ? BigDecimal.ZERO : earnings, owner.getRegisteredAt(),
				owner.getApprovalStatus() == null ? "PENDING" : owner.getApprovalStatus().name(),
				acceptance == null ? null : acceptance.getAcceptedOn());
	}
}
