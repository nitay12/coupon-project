package com.nitay.couponproject.utils;

import com.nitay.couponproject.config.Config;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Stack;

/**
 * A singleton connection pool, stores a number of connections so they can be reused for future requests.
 * If all the connections are being used the
 */
public class ConnectionPool {
    @Getter
    @Setter
    boolean testMode = false;
    private static final int NUMBER_OF_CONNECTIONS = 5;
    private static ConnectionPool instance = null;
    private final Stack<Connection> connections = new Stack<>();

    private ConnectionPool() throws SQLException {
        openAllConnections();
    }

    /**
     * Gets the connection pool instance (lazy loading)
     *
     * @return ConnectionPool
     * @throws SQLException for any SQL problem
     */
    public static ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    private void openAllConnections() throws SQLException {
        for (int counter = 0; counter < NUMBER_OF_CONNECTIONS; counter++) {
            final Connection connection = DriverManager.getConnection(
                    Config.getMySQLurl(),
                    Config.getMySQLuser(),
                    Config.getMySQLpass());
            connections.push(connection);
        }
    }

    /**
     * Closing all the connections
     *
     * @throws InterruptedException If the waiting threads are interrupted
     */
    public void closeAllConnections() throws InterruptedException {
        synchronized (connections) {
            while (connections.size() < NUMBER_OF_CONNECTIONS) {
                connections.wait();
            }
            connections.removeAllElements();
        }
    }

    /**
     * Get an available connection from the connection pool.
     * If the connection pool is empty the requesting thread is waiting for an available connection
     *
     * @return Connection object
     * @throws InterruptedException If the waiting thread is interrupted
     */
    public Connection getConnection() throws InterruptedException {
        synchronized (connections) {
            final long start = Calendar.getInstance().getTimeInMillis();
            if (connections.isEmpty() & testMode) {
                System.out.println(Thread.currentThread().getName() + " is waiting for an available connection");
            }
            while (connections.isEmpty()) {
                connections.wait();
            }
            final long end = Calendar.getInstance().getTimeInMillis();
            final long duration = end - start;
            if (testMode) {
                System.out.println(Thread.currentThread().getName() + " found available connection after " + duration + " ms");
            }
            return connections.pop();
        }
    }

    /**
     * Returns a connection to the connection pool
     *
     * @param connection a connection object to return
     */
    public void returnConnection(final Connection connection) {
        synchronized (connections) {
            if (connection == null) {
                System.out.println("Attempt to return null connection terminated");
                return;
            }
            connections.push(connection);
            if (testMode) {
                System.out.println(Thread.currentThread().getName() + " is returning it's connection, now there are " + connections.size());
            }
            connections.notifyAll();
        }
    }
}