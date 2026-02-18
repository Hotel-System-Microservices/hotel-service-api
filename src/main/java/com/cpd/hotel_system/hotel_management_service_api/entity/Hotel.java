package com.cpd.hotel_system.hotel_management_service_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Hotel {

    @Id
    @Column(name = "hotel_id")
    private String hotelId;

    @Column(name = "hotel_name",nullable = false,length = 100)
    private String hotelName;

    @Column(name = "star_rating",nullable = false)
    private int starRating;

    @Lob
    @Column(nullable = false,length = 100)
    private Blob description;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "active_status",nullable = false)
    private boolean activeStatus;

    @Column(name = "starting_form",nullable = false)
    private BigDecimal startingForm;

    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL)
    private List<Branch> branches;



}
