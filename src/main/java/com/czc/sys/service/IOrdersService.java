package com.czc.sys.service;

import com.czc.sys.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
public interface IOrdersService extends IService<Orders> {

    void submit(Orders orders);
}
