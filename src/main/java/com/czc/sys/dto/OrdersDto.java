package com.czc.sys.dto;

import com.czc.sys.entity.OrderDetail;
import com.czc.sys.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author czc
 */
@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
