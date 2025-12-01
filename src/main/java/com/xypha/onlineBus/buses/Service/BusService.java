package com.xypha.onlineBus.buses.Service;

import com.xypha.onlineBus.buses.Dto.BusRequest;
import com.xypha.onlineBus.buses.Dto.BusResponse;
import com.xypha.onlineBus.buses.Entity.Bus;
import com.xypha.onlineBus.routes.Dto.RouteResponse;

import java.util.List;

public interface BusService {

    //Pagination Part
    List<BusResponse> getBusPaginated(int page, int size);

    int countBuses();

    //CRUD
    BusResponse addBus(BusRequest busRequest);

    List<BusResponse> getAllBuses();

    BusResponse getBusById(Long id);

    BusResponse updateBus(Long id, BusRequest busRequest);

    void deleteBus(Long id);


}
