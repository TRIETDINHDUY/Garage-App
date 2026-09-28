package com.tridinh.repository;

import com.tridinh.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {

    /**
     * Load all garages with address eagerly (for homepage display).
     */
    @Query("SELECT g FROM Garage g LEFT JOIN FETCH g.address ORDER BY g.garageName")
    List<Garage> findAllWithAddress();

    /**
     * Load single garage with address eagerly (for detail page).
     */
    @Query("SELECT g FROM Garage g LEFT JOIN FETCH g.address WHERE g.garageId = :id")
    Optional<Garage> findByIdWithAddress(@Param("id") Long id);

    /**
     * CC Search: find active garages by city+state that offer the given service.
     * Uses JOIN FETCH so garageServices list is populated after query.
     */
    @Query("""
            SELECT DISTINCT g FROM Garage g
            LEFT JOIN FETCH g.address
            JOIN FETCH g.garageServices gs
            JOIN FETCH gs.service s
            WHERE LOWER(g.address.city) = LOWER(:city)
            AND LOWER(g.address.state) = LOWER(:state)
            AND LOWER(s.serviceCode) = LOWER(:serviceCode)
            AND gs.status = 'AVAILABLE'
            AND g.status = 'ACTIVE'
            """)
    List<Garage> searchByCityStateAndService(@Param("city") String city,
                                              @Param("state") String state,
                                              @Param("serviceCode") String serviceCode);

    /**
     * CC Search with optional street filter (partial match on garage address street).
     */
    @Query("""
            SELECT DISTINCT g FROM Garage g
            LEFT JOIN FETCH g.address
            JOIN FETCH g.garageServices gs
            JOIN FETCH gs.service s
            WHERE LOWER(g.address.city) = LOWER(:city)
            AND LOWER(g.address.state) = LOWER(:state)
            AND LOWER(s.serviceCode) = LOWER(:serviceCode)
            AND LOWER(g.address.street) LIKE LOWER(CONCAT('%', :street, '%'))
            AND gs.status = 'AVAILABLE'
            AND g.status = 'ACTIVE'
            """)
    List<Garage> searchByCityStateStreetAndService(@Param("city") String city,
                                                    @Param("state") String state,
                                                    @Param("street") String street,
                                                    @Param("serviceCode") String serviceCode);

    /**
     * UI Search: flexible search with all-optional params.
     * JOIN FETCH garageServices so serviceCode can be filtered in Java after query.
     */
    @Query("""
            SELECT DISTINCT g FROM Garage g
            LEFT JOIN FETCH g.address
            LEFT JOIN FETCH g.garageServices gs
            LEFT JOIN FETCH gs.service
            WHERE (:name IS NULL OR :name = '' OR LOWER(g.garageName) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:city IS NULL OR :city = '' OR LOWER(g.address.city) LIKE LOWER(CONCAT('%', :city, '%')))
            AND (:state IS NULL OR :state = '' OR LOWER(g.address.state) LIKE LOWER(CONCAT('%', :state, '%')))
            ORDER BY g.garageName
            """)
    List<Garage> searchUI(@Param("name") String name,
                          @Param("city") String city,
                          @Param("state") String state);
}
