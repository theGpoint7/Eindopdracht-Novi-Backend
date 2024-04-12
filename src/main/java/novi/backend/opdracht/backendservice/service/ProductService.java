package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.ProductOutputDto;
import novi.backend.opdracht.backendservice.model.Product;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // method to obtain list of all product
    public List<ProductOutputDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductOutputDto> productOutputDtos = new ArrayList<>();
        for (Product product : products) {
            productOutputDtos.add(transferProductToOutputDto(product));
        }
        return productOutputDtos;
    }

    // method to obtain a single product by its ID
    public ProductOutputDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return transferProductToOutputDto(product);
        }
        return null;
    }

    // method to create a new product
    public ProductOutputDto createProduct(ProductInputDto productInputDto) {
        Product product = transferInputDtoToProduct(productInputDto);
        Product savedProduct = productRepository.save(product);
        return transferProductToOutputDto(savedProduct);
    }

    // reusable method for handling outgoing products
    public ProductOutputDto transferProductToOutputDto(Product product) {
        ProductOutputDto productOutputDto = new ProductOutputDto();

        productOutputDto.name = product.getName();
        productOutputDto.description = product.getDescription();
        productOutputDto.price = product.getPrice();
        productOutputDto.inventoryCount = product.getInventoryCount();
        productOutputDto.imageUrl = product.getImageUrl();

        return productOutputDto;
    }

    // reusable method for handling incoming products
    public Product transferInputDtoToProduct(ProductInputDto productInputDto) {
        Product product = new Product();
        product.setName(productInputDto.name);
        product.setDescription(productInputDto.description);
        product.setPrice(productInputDto.price);
        product.setInventoryCount(productInputDto.inventoryCount);
        product.setImageUrl(productInputDto.imageUrl);
        return product;
    }
}
