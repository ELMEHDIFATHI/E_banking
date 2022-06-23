package E_bank.Backend.services;

import E_bank.Backend.dtos.*;
import E_bank.Backend.entities.*;
import E_bank.Backend.enumes.OperationType;
import E_bank.Backend.exceptions.BalanceNotSufficentExeption;
import E_bank.Backend.exceptions.CustomerNotFoundExeption;
import E_bank.Backend.mappers.BankAccountMapperImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import E_bank.Backend.exceptions.BankAccountNotfoundExeption;
import E_bank.Backend.repositories.AccountOperationRepository;
import E_bank.Backend.repositories.BankAccountRepository;
import E_bank.Backend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;

    private BankAccountRepository bankAccountRepository;

    private AccountOperationRepository accountOperationRepository;

    private BankAccountMapperImpl dtoMapper;


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("update  customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
        log.info("delete customer");
        customerRepository.deleteById(customerId);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundExeption {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer==null)
            throw new CustomerNotFoundExeption("customer not found");


        CurrentAccount currentAccount= new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankaccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankaccount) ;
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double intrestRate, Long customerId) throws CustomerNotFoundExeption {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer==null)
            throw new CustomerNotFoundExeption("customer not found");


        SavingAccount savingAccount= new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setInterestRate(intrestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankaccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankaccount) ;


    }

    @Override
    public List<CustomerDTO> listCustemers() {
        List<Customer> customers = customerRepository.findAll();

        /*List<CustomerDTO>customerDTOS=new ArrayList<>();
        for(Customer customer:customers){
            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);*/

        List<CustomerDTO> CustomerDTOs = customers.stream()
                .map(cust-> dtoMapper.fromCustomer(cust))
                .collect(Collectors.toList());
        return CustomerDTOs;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotfoundExeption {
        BankAccount bankAccount= bankAccountRepository.findById(accountId).orElseThrow(
                ()->new BankAccountNotfoundExeption("bank Account not found"));

        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount =(SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }else {
            CurrentAccount currentAccount =(CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }

    }

//get get Bank Account By Id method that use bankAccountRepository
     private BankAccount getBankAccountById(String accountID) throws BankAccountNotfoundExeption {
        return bankAccountRepository.findById(accountID).orElseThrow(
                ()->new BankAccountNotfoundExeption("bank Account not found"));
    }
    @Override
    public void debit(String accountID, double amount, String description) throws BankAccountNotfoundExeption, BalanceNotSufficentExeption {

        BankAccount bankAccount= getBankAccountById(accountID);

        if (bankAccount.getBalance()<amount)
            throw new BalanceNotSufficentExeption("Balance not sufficent");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }



    @Override
    public void credit(String accountID, double amount, String description) throws BankAccountNotfoundExeption{

        BankAccount bankAccount= getBankAccountById(accountID);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIDSource, String accountIdDestination, double amount) throws BalanceNotSufficentExeption, BankAccountNotfoundExeption {
        debit(accountIDSource,amount,"transfer to"+accountIdDestination);
        credit(accountIdDestination,amount,"transfer from"+accountIDSource);

    }
    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> BankAccountDTOs = bankAccounts.stream().map(account -> {
            if (account instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) account;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) account;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());

        return BankAccountDTOs;
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        List<AccountOperationDTO> accountOperationDTOs = accountOperations.stream().map(op ->
                dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        return accountOperationDTOs;
    }
    @Override
    public CustomerDTO getCustomer(Long custemerId) throws CustomerNotFoundExeption {
        Customer customer = customerRepository.findById(custemerId).orElseThrow(() -> new CustomerNotFoundExeption("Customer Not found !"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotfoundExeption {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount==null)throw new BankAccountNotfoundExeption("Account not found !");
        Page<AccountOperation> accountOperation = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> AccountOperationDTOs = accountOperation.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOs(AccountOperationDTOs);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrrentPage(page);
        accountHistoryDTO.setTotalPAges(size);
        accountHistoryDTO.setTotalPAges(accountOperation.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> serachCustomer(String keyword) {
        List<Customer> customers = customerRepository.serchCustomer(keyword);
         return customers.stream()
                 .map(customer -> dtoMapper.fromCustomer(customer))
                 .collect(Collectors.toList());

    }
    @Override
    public List<BankAccountDTO> getCustemerAccounts(Long custemrId){
        List<BankAccount> custemerBankAccount = bankAccountRepository.findByCustomer_Id(custemrId);
        return custemerBankAccount.stream()
                .map(bankAccount -> {
                   if(bankAccount instanceof SavingAccount){
                       return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
                   }else {
                      return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
                   }
                }).collect(Collectors.toList());

    }

}
