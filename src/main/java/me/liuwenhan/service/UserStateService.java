package me.liuwenhan.service;

import me.liuwenhan.pojo.UserState;

/**
 * Created by wsk1103 on 2017/5/13.
 */
public interface UserStateService {
    int deleteByPrimaryKey(Integer id);

    int insert(UserState record);

    int insertSelective(UserState record);

    UserState selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserState record);

    int updateByPrimaryKey(UserState record);

    UserState selectByUid(int uid);
}
