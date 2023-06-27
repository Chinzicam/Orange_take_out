package com.czc.sys.service.impl;

import com.czc.sys.entity.OrderDetail;
import com.czc.sys.mapper.OrderDetailMapper;
import com.czc.sys.service.IOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author czc
 * @since 2023-06-28
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
