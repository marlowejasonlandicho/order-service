package com.demo.order.dto;

public class DistanceMatrixApiResponse {

	private String status;

	private String errorMessage;

	private String[] origin_addresses;

	private String[] destination_addresses;

	private DistanceMatrixRow[] rows;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String[] getOrigin_addresses() {
		return origin_addresses;
	}

	public void setOrigin_addresses(String[] origin_addresses) {
		this.origin_addresses = origin_addresses;
	}

	public String[] getDestination_addresses() {
		return destination_addresses;
	}

	public void setDestination_addresses(String[] destination_addresses) {
		this.destination_addresses = destination_addresses;
	}

	public DistanceMatrixRow[] getRows() {
		return rows;
	}

	public void setRows(DistanceMatrixRow[] rows) {
		this.rows = rows;
	}

}
