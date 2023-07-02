package com.czc.sys.service;

import com.czc.sys.dto.SetmealDto;
import com.czc.sys.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
public interface ISetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);
}
