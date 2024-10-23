package org.konoechoda;

import org.konoechoda.model.User;
import org.konoechoda.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        return user;
    }
}
