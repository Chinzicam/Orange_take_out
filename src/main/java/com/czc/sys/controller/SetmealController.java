package com.czc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.common.Result;
import com.czc.sys.dto.SetmealDto;
import com.czc.sys.entity.Category;
import com.czc.sys.entity.Setmeal;
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
        return Result.success("批量操作成功");
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

}
