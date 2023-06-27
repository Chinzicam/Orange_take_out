package com.czc.sys.service.impl;

import com.czc.sys.entity.Dish;
import com.czc.sys.mapper.DishMapper;
import com.czc.sys.service.IDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

}
