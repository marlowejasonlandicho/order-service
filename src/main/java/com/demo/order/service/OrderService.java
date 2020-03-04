package com.demo.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.order.dto.Coordinates;
import com.demo.order.dto.Distance;
import com.demo.order.dto.DistanceMatrixApiResponse;
import com.demo.order.dto.DistanceMatrixElement;
import com.demo.order.dto.DistanceMatrixRow;
import com.demo.order.entity.Order;
import com.demo.order.entity.OrderStatus;
import com.demo.order.exceptions.OrderException;
import com.demo.order.repository.OrderRepository;

/**
 * Service for invoking Google Maps API for fetching distance given start and
 * origin
 * 
 * @author marlowelandicho
 *
 */
@Service
public class OrderService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OrderRepository orderRepository;

	@Value("${google.maps.url}")
	private String mapsUrl;

	@Value("${google.maps.client.api.key}")
	private String mapsClientApiKey;

	/**
	 * 
	 * <p>
	 * Calls the Google Distance Matrix API to retrieve the distance based on start
	 * and end coordinates. Save the Order based on distance.
	 * </p>
	 * 
	 * @param start the start Coordinate
	 * @param end   the ending Coordinate
	 * 
	 * @return newly created Order
	 */
	public Order createOrder(Coordinates start, Coordinates end) {

		ResponseEntity<DistanceMatrixApiResponse> matrixApiResponse = restTemplate.exchange(mapsUrl, HttpMethod.GET,
				getHttpEntity(), DistanceMatrixApiResponse.class, start.getLatitude() + "," + start.getLongitude(),
				end.getLatitude() + "," + end.getLongitude(), mapsClientApiKey);

		DistanceMatrixRow[] matrixRows = matrixApiResponse.getBody().getRows();

		Order order = new Order();
		Long distance = getDistance(matrixRows);
		if (Objects.nonNull(distance)) {
			order.setDistance(distance);
			orderRepository.save(order);
		} else {
			order.setDistance(0L);
		}

		return order;
	}

	/**
	 * 
	 * <p>
	 * Takes the Order given an id
	 * </p>
	 * 
	 * @param id the Order id from the database
	 * 
	 * @return status of updating the Order
	 */
	public OrderStatus takeOrder(String id) {
		Order order = orderRepository.findById(Long.valueOf(id)).orElse(null);
		if (Objects.nonNull(order)) {
			order.setStatus(OrderStatus.SUCCESS.name());
			orderRepository.save(order);
			return OrderStatus.SUCCESS;
		} else {
			throw new OrderException("Order not found.");
		}
	}

	/**
	 * 
	 * <p>
	 * Lists all Order given pagination
	 * </p>
	 * 
	 * @param pageNum the page number
	 * @param limit   size of each page
	 * 
	 * @return list of Orders
	 * 
	 */
	public List<Order> listOrders(int pageNum, int limit) {
		List<Order> orderList = new ArrayList<Order>();

		pageNum -= pageNum;
		
		if (pageNum < 0) {
			pageNum = 0;
		}

		Pageable pageable = PageRequest.of(pageNum, limit, Direction.ASC, "id");

		Page<Order> page = orderRepository.findAll(pageable);

		orderList.addAll(page.getContent());

		return orderList;
	}

	/**
	 * 
	 * <p>
	 * Parses the distance value from Google Maps API Response
	 * </p>
	 * 
	 * @return the distance in meters
	 */
	private Long getDistance(DistanceMatrixRow[] matrixRows) {
		if (Objects.nonNull(matrixRows)) {
			for (int i = 0; i < matrixRows.length; i++) {
				DistanceMatrixElement[] matrixElements = matrixRows[i].getElements();
				if (Objects.nonNull(matrixElements)) {
					for (int j = 0; j < matrixElements.length; j++) {
						Distance distance = matrixElements[j].getDistance();
						if (Objects.nonNull(distance)) {
							return Long.valueOf(distance.getValue());
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @return an instance of HttpEntity
	 */
	private HttpEntity<String> getHttpEntity() {
		HttpHeaders headers = new HttpHeaders();
		return new HttpEntity<>(headers);
	}
}
