package me.liuwenhan.service;

import me.liuwenhan.pojo.UserPassword;

/**
 * Created by Maibenben on 2017/4/27.
 */
public interface UserPasswordService {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPassword record);

    int insertSelective(UserPassword record);

    UserPassword selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPassword record);

    int updateByPrimaryKey(UserPassword record);

    UserPassword selectByUid(Integer uid);
}
