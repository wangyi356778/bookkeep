package com.bookkeep.bookkeepapi.mapper;

import com.bookkeep.bookkeepapi.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User findByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO user(phone, password, name, age, occupation, gender) VALUES(#{phone}, #{password}, #{name}, #{age}, #{occupation}, #{gender})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}
