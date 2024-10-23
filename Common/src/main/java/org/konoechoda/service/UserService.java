package org.konoechoda.service;

import org.konoechoda.model.User;

//用户服务接口
public interface  UserService {

    /**
     * 获取用户信息
     * @param user 用户信息
     * @return 用户信息
     */
    User getUser(User user);
}
