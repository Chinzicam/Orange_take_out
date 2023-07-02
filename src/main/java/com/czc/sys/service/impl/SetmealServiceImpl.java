package com.czc.sys.service.impl;

import com.czc.sys.dto.SetmealDto;
import com.czc.sys.entity.Setmeal;
import com.czc.sys.entity.SetmealDish;
import com.czc.sys.mapper.SetmealMapper;
import com.czc.sys.service.ISetmealDishService;
import com.czc.sys.service.ISetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {

    @Autowired
    protected ISetmealDishService setmealDishService;
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map(item -> {
                item.setSetmealId(String.valueOf(setmealDto.getId()));
                return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}
