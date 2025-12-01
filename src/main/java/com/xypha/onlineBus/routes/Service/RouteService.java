package com.xypha.onlineBus.routes.Service;

import com.xypha.onlineBus.routes.Dto.RouteResponse;
import com.xypha.onlineBus.routes.Entity.Route;

import java.util.List;
import java.util.Map;

public interface RouteService {
    RouteResponse createRoute(RouteResponse request);

    RouteResponse getRouteById(Long id);

    RouteResponse updateRoute(Long id, RouteResponse request);

    List<RouteResponse> getAllRoutes();

    void deleteRoute(Long id);

    Map<String,Object> searchRoutes (String source, String destination, int page, int size);


}
