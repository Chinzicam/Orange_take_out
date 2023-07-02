package com.czc.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czc.common.CustomException;
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

    @Override
    public void removeWithDish(List<Long> ids) {
        //查询并判断是否在售卖
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,1);
        int count = (int) this.count(wrapper);
        if(count>0){
            throw new CustomException("套餐正在售卖中，请先停售再进行删除");
        }
        //如果没有在售套餐，则直接删除
        this.removeByIds(ids);
        //继续删除SetmealDish表中的连接数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper);

    }
}
