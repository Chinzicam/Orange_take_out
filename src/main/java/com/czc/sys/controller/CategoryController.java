package com.czc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.common.Result;
import com.czc.sys.entity.Category;
import com.czc.sys.entity.Employee;
import com.czc.sys.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    /**
     *获取分类管理数据
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件查询器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        //排序
        wrapper.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody Category category){
        log.info("category:{}", category);
        categoryService.save(category);
        return Result.success("新增分类成功");
    }

    /**
     * 删除分类菜品
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> delete(Long id){
        log.info("将被删除的id：{}", id);
        categoryService.removeById(id);
        return Result.success("分类信息删除成功");
    }

    /**
     * 编辑分类
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        log.info("修改分类信息为：{}", category);
        categoryService.updateById(category);
        return Result.success("修改分类信息成功");
    }



}
