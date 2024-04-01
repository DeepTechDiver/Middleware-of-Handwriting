package com.example.mapper;

import com.example.pojo.User;

import java.util.List;

public interface UserMapper {
    List<User> list();

    User findById(Integer id);

    Integer update(User user);

    Integer insert(User user);

    Integer delete(Integer id);
}
