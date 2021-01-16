package org.sid.billingService.repositories;

import org.sid.billingService.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
@CrossOrigin("*")

@RepositoryRestResource
public interface ProductItemRepository extends
        JpaRepository<ProductItem,Long> {
    List<ProductItem> findByBillId(Long billID);
}