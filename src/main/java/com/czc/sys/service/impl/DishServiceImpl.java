package com.czc.sys.service.impl;

import com.czc.sys.dto.DishDto;
import com.czc.sys.entity.Dish;
import com.czc.sys.entity.DishFlavor;
import com.czc.sys.mapper.DishMapper;
import com.czc.sys.service.IDishFlavorService;
import com.czc.sys.service.IDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Autowired
    private IDishFlavorService dishFlavorService;

    /**
     * 将菜品数据和口味数据分别存进两张表，即dish表和dish_flavor表
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //将菜品数据保存到dish表
        this.save(dishDto);
        Long dishId = dishDto.getId();
        //将获取到的dishId赋值给dishFlavor的dishId属性
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor : flavors) {
            dishFlavor.setDishId(dishId);
        }
        //同时将菜品口味数据保存到dish_flavor表
        dishFlavorService.saveBatch(flavors);
    }


}
