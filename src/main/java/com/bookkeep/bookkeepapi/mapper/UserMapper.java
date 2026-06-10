package com.bookkeep.bookkeepapi.mapper;

import com.bookkeep.bookkeepapi.entity.User;
import org.apache.ibatis.annotations.*;
@Mapper
public interface UserMapper {

    // 根据用户名查询用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    // 插入新用户
    @Insert("INSERT INTO user(username, password, age) VALUES(#{username}, #{password}, #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}