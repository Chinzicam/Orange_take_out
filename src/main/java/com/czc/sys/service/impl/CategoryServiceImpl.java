package com.czc.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czc.common.CustomException;
import com.czc.sys.entity.Category;
import com.czc.sys.entity.Dish;
import com.czc.sys.entity.Setmeal;
import com.czc.sys.mapper.CategoryMapper;
import com.czc.sys.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private  SetmealServiceImpl setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = (int) dishService.count(dishLambdaQueryWrapper);
        log.info("dish查询条件，查询到的条目数为：{}",count1);
        if (count1 > 0){
            //已关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = (int) setmealService.count(setmealLambdaQueryWrapper);
        log.info("setmeal，查询到的条目数为：{}",count1);
        if (count2 > 0){
            //已关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }


    }
}
