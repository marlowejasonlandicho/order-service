package com.demo.order.exceptions;

import java.util.Date;

/**
 * Error Details for REST Clients.
 * 
 * @author marlowelandicho
 *
 */
public class ErrorDetails {

  private Date timestamp;
  private String message;
  private String details;

  /**
   * Error Detail instance representing error messages returned by the application.
   * 
   * @param timestamp timestamp when the error occurred
   * @param message   message passed from the error
   * @param details   contains the details of the error
   */
  public ErrorDetails(Date timestamp, String message, String details) {
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

}