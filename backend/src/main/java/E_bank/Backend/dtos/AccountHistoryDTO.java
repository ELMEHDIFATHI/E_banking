package E_bank.Backend.dtos;


import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int currrentPage;
    private int totalPAges;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOs;


}
