package novi.backend.opdracht.backendservice.model;

public enum OrderStatus {
    PENDING,
    WAITING_FOR_SHIPMENT,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELED
}