package com.kosta.big.view.hikari;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/status")
public class HikariController {

    private final DataSource masterDataSource;
    private final DataSource slaveDataSource;

    @Autowired
    public HikariController(@Qualifier("masterDataSource") DataSource masterDataSource,
                            @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        this.masterDataSource = masterDataSource;
        this.slaveDataSource = slaveDataSource;
    }


    @GetMapping("/hikari")
    public Map<String, Object> getHikariDataSource() throws SQLException {
        Map<String, Object> status = new HashMap<>();

        status.put("master", getPoolStatus(masterDataSource));
        status.put("slave", getPoolStatus(slaveDataSource));

        return status;

    }

    private Map<String, Object> getPoolStatus(DataSource dataSource) throws SQLException {
        Map<String, Object> poolStatus = new HashMap<>();
        try (Connection connection = dataSource.getConnection()){
            com.zaxxer.hikari.HikariDataSource hikariDataSource = (com.zaxxer.hikari.HikariDataSource) dataSource;
            poolStatus.put("totalConnection", hikariDataSource.getHikariPoolMXBean().getTotalConnections());
            poolStatus.put("activeConnection", hikariDataSource.getHikariPoolMXBean().getActiveConnections());
            poolStatus.put("maxConnection", hikariDataSource.getMaximumPoolSize());
            poolStatus.put("idleConnection", hikariDataSource.getHikariPoolMXBean().getIdleConnections());
            poolStatus.put("threadAwaiting", hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        return poolStatus;
    }


}
