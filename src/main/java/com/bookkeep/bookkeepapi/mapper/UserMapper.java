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

    @Update("UPDATE user SET name=#{name}, age=#{age}, occupation=#{occupation}, gender=#{gender} WHERE id=#{id}")
    int update(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") Long id);
}
