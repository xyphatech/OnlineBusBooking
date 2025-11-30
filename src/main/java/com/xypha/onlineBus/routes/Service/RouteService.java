package com.xypha.onlineBus.routes.Service;

import com.xypha.onlineBus.routes.Mapper.RouteMapperUtil;
import com.xypha.onlineBus.routes.Dto.RouteRequest;
import com.xypha.onlineBus.routes.Dto.RouteResponse;
import com.xypha.onlineBus.routes.Entity.Route;
import com.xypha.onlineBus.routes.Mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RouteService {

    @Autowired
    private RouteMapper routeMapper;

    public RouteResponse mapToResponse(Route route) {
        RouteResponse res = new RouteResponse();
        res.setId(route.getId());
        res.setSource(route.getSource());
        res.setPrice(route.getPrice());
        res.setDestination(route.getDestination());
        res.setDepartureTime(route.getDepartureTime());
        res.setArrivalTime(route.getArrivalTime());

        // Map bus-related fields if present
        res.setBusId(route.getBusId());
        res.setBusNumber(route.getBusNumber());
        res.setBusType(route.getBusType());
        res.setTotalSeats(route.getTotalSeats());
        res.setHasAC(route.getHasAC());
        res.setHasWifi(route.getHasWifi());

        return res;
    }

    public RouteResponse addRoute(RouteRequest routeRequest) {
        Route route = new Route();
        route.setSource(routeRequest.getSource());
        route.setDestination(routeRequest.getDestination());
        route.setPrice(routeRequest.getPrice());
        route.setDepartureTime(routeRequest.getDepartureTime());
        route.setArrivalTime(routeRequest.getArrivalTime());
        route.setBusId(routeRequest.getBusId());

        if (routeMapper.countDuplicateRoute(route) > 0) {
            throw new RuntimeException("This route already exists at the same time");
        }
        routeMapper.insertRoute(route);
        return mapToResponse(route);
    }

    public List<RouteResponse> getAllRoute() {
                return routeMapper.getAllRoute().stream()
                        .map(route -> mapToResponse(route))
                        .collect(Collectors.toList());
    }

    public RouteResponse getRouteById(Long id) {
        Route route = routeMapper.getRouteById(id);
        if (route == null)
            throw new RuntimeException("Route not found");
        return mapToResponse(route);
    }

    public RouteResponse updateRoute(Long id, RouteRequest request) {
        Route route = routeMapper.getRouteById(id);
        if (route == null)
            throw new RuntimeException("Route not found");

        route.setSource(request.getSource());
        route.setDestination(request.getDestination());
        route.setPrice(request.getPrice());
        route.setDepartureTime(request.getDepartureTime());
        route.setArrivalTime(request.getArrivalTime());
        route.setBusId(request.getBusId());

        routeMapper.updateRoute(route);
        return mapToResponse(route);
    }

    public void deleteRoute(Long id) {
        routeMapper.deleteRoute(id);
    }

    // Search method
    // public List<Route> searchRoutes(
    // String source, String destination,
    // int page, int size){
    // int offset = (page - 1) *size;
    // return routeMapper.searchRoutes(source,destination,size,offset);
    // }

    public Map<String, Object> searchRoutes(
            String source, String destination,
            int page, int size) {
        int offset = page * size;
        List<Route> routes = routeMapper.searchRoutes(source, destination, size, offset);
        int total = routeMapper.countSearchRoutes(source, destination);

        Map<String, Object> result = new HashMap<>();
        result.put("routes", RouteMapperUtil.toList(routes));
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;

    }

}
