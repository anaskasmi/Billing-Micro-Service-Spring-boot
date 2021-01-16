package org.sid.billingService.repositories;

import org.sid.billingService.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource
public interface BillRepository extends JpaRepository<Bill,Long> {
}
