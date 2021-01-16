package org.sid.billingService;

import org.sid.billingService.entities.Bill;
import org.sid.billingService.entities.ProductItem;
import org.sid.billingService.feign.CustomerServiceClient;
import org.sid.billingService.feign.InventoryServiceClient;
import org.sid.billingService.model.Customer;
import org.sid.billingService.model.Product;
import org.sid.billingService.repositories.BillRepository;
import org.sid.billingService.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @Bean
    CommandLineRunner start(BillRepository billRepository, ProductItemRepository productItemRepository, CustomerServiceClient customerServiceClient, InventoryServiceClient inventoryServiceClient, RepositoryRestConfiguration repositoryRestConfiguration)
    {
        repositoryRestConfiguration.exposeIdsFor(Bill.class);

        return args -> {
            Customer customer =  customerServiceClient.findCustomerById(1L);
            Bill bill =  billRepository.save(new Bill(null,new Date(),null,null,customer.getId()));
            PagedModel<Product> restProductItems = inventoryServiceClient.findAll();
            restProductItems.forEach(restProductItemData->{
                ProductItem productItem = new ProductItem();
                productItem.setId(null);
                productItem.setPrice(restProductItemData.getPrice());
                productItem.setQuantity(1+new Random().nextInt((int)restProductItemData.getQuantity()));
                productItem.setProductID(restProductItemData.getId());
                productItem.setBill(bill);
                productItemRepository.save(productItem);
            });


            Customer customer2 =  customerServiceClient.findCustomerById(2L);
            Bill bill2 =  billRepository.save(new Bill(null,new Date(),null,null,customer2.getId()));
            restProductItems.forEach(restProductItemData->{
                ProductItem productItem2 = new ProductItem();
                productItem2.setId(null);
                productItem2.setPrice(restProductItemData.getPrice());
                productItem2.setQuantity(1+new Random().nextInt((int)restProductItemData.getQuantity()));
                productItem2.setProductID(restProductItemData.getId());
                productItem2.setBill(bill2);
                productItemRepository.save(productItem2);
            });

        };
    }
}
