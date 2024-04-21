//package novi.backend.opdracht.backendservice.model;
//
//import jakarta.persistence.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table (name = "carts")
//public class Cart {
//    @Id
//    private Long userId;
//
//    @OneToOne
//    @MapsId
//    @JoinColumn(name = "userId")
//    private User0 user;
//
//    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CartItem> items = new ArrayList<>();
//
//    // Constructors
//
//    public Cart() {
//    }
//
//    public Cart(User0 user) {
//        this.user = user;
//    }
//
//    // Getters and Setters
//
//    public Long getId() {
//        return userId;
//    }
//
//    public void setId(Long id) {
//        this.userId = id;
//    }
//
//    public User0 getUser() {
//        return user;
//    }
//
//    public void setUser(User0 user) {
//        this.user = user;
//    }
//
//    public List<CartItem> getItems() {
//        return items;
//    }
//
//    public void setItems(List<CartItem> items) {
//        this.items = items;
//    }
//
//    // Helper Methods
//
//    public void addItem(CartItem item) {
//        items.add(item);
//        item.setCart(this);
//    }
//
//    public void removeItem(CartItem item) {
//        items.remove(item);
//        item.setCart(null);
//    }
//}
