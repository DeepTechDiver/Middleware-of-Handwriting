package com.example.mapper;

import com.example.pojo.User;

import java.util.List;

public interface UserMapper {
    List<User> list();

    User findById(Integer id);

    int update(User user);

    int delete(Integer id);
}
