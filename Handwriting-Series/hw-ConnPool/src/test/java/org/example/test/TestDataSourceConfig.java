package org.example.test;

import org.example.connectionpool.ConnectionPool;
import org.example.connectionpool.DataSourceConfig;
import org.example.connectionpool.DataSourceManager;
import org.junit.Test;

import java.sql.Connection;

public class TestDataSourceConfig {
    public static void main(String[] args) {
        Thead1 t = new Thead1();
        for (int i = 0; i < 8; i++) {
            new Thread(t, "线程" + i).start();
        }
    }
}

class Thead1 implements Runnable {
    public void run(){
        try {
            Connection conn = DataSourceManager.getConn();
            Thread.sleep(1000);
            DataSourceManager.releaseConn(conn);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}