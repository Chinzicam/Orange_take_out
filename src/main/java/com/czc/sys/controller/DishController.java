package com.czc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.common.CustomException;
import com.czc.common.Result;
import com.czc.sys.dto.DishDto;
import com.czc.sys.entity.Category;
import com.czc.sys.entity.Dish;
import com.czc.sys.entity.DishFlavor;
import com.czc.sys.service.ICategoryService;
import com.czc.sys.service.IDishFlavorService;
import com.czc.sys.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private IDishService dishService;
    @Autowired
    private IDishFlavorService dishFlavorService;
    @Autowired
    private ICategoryService categoryService;

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        //这个就是我们到时候返回的结果
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝，这里只需要拷贝一下查询到的条目数
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        //获取原records数据
        List<Dish> records = pageInfo.getRecords();

        //遍历每一条records数据
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //将数据赋给dishDto对象
            BeanUtils.copyProperties(item, dishDto);
            //然后获取一下dish对象的category_id属性
            Long categoryId = item.getCategoryId();  //分类id
            //根据这个属性，获取到Category对象
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                //随后获取Category对象的name属性，也就是菜品分类名称
                String categoryName = category.getName();
                //最后将菜品分类名称赋给dishDto对象就好了
                dishDto.setCategoryName(categoryName);
            }
            //结果返回一个dishDto对象
            return dishDto;
            //并将dishDto对象封装成一个集合，作为我们的最终结果
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return Result.success(dishDtoPage);
    }

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return Result.success("新增菜品成功");
    }

    /**
     * 根据id获取编辑页的口味信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Result<DishDto> get( @PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }

    /**
     * 编辑菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return Result.success("修改菜品成功");
    }

    /**
     * 批量更新菜品状态
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> status(@PathVariable Integer status,@RequestParam List<Long> ids){
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(ids != null, Dish::getId, ids);
        wrapper.set(Dish::getStatus, status);
        dishService.update(wrapper);
        return Result.success("批量操作成功");
    }

    /**
     * 批量删除商品
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Dish::getId, ids);
        wrapper.eq(Dish::getStatus, 1);
        //对商品进行判断
        int count = (int) dishService.count(wrapper);
        if (count > 0) {
            throw new CustomException("删除列表中存在启售状态商品，无法删除"+count);
        }
        dishService.removeByIds(ids);
        return Result.success("批量删除成功");
    }

    /**
     * 套餐管理 添加菜品 功能
     * 根据全部菜品，获取在售菜品
     * 创建一个dishDto对象，将在售菜品copy
     * 根据在售菜品的categoryId，获取菜品名，传值
     *根据在售菜品的id，获取口味，传值
     * 最后封装成List返回
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDto>> get(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //根据传进来的categoryId查询
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //只查询状态为1的菜品（在售菜品）
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        //获取查询到的结果作为返回值
        List<Dish> list = dishService.list(queryWrapper);
        log.info("查询到的菜品信息list:{}", list);
        //item就是list中的每一条数据，相当于遍历了
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            //创建一个dishDto对象
            DishDto dishDto = new DishDto();
            //将item的属性全都copy到dishDto里
            BeanUtils.copyProperties(item, dishDto);
            //由于dish表中没有categoryName属性，只存了categoryId
            Long categoryId = item.getCategoryId();
            //所以我们要根据categoryId查询对应的category
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //然后取出categoryName，赋值给dishDto
                dishDto.setCategoryName(category.getName());
            }
            //然后获取一下菜品id，根据菜品id去dishFlavor表中查询对应的口味，并赋值给dishDto
            Long itemId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //条件就是菜品id
            lambdaQueryWrapper.eq(itemId != null, DishFlavor::getDishId, itemId);
            //根据菜品id，查询到菜品口味
            List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
            //赋给dishDto的对应属性
            dishDto.setFlavors(flavors);
            //并将dishDto作为结果返回
            return dishDto;
            //将所有返回结果收集起来，封装成List
        }).collect(Collectors.toList());
        return Result.success(dishDtoList);
    }

}
