package com.xypha.OnlineBus.Routes.Mapper;

import com.xypha.OnlineBus.Routes.Dto.RouteResponse;
import com.xypha.OnlineBus.Routes.Entity.Route;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RouteMapper {

    @Insert("INSERT INTO route (source,destination,price,departure_time,arrival_time, bus_id)"+
    "VALUES(#{source}, #{destination}, #{price}, #{departureTime}, #{arrivalTime}, #{busId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertRoute(Route route);

    @Select("SELECT r.id AS route_id, r.source, r.destination, r.price, r.departure_time, r.arrival_time, " +
            "b.id AS bus_id, b.bus_number, b.bus_type, b.total_seats, b.has_ac, b.has_wifi " +
            "FROM route r " +
            "LEFT JOIN bus b ON r.bus_id = b.id " +
            "ORDER BY r.id DESC")
    @Results({
            @Result(property = "id", column = "route_id"),
            @Result(property = "source", column = "source"),
            @Result(property = "destination", column = "destination"),
            @Result(property = "price", column = "price"),
            @Result(property = "departureTime", column = "departure_time"),
            @Result(property = "arrivalTime", column = "arrival_time"),

            // Bus fields
            @Result(property = "busId", column = "bus_id"),
            @Result(property = "busNumber", column = "bus_number"),
            @Result(property = "busType", column = "bus_type"),
            @Result(property = "totalSeats", column = "total_seats"),
            @Result(property = "hasAC", column = "has_ac"),
            @Result(property = "hasWifi", column = "has_wifi")
    })
    List<RouteResponse> getAllRoute();

    @Select("SELECT r.id, r.source, r.destination, r.price, r.departure_time, r.arrival_time, " +
            "b.id AS bus_id,b.bus_number, b.bus_type, b.total_seats, b.has_ac, b.has_wifi " +
            "FROM route r " +
            "LEFT JOIN bus b ON r.bus_id = b.id " +
            "WHERE r.id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "source", column = "source"),
            @Result(property = "destination", column = "destination"),
            @Result(property = "price", column = "price"),
            @Result(property = "departureTime", column = "departure_time"),
            @Result(property = "arrivalTime", column = "arrival_time"),
            @Result(property = "busId", column = "bus_id"),
            @Result(property = "busNumber", column = "bus_number"),
            @Result(property = "busType", column = "bus_type"),
            @Result(property = "totalSeats", column = "total_seats"),
            @Result(property = "hasAC", column = "has_ac"),
            @Result(property = "hasWifi", column = "has_wifi"),
    })
    Route getRouteById(Long id);

    @Update("UPDATE route SET source=#{source}, destination=#{destination}, departure_time=#{departureTime}, arrival_time=#{arrivalTime}, price=#{price}, bus_id=#{busId} WHERE id=#{id}")
    void updateRoute(Route route);

    @Delete("DELETE FROM route WHERE id = #{id}")
    void deleteRoute(Long id);

    @Select("SELECT COUNT(*) FROM route WHERE source=#{source} AND destination=#{destination} " +
            "AND departure_time=#{departureTime} AND arrival_time=#{arrivalTime}")
    int countDuplicateRoute(Route route);



    //Search Method
    @Select("<script>"
            + "SELECT * FROM route WHERE 1=1 "
            + "<if test='source != null and source != \"\"'> AND source LIKE CONCAT('%', #{source}, '%') </if>"
            + "<if test='destination != null and destination != \"\"'> AND destination LIKE CONCAT('%', #{destination}, '%') </if>"
            + " LIMIT #{limit} OFFSET #{offset} "
            + "</script>")
    int countSearchRoutes(
            @Param("source") String source,
            @Param("destination") String destination
    );

    List<Route> searchRoutes(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("limit") int limit,
            @Param("offset") int offset
    );



    //A driver or assistant cannot be assigned to more than one bus on the same day.
    //
    //A bus cannot be assigned to more than one route on the same day.
    //
    //A driver or assistant cannot be assigned to more than one route on the same day.


    @Select("SELECT COUNT (*) FROM route WHERE bus_id = #{busId} AND DATE(departure_time) = #{date} ")
    int countBusAssignmentsForDate(
            @Param("busId") Long busId,
            @Param("date") LocalDate date);

    @Select("SELECT COUNT (*) FROM route WHERE driver_id = #{driverId} AND DATE(departure_time) = #{date} ")
    int countDriverAssignmentsForDate(
            @Param("driverId") Long driverId,
            @Param("date") LocalDate date);

    @Select("SELECT COUNT (*) FROM route WHERE assistant_id = #{assistantId} AND DATE(departure_time) = #{date} ")
    int countAssistantAssignmentsForDate(
            @Param("assistantId") Long assistantId,
            @Param("date") LocalDate date);


}
