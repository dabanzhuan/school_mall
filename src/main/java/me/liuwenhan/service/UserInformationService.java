package me.liuwenhan.service;

import me.liuwenhan.pojo.UserInformation;

import java.util.List;

/**
 * Created by Maibenben on 2017/4/26.
 */
public interface UserInformationService {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInformation record);

    int insertSelective(UserInformation record);

    UserInformation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInformation record);

    int updateByPrimaryKey(UserInformation record);

    int selectIdByPhone(String phone);

    List<UserInformation> getAllForeach(List<Integer> list);
}
