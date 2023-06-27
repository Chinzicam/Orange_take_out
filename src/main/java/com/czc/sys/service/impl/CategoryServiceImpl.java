package com.czc.sys.service.impl;

import com.czc.sys.entity.Category;
import com.czc.sys.mapper.CategoryMapper;
import com.czc.sys.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
