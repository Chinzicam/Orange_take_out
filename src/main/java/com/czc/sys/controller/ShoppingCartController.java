package com.czc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czc.common.BaseContext;
import com.czc.common.Result;
import com.czc.sys.entity.ShoppingCart;
import com.czc.sys.service.ICategoryService;
import com.czc.sys.service.IShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    /**
     * 展示已经在购物车里的菜品
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> shoppingList() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list();
        log.info("shoppingList={}", list);
        return Result.success(list);
    }

    /**
     * 添加菜品进入购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("shoppingCart={}", shoppingCart);
        //获取当前用户id
        Long currentId = BaseContext.getCurrentId();
        //设置当前用户id
        shoppingCart.setUserId(currentId);
        //获取当前菜品id
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //判断添加的是菜品还是套餐
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //查询当前菜品或者套餐是否在购物车中
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null) {
            //如果已存在就在当前的数量上加1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //如果不存在，则设置一下创建时间
            shoppingCart.setCreateTime(LocalDateTime.now());
            //如果不存在，则添加到购物车，其中数量默认为1
            shoppingCartService.save(shoppingCart);
            //为了统一结果，最后都返回cartServiceOne
            cartServiceOne = shoppingCart;
        }
        return Result.success(cartServiceOne);
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public Result<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //获取当前userId，查询当前用户ID的购物车并清空
        Long userId = BaseContext.getCurrentId();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(queryWrapper);
        return Result.success("成功清空购物车");
    }

    /**
     * 减少商品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        //只查询当前用户ID的购物车
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        //代表数量减少的是菜品数量
        if (dishId != null) {
            //通过dishId查出购物车菜品数据
            wrapper.eq(ShoppingCart::getDishId, dishId);
            //将查出来的数据的数量-1
            ShoppingCart dishCart = shoppingCartService.getOne(wrapper);
            //然后判断
            dishCart.setNumber(dishCart.getNumber() - 1);
            //大于0则更新
            if (dishCart.getNumber() > 0) {
                shoppingCartService.updateById(dishCart);
            } else if (dishCart.getNumber() == 0) {
                //小于0则删除
                shoppingCartService.removeById(dishCart.getId());
            }
            return Result.success(dishCart);
        }

        if (setmealId != null) {
            //通过setmealId查询购物车套餐数据
            wrapper.eq(ShoppingCart::getSetmealId, setmealId);
            //将查出来的数据的数量-1
            ShoppingCart setmealCart = shoppingCartService.getOne(wrapper);
            setmealCart.setNumber(setmealCart.getNumber() - 1);
            //然后判断
            if (setmealCart.getNumber() > 0) {
                //大于0则更新
                shoppingCartService.updateById(setmealCart);
            } else if (setmealCart.getNumber() == 0){
                //等于0则删除
                shoppingCartService.removeById(setmealCart.getId());
            }
            return Result.success(setmealCart);
        }
        return Result.error("操作异常");
    }
}
