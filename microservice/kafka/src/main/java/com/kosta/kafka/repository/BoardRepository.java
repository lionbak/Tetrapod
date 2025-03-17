package com.kosta.kafka.repository;

import com.kosta.kafka.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>{

}
