package mainpackage.service;

import jakarta.persistence.EntityNotFoundException;
import mainpackage.model.ReservationDTO;
import mainpackage.model.ReservationEntity;
import mainpackage.model.ReservationStatus;
import mainpackage.repository.ReservationRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;

    public ReservationService(ReservationRepository reservationRepository, ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.modelMapper = modelMapper;
    }

    public ReservationDTO getReservationById(Long id) {
        ReservationEntity reservationEntity = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reservation not found with id %s".formatted(id)));

        ReservationDTO reservationDTO = modelMapper.map(reservationEntity, ReservationDTO.class);
        return reservationDTO;
    }

    public List<ReservationDTO> getAllReservations() {
        List<ReservationDTO> reservationDTOS = new ArrayList<>();
        List<ReservationEntity> allEntities = reservationRepository.findAll();
        reservationDTOS = allEntities.stream().map(entity -> modelMapper.map(entity, ReservationDTO.class)).collect(Collectors.toList());

        return reservationDTOS;
    }

    public ReservationDTO createReservation(ReservationDTO reservationDTOToCreate) {
        if (reservationDTOToCreate.getStatus() != null) {
            throw new IllegalArgumentException("Id & status should be empty");
        }
        if (!validDates(reservationDTOToCreate)) {
            throw new IllegalArgumentException("Start date should be before end date and they should not be equals");
        }

        ReservationEntity reservationEntity = modelMapper.map(reservationDTOToCreate, ReservationEntity.class);
        reservationEntity.setStatus(ReservationStatus.PENDING);
        reservationEntity = reservationRepository.save(reservationEntity);
        modelMapper.map(reservationEntity, reservationDTOToCreate);
        return reservationDTOToCreate;
    }

    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTOToUpdate) {
        ReservationEntity reservationEntity = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reservation not found with id %s".formatted(id)));

        if (!reservationEntity.getStatus().equals(ReservationStatus.PENDING)) {
            throw new IllegalArgumentException("Reservation can not be edited with status %s".formatted(reservationEntity.getStatus()));
        }
        if (!validDates(reservationDTOToUpdate)) {
            throw new IllegalArgumentException("Start date should be before end date and they should not be equals");
        }

        reservationEntity = modelMapper.map(reservationDTOToUpdate, ReservationEntity.class);
        reservationEntity.setId(id);//оставлять айди и статус
        reservationEntity.setStatus(ReservationStatus.PENDING);

        reservationEntity = reservationRepository.save(reservationEntity);
        modelMapper.map(reservationEntity, reservationDTOToUpdate);
        return reservationDTOToUpdate;
    }

    @Transactional
    public void cancelReservation(Long id) {
        ReservationEntity reservationEntity = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reservation not found with id %s".formatted(id)));
        //отменять можно только неподтверждённые и неотменённые бронирования
        if (reservationEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Reservation can not be cancelled with status %s".formatted(reservationEntity.getStatus()));
        }


        reservationRepository.updateStatus(id, ReservationStatus.CANCELLED);
        log.info("Reservation with id {} has been cancelled", id);
    }

    public ReservationDTO approveReservation(Long id) {
        ReservationEntity reservationEntity = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reservation not found with id %s".formatted(id)));

        if (!reservationEntity.getStatus().equals(ReservationStatus.PENDING)) {
            throw new IllegalArgumentException("Reservation can not be approved with status %s".formatted(reservationEntity.getStatus()));
        }
        if (crosWithOtherReservations(reservationEntity)) {
            throw new IllegalArgumentException("Reservation can not be approved with cross other reservation's dates %s %s".formatted(reservationEntity.getStartDate(), reservationEntity.getEndDate()));
        }

        reservationEntity.setStatus(ReservationStatus.APPROVED);
        reservationRepository.save(reservationEntity);
        ReservationDTO reservationDTO = new ReservationDTO();
        modelMapper.map(reservationEntity, reservationDTO);


        return reservationDTO;
    }

    private Boolean validDates(ReservationDTO reservationDTO) {
        if (reservationDTO.getStartDate().isBefore(reservationDTO.getEndDate()))
            return true;
        return false;
    }

    private Boolean crosWithOtherReservations(ReservationEntity reservationEntity) {
        Boolean t = false;//совпадения с другими комнатами
        List<ReservationEntity> allReservations = reservationRepository.findAll();
        for (ReservationEntity existingReservation : allReservations) {
            if (existingReservation.getId().equals(reservationEntity.getId()))//одна и та же бронь (объект)
                continue;
            if (!existingReservation.getRoomId().equals(reservationEntity.getRoomId()))//не одна и та же комната
                continue;
            if (!existingReservation.getStatus().equals(ReservationStatus.APPROVED))//не подтверждённая бронь
                continue;
            if (reservationEntity.getStartDate().isBefore(existingReservation.getEndDate()) && reservationEntity.getEndDate().isAfter(existingReservation.getStartDate()))//пересечение дат
                t = true;

        }
        return t;
    }

}
