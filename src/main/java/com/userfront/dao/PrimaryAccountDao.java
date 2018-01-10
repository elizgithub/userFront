package com.userfront.dao;

import com.userfront.model.PrimaryAccount;
import org.springframework.data.repository.CrudRepository;

public interface PrimaryAccountDao extends CrudRepository<PrimaryAccount,Long> {
    PrimaryAccount findByAccountNumber(int accountNumber);
}
