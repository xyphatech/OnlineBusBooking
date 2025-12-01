package com.xypha.onlineBus.routes.Controller;


import com.xypha.onlineBus.routes.Dto.RouteRequest;
import com.xypha.onlineBus.routes.Dto.RouteResponse;
import com.xypha.onlineBus.routes.Mapper.RouteMapper;
import com.xypha.onlineBus.routes.Service.RouteServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/route")

public class RouteController {

    @Autowired
    private RouteServiceImpl routeService;

    @Autowired
    private RouteMapper routeMapper;



    @PostMapping
    public ResponseEntity<RouteResponse> addRoute(
            @Valid @RequestBody RouteRequest routeRequest){
            return ResponseEntity.ok(routeService.addRoute(routeRequest));
    }

    @GetMapping
    public ResponseEntity <List<RouteResponse>> getAllRoutes(){
        List<RouteResponse> routes = routeService.getAllRoute();
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/search")
    public ResponseEntity <Map<String, Object>> searchRoutes(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
    return ResponseEntity.ok(routeService.searchRoutes(source, destination, page, size));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoute(
            @PathVariable Long id
    ){
        routeService.deleteRoute(id);
        return ResponseEntity.ok("Route deleted successfully");
    }


    @PutMapping("/{id}")
    public ResponseEntity <RouteResponse>updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody RouteRequest routeRequest
    ){
        return ResponseEntity.ok(routeService.updateRoute(id, routeRequest));
    }


    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRouteById(
            @PathVariable Long id
    ){
        RouteResponse route = routeService.getRouteById(id);
        return ResponseEntity.ok(route);
    }


}
