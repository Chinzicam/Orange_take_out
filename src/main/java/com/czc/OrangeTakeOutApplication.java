package com.czc;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author czc
 */
@SpringBootApplication
@MapperScan("com.czc.*.mapper")
@Slf4j
@ServletComponentScan
@EnableTransactionManagement
public class OrangeTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrangeTakeOutApplication.class, args);
        log.info("项目启动成功...");
    }

}
