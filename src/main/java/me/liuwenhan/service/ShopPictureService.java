package me.liuwenhan.service;

import me.liuwenhan.pojo.ShopPicture;

import java.util.List;

/**
 * Created by wsk1103 on 2017/5/12.
 */
public interface ShopPictureService {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopPicture record);

    int insertSelective(ShopPicture record);

    ShopPicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopPicture record);

    int updateByPrimaryKey(ShopPicture record);

    ShopPicture selectBySidOnlyOne(Integer sid);

    List<ShopPicture> selectBySid(Integer sid);
}
