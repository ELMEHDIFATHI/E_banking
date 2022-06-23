package E_bank.Backend;

import E_bank.Backend.dtos.BankAccountDTO;
import E_bank.Backend.dtos.CurrentBankAccountDTO;
import E_bank.Backend.dtos.CustomerDTO;
import E_bank.Backend.dtos.SavingBankAccountDTO;
import E_bank.Backend.entities.AccountOperation;
import E_bank.Backend.entities.CurrentAccount;
import E_bank.Backend.entities.Customer;
import E_bank.Backend.entities.SavingAccount;
import E_bank.Backend.enumes.AccountStatus;
import E_bank.Backend.enumes.OperationType;
import E_bank.Backend.services.BankAccountService;


import E_bank.Backend.exceptions.CustomerNotFoundExeption;
import E_bank.Backend.repositories.AccountOperationRepository;
import E_bank.Backend.repositories.BankAccountRepository;
import E_bank.Backend.repositories.CustomerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }
//@Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args->{
                Stream.of("Mehdi","Yassine","Anasse").forEach(name->{
                    CustomerDTO customer = new CustomerDTO();
                    customer.setName(name);
                    customer.setEmail(name+"@gmail.com");
                    bankAccountService.saveCustomer(customer);
                });
                bankAccountService.listCustemers().forEach(customer -> {
                    try {
                        bankAccountService.saveCurrentBankAccount(Math.random()*9000,9000,customer.getId());
                        bankAccountService.saveSavingBankAccount(Math.random()*12000,5.5,customer.getId());

                    } catch (CustomerNotFoundExeption e) {
                        e.printStackTrace();
                    }


                });
            List<BankAccountDTO> bankAccountList= bankAccountService.bankAccountList();
            for (BankAccountDTO bankAccount:bankAccountList){
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if (bankAccount instanceof SavingBankAccountDTO){
                        accountId=((SavingBankAccountDTO) bankAccount).getId();
                    }else {
                        accountId=((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*120000,"credit");
                    bankAccountService.debit(accountId,1000+Math.random()*9000,"debit");
                }

            }
        };
    }

  @Bean
    CommandLineRunner  start(CustomerRepository customerRepository,
                             BankAccountRepository bankAccountRepository,
                             AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("archid", "demnati", "fathi").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);


                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);


            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i < 5; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);

                }


            });
        };
    }
}
