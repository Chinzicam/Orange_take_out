package com.czc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.common.Result;
import com.czc.sys.entity.Dish;
import com.czc.sys.entity.Employee;
import com.czc.sys.service.IDishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜品口味关系表 前端控制器
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@RestController
@Slf4j
@RequestMapping("/dishFlavor")
public class DishFlavorController {

}
