package com.swamy.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swamy.entity.Order;
import com.swamy.exception.ResourceNotFoundException;
import com.swamy.payload.OrderDTO;
import com.swamy.repository.OrderRepository;
import com.swamy.service.IOrderService;
import com.swamy.utils.AppConstants;
import com.swamy.utils.EmailUtil;

@Service
public class OrderServiceImpl implements IOrderService {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public OrderDTO saveOrder(OrderDTO orderDTO) {

		Order order = modelMapper.map(orderDTO, Order.class);
		Order savedOrder = orderRepository.save(order);
		OrderDTO orderResponse = modelMapper.map(savedOrder, OrderDTO.class);
		String body = readMailBody(AppConstants.EMAIL_BODY_TEMPLATE, orderResponse);
		String sentEmail = emailUtil.sendEmail(AppConstants.SUBJECT, body, AppConstants.RECIPIENT);
		LOGGER.info(sentEmail);

		System.out.println(sentEmail);

		return orderResponse;
	}

	@Override
	public OrderDTO getOrderById(Integer id) {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXCEPTION_MSG + id));
		OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

		return orderDTO;
	}

	@Override
	public List<OrderDTO> getAllOrders() {
		List<OrderDTO> list = new ArrayList<>();
		List<Order> ordersList = orderRepository.findAll();

		ordersList.forEach(order -> {
			OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
			list.add(orderDTO);
		});

		return list;
	}

	@Override
	public OrderDTO updateOrder(Integer id, OrderDTO orderDTO) {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXCEPTION_MSG + id));

		order.setName(orderDTO.getName());
		order.setQty(orderDTO.getQty());
		order.setPrice(orderDTO.getPrice());

		Order updatedOrder = orderRepository.save(order);
		OrderDTO orderResponse = modelMapper.map(updatedOrder, OrderDTO.class);
		return orderResponse;
	}

	@Override
	public String deleteOrder(Integer id) {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXCEPTION_MSG + id));

		orderRepository.delete(order);
		return AppConstants.DELETE_SUCCESS + id;
	}

	public String readMailBody(String fileName, OrderDTO orderDTO) {
		String mailBody = null;
		StringBuffer buffer = new StringBuffer();
		Path filePath = Paths.get(fileName);

		// try-with-resources
		try (Stream<String> lines = Files.lines(filePath)) {
			lines.forEach(line -> {
				buffer.append(line);
				buffer.append("\n");
			});

			mailBody = buffer.toString();
			mailBody = mailBody.replace(AppConstants.USER, AppConstants.SENDER);
			mailBody = mailBody.replace(AppConstants.ORDER_ID, orderDTO.getId().toString());
			mailBody = mailBody.replace(AppConstants.ORDER_NAME, orderDTO.getName());
			mailBody = mailBody.replace(AppConstants.QTY, orderDTO.getQty().toString());
			mailBody = mailBody.replace(AppConstants.PRICE, orderDTO.getPrice().toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return mailBody;
	}
}
