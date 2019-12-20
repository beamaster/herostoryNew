package com.steam.game.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class UserManager {

    /**
     * 用户字典
     */
    private static final Map<Integer, UserEntity> _userMap = new HashMap();

    private UserManager() {
    }

    /**
     * 添加用户
     * @param currentUser
     */
    public static void addUser(UserEntity currentUser) {
        if (null != currentUser) {
            _userMap.put(currentUser.getUserId(), currentUser);
        }
    }

    /**
     * 移除用户
     * @param userId
     */
    public static void removeUser(int userId) {
        _userMap.remove(userId);
    }

    /**
     * 用户列表
     * @return
     */
    public static Collection<UserEntity> listUser() {
        return _userMap.values();
    }
}
