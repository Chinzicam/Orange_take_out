package com.czc.sys.service;

import com.czc.sys.dto.DishDto;
import com.czc.sys.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
public interface IDishService extends IService<Dish> {

    /**
     * 保存菜品和口味
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);
}
