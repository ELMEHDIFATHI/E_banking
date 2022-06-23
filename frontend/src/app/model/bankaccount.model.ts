export interface BankAccount {
  type:          string;
  id:            string;
  balance:       number;
  createdAt:     Date;
  status:        null;
  customerDTO:   CustomerDTO;
  interestRate?: number;
  overDraft?:    number;
}

export interface CustomerDTO {
  id:    number;
  name:  string;
  email: string;
}
