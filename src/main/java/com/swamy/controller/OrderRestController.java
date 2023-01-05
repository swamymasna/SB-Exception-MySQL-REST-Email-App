package com.swamy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swamy.payload.OrderDTO;
import com.swamy.service.IOrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

	@Autowired
	private IOrderService orderService;

	@PostMapping("/save")
	public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTO orderDTO) {
		OrderDTO savedOrder = orderService.saveOrder(orderDTO);
		return new ResponseEntity<OrderDTO>(savedOrder, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable(value = "id") Integer orderId) {
		OrderDTO orderDTO = orderService.getOrderById(orderId);
		return new ResponseEntity<OrderDTO>(orderDTO, HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<List<OrderDTO>> getAllOrders() {
		List<OrderDTO> ordersList = orderService.getAllOrders();
		return ResponseEntity.ok(ordersList);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<OrderDTO> updateOrder(@PathVariable Integer id, @RequestBody OrderDTO orderDTO) {
		OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
		return new ResponseEntity<OrderDTO>(updatedOrder, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
		String deletedOrder = orderService.deleteOrder(id);
		return new ResponseEntity<>(deletedOrder, HttpStatus.OK);
	}
}
