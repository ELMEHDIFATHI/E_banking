package E_bank.Backend.repositories;

import E_bank.Backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    List<Customer> findByNameContains(String keyword);
    @Query("select c from Customer c where c.name like:kw")
    List<Customer> serchCustomer(@Param("kw") String keyword);
}
