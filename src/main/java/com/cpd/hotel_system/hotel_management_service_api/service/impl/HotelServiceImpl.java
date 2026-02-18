package com.cpd.hotel_system.hotel_management_service_api.service.impl;

import com.cpd.hotel_system.hotel_management_service_api.dto.request.RequestHotelDto;
import com.cpd.hotel_system.hotel_management_service_api.dto.response.ResponseBranchDto;
import com.cpd.hotel_system.hotel_management_service_api.dto.response.ResponseHotelDto;
import com.cpd.hotel_system.hotel_management_service_api.dto.response.paginate.HotelPaginateResponseDto;
import com.cpd.hotel_system.hotel_management_service_api.entity.Branch;
import com.cpd.hotel_system.hotel_management_service_api.entity.Hotel;
import com.cpd.hotel_system.hotel_management_service_api.exceptions.EntryNotFoundException;
import com.cpd.hotel_system.hotel_management_service_api.repo.HotelRepo;
import com.cpd.hotel_system.hotel_management_service_api.service.HotelService;
import com.cpd.hotel_system.hotel_management_service_api.util.ByteCodeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepo hotelRepo;

    private final ByteCodeHandler byteCodeHandler;


    @Override
    public void create(RequestHotelDto dto)  {

        Hotel hotel= null;
        try {
            hotel = toHotel(dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        hotelRepo.save(hotel);
    }

    @Override
    public void update(RequestHotelDto dto, String hotelId) throws SQLException {
                Hotel selectedHotel=hotelRepo.findById(hotelId).orElseThrow(()->new EntryNotFoundException("Hotel not found"));
                selectedHotel.setHotelName(dto.getHotelName());
                selectedHotel.setStarRating(dto.getStarRating());
                selectedHotel.setDescription(byteCodeHandler.stringToBlob(dto.getDescription()));
                selectedHotel.setUpdatedAt(LocalDateTime.now());
                selectedHotel.setStartingForm(dto.getStartingForm());
                hotelRepo.save(selectedHotel);
    }

    @Override
    public void delete(String hotelId) {

        //first method
        Hotel selectedHotel=hotelRepo.findById(hotelId).orElseThrow(()->new EntryNotFoundException("Hotel not found"));
        hotelRepo.delete(selectedHotel);

        //second method
      /*  hotelRepo.findById(hotelId).orElseThrow(()->new EntryNotFoundException("Hotel not found"));
        hotelRepo.deleteById(hotelId);*/
    }

    @Override
    public ResponseHotelDto findById(String hotelId) throws SQLException {
        Hotel hotel=hotelRepo.findById(hotelId).orElseThrow(()->new EntryNotFoundException("Hotel not found"));
        return toResponseHotelDto(hotel);
    }

    @Override
    public HotelPaginateResponseDto findAll(int page, int size, String searchText) {

        return HotelPaginateResponseDto.builder()
                .dataCount(hotelRepo.countAllHotels(searchText))
                .dataList(
                        hotelRepo.searchAllHotels(searchText, PageRequest.of(page,size))
                                .stream().map(e ->{
                                    try {
                                        return toResponseHotelDto(e);
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }).collect(Collectors.toList())
                ).build();
    }

    private Hotel toHotel(RequestHotelDto dto) throws SQLException {
    return dto==null?null:
                Hotel.builder().
                        hotelId(UUID.randomUUID().toString())
                        .hotelName(dto.getHotelName())
                        .starRating(dto.getStarRating())
                        .description(byteCodeHandler.stringToBlob(dto.getDescription()))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .activeStatus(true)
                        .startingForm(dto.getStartingForm())
                        .build();
    }

    private ResponseHotelDto toResponseHotelDto(Hotel hotel) throws SQLException {
        return hotel==null?null:
                ResponseHotelDto.builder()
                        .hotelId(hotel.getHotelId())
                        .activeStatus(hotel.isActiveStatus())
                        .createdAt(hotel.getCreatedAt())
                        .updatedAt(hotel.getUpdatedAt())
                        .description(byteCodeHandler.blobToString(hotel.getDescription()))
                        .hotelName(hotel.getHotelName())
                        .starRating(hotel.getStarRating())
                        .startingForm(hotel.getStartingForm())
                        .branches(
                                hotel.getBranches().stream().map(e-> {
                                    try {
                                        return toResponseBranchDto(e);
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }).toList()
                        )
                        .build();
    }

    private ResponseBranchDto toResponseBranchDto(Branch branch) throws SQLException{
            return branch==null?null:
                    ResponseBranchDto.builder()
                            .branchId(branch.getBranchId())
                            .branchName(branch.getBranchName())
                            .branchType(branch.getBranchType())
                            .roomCount(branch.getRoomCount())
                            .build();
    }


}
