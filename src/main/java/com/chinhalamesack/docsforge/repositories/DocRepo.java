package com.chinhalamesack.docsforge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chinhalamesack.docsforge.entities.DocEntity;

public interface DocRepo extends JpaRepository<DocEntity, Long> {
	
	DocEntity findById(long id);
	
	DocEntity findByName(String name);
	
	void delete(DocEntity doc);

}
