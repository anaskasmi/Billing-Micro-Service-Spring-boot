package org.sid.billingService.web;

import lombok.AllArgsConstructor;
import org.sid.billingService.entities.Bill;
import org.sid.billingService.feign.CustomerServiceClient;
import org.sid.billingService.feign.InventoryServiceClient;
import org.sid.billingService.model.Product;
import org.sid.billingService.repositories.BillRepository;
import org.sid.billingService.repositories.ProductItemRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin("*")
@RestController
public class BillingRestController {
     private BillRepository billRepository;
     private ProductItemRepository productItemRepository;
     private CustomerServiceClient customerServiceClient;
     private InventoryServiceClient inventoryServiceClient;

     public BillingRestController(BillRepository billRepository,ProductItemRepository productItemRepository,CustomerServiceClient customerServiceClient,InventoryServiceClient inventoryServiceClient)
     {
         this.billRepository = billRepository;
         this.productItemRepository = productItemRepository;
         this.customerServiceClient = customerServiceClient;
         this.inventoryServiceClient = inventoryServiceClient;
     }
    @GetMapping("/bills/full/{id}")
    Bill getBill(@PathVariable(name="id") Long id){
        Bill bill=billRepository.findById(id).get();
        bill.setCustomer(customerServiceClient.findCustomerById(bill.getCustomerID()));
        bill.setProductItems(productItemRepository.findByBillId(id));
        bill.getProductItems().forEach(pi->{
            Product restProduct =inventoryServiceClient.findProductById(pi.getProductID());
            pi.setProductName(restProduct.getName());
            pi.setQuantityInStock(restProduct.getQuantity());
            pi.setProductID(restProduct.getId());
            pi.setPriceInStock(restProduct.getPrice());
        });
        return bill;
     }

}
