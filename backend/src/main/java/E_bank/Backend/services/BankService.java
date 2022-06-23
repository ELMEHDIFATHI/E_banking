package E_bank.Backend.services;


import E_bank.Backend.entities.BankAccount;
import E_bank.Backend.entities.CurrentAccount;
import E_bank.Backend.entities.SavingAccount;
import E_bank.Backend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("0e5742c2-9211-4f6f-a5e7-6ae126f9491c").orElse(null);
        System.out.println("********************************************");
        if (bankAccount!=null){


            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount){
                System.out.println("OverDraft => "+((CurrentAccount)bankAccount).getOverDraft());
            }else if (bankAccount instanceof SavingAccount){
                System.out.println("getInterestRate => "+((SavingAccount)bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op->{
                System.out.println(op.getType()+"\t"+op.getAmount()+"\t"+op.getOperationDate());

            });
        }
    }



}
