package com.swamy.service;

import java.util.List;

import com.swamy.payload.OrderDTO;

public interface IOrderService {

	OrderDTO saveOrder(OrderDTO orderDTO);

	OrderDTO getOrderById(Integer id);

	List<OrderDTO> getAllOrders();

	OrderDTO updateOrder(Integer id, OrderDTO orderDTO);

	String deleteOrder(Integer id);

}
