package com.wust.myblog.service;

import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;

public interface IUserService {
    public User getUserById(Long id);

    Result login(User user);

    Result logout();

    Result register(User user);

    Result validate(User user);

    Result loginCheck(User user);

    Result active(String username, String token);

    Result forgetUser(String email);
}
