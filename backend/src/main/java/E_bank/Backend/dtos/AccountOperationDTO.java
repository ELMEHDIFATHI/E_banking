package E_bank.Backend.dtos;

import lombok.Data;
import E_bank.Backend.enumes.OperationType;

import java.util.Date;


@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
