package mainpackage.repository;

import mainpackage.model.ReservationEntity;
import mainpackage.model.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query(value = "select r from ReservationEntity r where r.status = :status", nativeQuery = false)
    List<ReservationEntity> findAllByStatus(@Param("status") ReservationStatus status);

    //изменяющие запросы должны быть транзакционными
    @Transactional
    @Modifying
    @Query("""
            update ReservationEntity r
            set r.userId = :userId, r.roomId = :roomId, r.startDate = :startDate, r.endDate = :endDate, r.status = :status
            where r.id = :id
            """)
    int updateAllFields(@Param("id") Long id, @Param("userId") Long userId, @Param("roomId") Long roomId,
                        @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                        @Param("status") ReservationStatus status);


    @Modifying
    @Query("update ReservationEntity r set r.status = :status where r.id = :id ")
    int updateStatus(@Param("id") Long id, @Param("status") ReservationStatus status);

    @Query("""
            select r.id from ReservationEntity r
            where r.roomId = :roomId and :startDate < r.endDate and :endDate > r.startDate and r.status = :status
            and r.id != :id
            """)
    List<Long> findAllConflictReservationIds(@Param("id") Long id, @Param("roomId") Long roomId, @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate, @Param("status") ReservationStatus status);

    @Query("""
            select r from ReservationEntity r
            where (r.roomId = :roomId or :roomId is null) and (r.userId = :userId or :userId is null)
            """)
    List<ReservationEntity> searchAllByFilter(@Param("roomId") Long roomId, @Param("userId") Long userId, Pageable pageable);
}
