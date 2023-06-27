package com.czc.sys.service.impl;

import com.czc.sys.entity.Orders;
import com.czc.sys.mapper.OrdersMapper;
import com.czc.sys.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

}
