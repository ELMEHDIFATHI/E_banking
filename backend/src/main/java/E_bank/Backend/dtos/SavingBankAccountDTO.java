package E_bank.Backend.dtos;


import E_bank.Backend.enumes.AccountStatus;
import lombok.Data;

import java.util.Date;





@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;


}
