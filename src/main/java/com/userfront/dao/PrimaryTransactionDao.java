package com.userfront.dao;

import com.userfront.model.PrimaryTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrimaryTransactionDao extends CrudRepository <PrimaryTransaction, Long> {

    //list of primary transaction
    List<PrimaryTransaction> findAll();
}
