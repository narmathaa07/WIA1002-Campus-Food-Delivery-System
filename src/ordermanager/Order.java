/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ordermanager;
import java.util.Objects;
/**
 *
 * @author Harshini
 */
public class Order implements Comparable<Order> {
    private final int orderId;
    private final String studentName;
    private final String pickupLocation;
    private final String deliveryLocation;
    private final long orderTime; // Timestamp in milliseconds
    private final int priority; // 1 = Urgent, 2 = Normal (lower number = higher priority)
    private Status status;

    public Order(int orderId, String studentName, String pickupLocation, String deliveryLocation, int priority) {
        this.orderId = orderId;
        this.studentName = studentName;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.orderTime = System.currentTimeMillis(); // Auto-set to current time
        this.priority = priority;
        this.status = Status.PENDING; // Default
    }

    // Getters (no setters for immutable fields like ID/time)
    public int getOrderId() { return orderId; }
    public String getStudentName() { return studentName; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDeliveryLocation() { return deliveryLocation; }
    public long getOrderTime() { return orderTime; }
    public int getPriority() { return priority; }
    public Status getStatus() { return status; }

    // Setter for status (used by dispatch/complete/cancel)
    public void setStatus(Status status) { this.status = status; }

    @Override
    public int compareTo(Order other) {
        // First compare priority (smaller number first = higher priority)
        if (this.priority != other.priority) {
            return Integer.compare(this.priority, other.priority);
        }
        // Then compare time (earlier first)
        return Long.compare(this.orderTime, other.orderTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID=" + orderId +
                ", Student='" + studentName + '\'' +
                ", Pickup='" + pickupLocation + '\'' +
                ", Delivery='" + deliveryLocation + '\'' +
                ", Time=" + orderTime +
                ", Priority=" + priority +
                ", Status=" + status +
                '}';
    }
}
