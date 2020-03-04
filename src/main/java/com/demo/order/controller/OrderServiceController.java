package com.demo.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.order.dto.Coordinates;
import com.demo.order.entity.Order;
import com.demo.order.entity.OrderStatus;
import com.demo.order.exceptions.OrderException;
import com.demo.order.service.OrderService;

/**
 * REST Controller for managing Orders.
 * 
 * @author marlowelandicho
 *
 */
@RestController
@RequestMapping(path = "/orders")
@Validated
public class OrderServiceController {

	@Autowired
	private OrderService orderService;

	/**
	 * <p>
	 * Creates an Order given the Coordinates
	 * </p>
	 * 
	 * @param requestMap specifies the Coordinates
	 * @return the newly created Order
	 * 
	 */
	@PostMapping
	public Order createOrder(@RequestBody @NonNull Map<String, String[]> requestMap) {
		Coordinates startCoordinates = new Coordinates();
		Coordinates endCoordinates = new Coordinates();
		String[] startCoordinatesRaw = requestMap.get("origin");
		String[] endCoordinatesRaw = requestMap.get("destination");

		if (Objects.nonNull(startCoordinatesRaw) && startCoordinatesRaw.length == 2) {
			startCoordinates.setLatitude(startCoordinatesRaw[0]);
			startCoordinates.setLongitude(startCoordinatesRaw[1]);
		} else {
			throw new OrderException("Invalid number of parameters. ORIGIN coordinates missing.");
		}

		if (Objects.nonNull(endCoordinatesRaw) && endCoordinatesRaw.length == 2) {
			endCoordinates.setLatitude(endCoordinatesRaw[0]);
			endCoordinates.setLongitude(endCoordinatesRaw[1]);
		} else {
			throw new OrderException("Invalid number of parameters. DESTINATION coordinates missing.");
		}

		return orderService.createOrder(startCoordinates, endCoordinates);
	}

	/**
	 * <p>
	 * Takes the Order given an id
	 * </p>
	 * 
	 * @param id the Order id from the database
	 * 
	 * @return status of updating the Order
	 * 
	 */
	@PatchMapping("/{id}")
	public Map<String, String> takeOrder(@PathVariable @NonNull String id,
			@RequestBody @NonNull Map<String, String> requestMap) {

		Map<String, String> responseMap = new HashMap<>();
		String requestStatus = requestMap.get("status");

		if (Objects.nonNull(requestStatus) && Objects.nonNull(OrderStatus.valueOf(requestStatus))) {

			OrderStatus orderStatus = orderService.takeOrder(id);
			responseMap.put("status", orderStatus.name());

			return responseMap;
		} else {
			throw new OrderException("Invalid Request!");
		}
	}

	/**
	 * <p>
	 * Lists all Order given pagination
	 * </p>
	 * 
	 * @param page  the page number
	 * @param limit size of each page
	 * 
	 * @return list of Orders
	 * 
	 */
	@GetMapping
	public List<Order> listOrders(@Min(1) @NotNull @RequestParam int page, @Min(1) @NotNull @RequestParam int limit) {

		return orderService.listOrders(page, limit);

	}
}
