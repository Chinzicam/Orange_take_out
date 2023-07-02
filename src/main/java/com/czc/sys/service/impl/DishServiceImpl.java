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

    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //将dish的字段存进数据库
        this.save(dishDto);
        Long dishId = dishDto.getId();
        //将菜品口味存进数据库
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor : flavors) {
            dishFlavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);
    }
}
