package com.xypha.onlineBus.restPassword.mapper;


import com.xypha.onlineBus.restPassword.entity.RestToken;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResetTokenMapper {

    @Insert("INSERT INTO reset_tokens (token, user_id, expiry_date) VALUES (#{token}, #{userId},  #{expiryDate}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertToken (RestToken token);

    @Select("SELECT * FROM reset_tokens WHERE token = #{token}")
    @Results({
            @Result (property = "id", column = "id"),
            @Result (property = "token", column = "token"),
            @Result (property = "userId", column = "user_id"),
            @Result (property = "expiryDate", column = "expiry_date")
    })
    RestToken findByToken (@Param("token") String token);

    @Delete("DELETE FROM reset_tokens WHERE token = #{token}")
    void deleteByToken (@Param("token") String token);

    @Select("SELECT * FROM reset_tokens")
    @Results({
            @Result (property = "id", column = "id"),
            @Result (property = "token", column = "token"),
            @Result (property = "userId", column = "user_id"),
            @Result (property = "expiryDate", column = "expiry_date")
    })
    List<RestToken> getAllRestTokens();

}
