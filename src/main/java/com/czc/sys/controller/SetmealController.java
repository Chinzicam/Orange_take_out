package com.czc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.common.CustomException;
import com.czc.common.Result;
import com.czc.sys.dto.SetmealDto;
import com.czc.sys.entity.Category;
import com.czc.sys.entity.Setmeal;
import com.czc.sys.entity.SetmealDish;
import com.czc.sys.service.ICategoryService;
import com.czc.sys.service.ISetmealDishService;
import com.czc.sys.service.ISetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private ISetmealService setmealService;
    @Autowired
    private ISetmealDishService setmealDishService;
    @Autowired
    private ICategoryService categoryService;

    /**
     * 新增套餐，及菜品处理
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return Result.success("新增套餐成功");
    }

    /**
     * 页面显示
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);
        //将查询后的pageInfo,copy到dtoPage
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        //将pageInfo的数据列表赋值给records
        //根据id，去records里获取CategoryName返回到setmealDto，并传给list
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        //将list数据列表设置到dtoPage对象中
        dtoPage.setRecords(list);
        return Result.success(dtoPage);
    }

    /**
     * 更新套餐售卖状态
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("status/{status}")
    public Result<String> status(@PathVariable Integer status, @RequestParam String ids) {
        LambdaUpdateWrapper<Setmeal> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Setmeal::getId, ids);
        wrapper.set(Setmeal::getStatus, status);
        setmealService.update(wrapper);
        return Result.success("操作成功");
    }

    /**
     * 删除套餐，需判断是否售卖
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam  List<Long> ids){
        setmealService.removeWithDish(ids);
        return Result.success("删除成功");
    }

    /**
     * 套餐修改菜品显示
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> getById(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        if (setmeal == null) {
            throw new CustomException("套餐信息不存在，请刷新重试");
        }
        SetmealDto setmealDto = new SetmealDto();
        //拷贝数据
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        //根据setmealId查询具体的setmealDish
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        //然后再设置属性
        setmealDto.setSetmealDishes(setmealDishes);
        return Result.success(setmealDto);
    }

    /**
     * 编辑套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public Result<Setmeal> updateWithDish(@RequestBody SetmealDto setmealDto) {
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long setmealId=setmealDto.getId();
        //先根据id把setmealDish表中对应套餐的数据删了
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealId);
        setmealDishService.remove(wrapper);
        //然后在重新添加
        setmealDishes = setmealDishes.stream().map((item) ->{
            item.setSetmealId(String.valueOf(setmealId));
            return item;
        }).collect(Collectors.toList());
        //更新套餐数据
        setmealService.updateById(setmealDto);
        //更新套餐对应菜品数据
        setmealDishService.saveBatch(setmealDishes);
        return Result.success(setmealDto);
    }

    /**
     * 菜品分类展示套餐信息
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        wrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);
        return Result.success(list);
    }

}
