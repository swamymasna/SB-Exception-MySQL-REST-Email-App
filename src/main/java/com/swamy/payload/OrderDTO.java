package com.swamy.payload;

import lombok.Data;

@Data
public class OrderDTO {

	private Integer id;
	private String name;
	private Double qty;
	private Double price;
}
