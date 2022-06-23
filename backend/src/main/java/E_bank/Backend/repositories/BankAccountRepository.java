package E_bank.Backend.repositories;

import E_bank.Backend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    public List<BankAccount> findByCustomer_Id(Long id);
}
