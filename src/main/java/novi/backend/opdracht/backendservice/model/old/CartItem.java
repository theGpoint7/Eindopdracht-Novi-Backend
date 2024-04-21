//package novi.backend.opdracht.backendservice.model;
//
//import jakarta.persistence.*;
//
//@Entity
//public class CartItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cart_id")
//    private Cart cart;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Product product;
//
//    private int quantity;
//
//    // Constructors
//
//    public CartItem() {
//    }
//
//    public CartItem(Cart cart, Product product, int quantity) {
//        this.cart = cart;
//        this.product = product;
//        this.quantity = quantity;
//    }
//
//    // Getters and Setters
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Cart getCart() {
//        return cart;
//    }
//
//    public void setCart(Cart cart) {
//        this.cart = cart;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//}
