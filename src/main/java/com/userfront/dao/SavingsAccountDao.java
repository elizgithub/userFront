package com.userfront.dao;

import com.userfront.model.PrimaryAccount;
import com.userfront.model.SavingsAccount;
import org.springframework.data.repository.CrudRepository;

public interface SavingsAccountDao extends CrudRepository<SavingsAccount,Long>{
    SavingsAccount findByAccountNumber(int accountNumber);

}
