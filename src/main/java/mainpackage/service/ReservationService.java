package mainpackage.service;

import mainpackage.model.Reservation;
import mainpackage.model.ReservationStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

//    private final Map<Long, Reservation> reservationsMap = Map.of(
//            1L, new Reservation(1L, 100L, 200L, LocalDate.now(), LocalDate.now().plusDays(1), ReservationStatus.PENDING),
//            2L, new Reservation(2L, 200L, 300L, LocalDate.now(), LocalDate.now().plusDays(5), ReservationStatus.APPROVED),
//            3L, new Reservation(3L, 300L, 500L, LocalDate.now(), LocalDate.now().plusDays(11), ReservationStatus.CANCELLED)
//    );
    private final AtomicLong countId;
    private final Map<Long, Reservation> reservationsMap;

    public ReservationService() {
        this.reservationsMap = new HashMap<>();
        countId = new AtomicLong();
    }

    public Reservation getReservationById(Long id) {
        if (!reservationsMap.containsKey(id)) {
            throw new NoSuchElementException("Reservation not found with id %s".formatted(id));
        }
        Reservation reservation = reservationsMap.get(id);
        return reservation;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservationsMap.values().forEach(reservation -> reservations.add(reservation));
        return reservations;
    }

    public Reservation createReservation(Reservation reservationToCreate) {
        if (reservationToCreate.id() != null && reservationToCreate.status() != null) {
            throw new IllegalArgumentException("Id & status should be empty");
        }
        Long idForNewReservation = countId.incrementAndGet();
        Reservation reservation = new Reservation(idForNewReservation, reservationToCreate.userId(), reservationToCreate.roomId(),
                reservationToCreate.startDate(), reservationToCreate.endDate(), ReservationStatus.PENDING);

        reservationsMap.put(idForNewReservation, reservation);
        return reservation;
    }

    public Reservation updateReservation(Long id, Reservation reservationToUpdate) {
        if (!reservationsMap.containsKey(id)) {
            throw new NoSuchElementException("Reservation not found with id %s".formatted(id));
        }
        Reservation reservationExist = reservationsMap.get(id);
        if (!reservationExist.status().equals(ReservationStatus.PENDING)) {
            throw new IllegalArgumentException("Reservation can not be edited with status %s".formatted(reservationExist.status()));
        }
        Reservation reservation = new Reservation(id, reservationToUpdate.userId(), reservationToUpdate.roomId(),
                reservationToUpdate.startDate(), reservationToUpdate.endDate(), ReservationStatus.PENDING);

        reservationsMap.put(id, reservation);
        return reservation;
    }

    public void deleteReservation(Long id) {
        if (!reservationsMap.containsKey(id)) {
            throw new NoSuchElementException("Reservation not found with id %s".formatted(id));
        }
        reservationsMap.remove(id);
    }

    public Reservation approveReservation(Long id) {
        if (!reservationsMap.containsKey(id)) {
            throw new NoSuchElementException("Reservation not found with id %s".formatted(id));
        }
        Reservation reservationToAprove = reservationsMap.get(id);
        if (!reservationToAprove.status().equals(ReservationStatus.PENDING)) {
            throw new IllegalArgumentException("Reservation can not be approved with status %s".formatted(reservationToAprove.status()));
        }
        if (crosWithOtherReservations(reservationToAprove)) {
            throw new IllegalArgumentException("Reservation can not be approved with cross other reservation's dates %s %s".formatted(reservationToAprove.startDate(), reservationToAprove.endDate()));
        }

        Reservation reservation = new Reservation(id, reservationToAprove.userId(), reservationToAprove.roomId(),
                reservationToAprove.startDate(), reservationToAprove.endDate(), ReservationStatus.APPROVED);
        reservationsMap.put(id, reservation);

        return reservation;
    }

    private Boolean crosWithOtherReservations(Reservation reservation) {
        Boolean t = false;//совпадения с другими комнатами
//        if (reservationsMap.values().stream().anyMatch(res -> res.roomId().equals(reservation.roomId()))) {
//            t = true;
//        }
        for (Reservation existingReservation: reservationsMap.values()) {
            if (existingReservation.id().equals(reservation.id()))//одна и та же бронь (объект)
                continue;
            if (!existingReservation.roomId().equals(reservation.roomId()))//не одна и та же комната
                continue;
            if (!existingReservation.status().equals(ReservationStatus.APPROVED))//не подтверждённая бронь
                continue;
            if (reservation.startDate().isBefore(existingReservation.endDate()) && reservation.endDate().isAfter(existingReservation.startDate()))//пересечение дат
                t = true;

        }
        return t;
    }

}
