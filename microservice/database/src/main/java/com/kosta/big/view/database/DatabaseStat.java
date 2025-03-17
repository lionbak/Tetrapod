package com.kosta.big.view.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/status")
public class DatabaseStat {

    private final JdbcTemplate masterJdbcTemplate;
    private final JdbcTemplate slaveJdbcTemplate;

    @Autowired
    public DatabaseStat(@Qualifier("masterJdbcTemplate") JdbcTemplate masterJdbcTemplate,
                        @Qualifier("slaveJdbcTemplate") JdbcTemplate slaveJdbcTemplate) {
        this.masterJdbcTemplate = masterJdbcTemplate;
        this.slaveJdbcTemplate = slaveJdbcTemplate;
    }
    @GetMapping
    public String getDatabaseStatus(@RequestParam("database") String database) {
        try{
            if("master".equalsIgnoreCase(database)){
                masterJdbcTemplate.queryForObject("select 1 from dual", Integer.class);
                return "Enabled";
            } else if ("slave".equalsIgnoreCase(database)){
                slaveJdbcTemplate.queryForObject("select 1 from dual", Integer.class);
                return "Enabled";
            } else {
                return "Unknown database";
            }
        } catch (Exception e) {
            if("master".equalsIgnoreCase(database)){
                return "Disabled";
            } else if ("slave".equalsIgnoreCase(database)){
                return "Disabled";
            } else {
                return "Unknown database";
            }
        }
    }

    @GetMapping("/all")
    public Map<String, String> getAllDatabaseStatuses() {
        Map<String, String> statuses = new HashMap<>();
        statuses.put("master", getDatabaseStatus("master"));
        statuses.put("slave", getDatabaseStatus("slave"));
        return statuses;
    }

}
