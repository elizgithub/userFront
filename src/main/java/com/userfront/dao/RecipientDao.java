package com.userfront.dao;

import com.userfront.model.Recipient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipientDao extends CrudRepository<Recipient,Long>
{
    List<Recipient> findAll();
    void deleteByName(String recipientName);
    Recipient findByName(String recipientName);
}
