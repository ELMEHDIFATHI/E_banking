export interface AccountDetails {
  accountId:            string;
  balance:              number;
  currrentPage:         number;
  totalPAges:           number;
  pageSize:             number;
  accountOperationDTOs: AccountOperation[];
}

export interface AccountOperation {
  id:            number;
  operationDate: Date;
  amount:        number;
  type:          string;
  description:   string;
}
