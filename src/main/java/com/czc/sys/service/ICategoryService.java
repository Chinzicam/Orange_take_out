package com.czc.sys.service;

import com.czc.sys.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
public interface ICategoryService extends IService<Category> {
    public void remove(Long id);

}
