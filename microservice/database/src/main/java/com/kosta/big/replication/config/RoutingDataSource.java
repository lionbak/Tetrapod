package com.kosta.big.replication.config;

import com.kosta.big.replication.common.DataSourceType;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.reactive.TransactionContext;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        //Transactional(readOnly = true)유무 따라 마스터 또는 슬레이브 선택
        //master, slave -> primary, replica로 바꿔서 표현
        DataSourceType dataSourceKey = TransactionSynchronizationManager
                .isCurrentTransactionReadOnly() ? DataSourceType.slave : DataSourceType.master;
        System.out.println("데이터 소스 라우팅 : " + dataSourceKey);
        return dataSourceKey;
    }
}
