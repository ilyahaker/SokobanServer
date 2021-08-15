package io.ilyahaker.sokobanserver.database.impl;

import io.ilyahaker.sokobanserver.database.api.PreparedQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PreparedQueryImpl<R> implements PreparedQuery<R> {

    private static Map<Class, TriConsumer<PreparedStatement, Integer, Object>> mapping = new LinkedHashMap<>();

    static {
        mapping.put(Byte.class, (statement, index, object) -> {
            statement.setInt(index, (Byte)object);
        });
        mapping.put(Byte.TYPE, (statement, index, object) -> {
            statement.setInt(index, (Byte)object);
        });
        mapping.put(Long.class, (statement, index, object) -> {
            statement.setLong(index, (Long)object);
        });
        mapping.put(Long.TYPE, (statement, index, object) -> {
            statement.setLong(index, (Long)object);
        });
        mapping.put(Integer.class, (statement, index, object) -> {
            statement.setInt(index, (Integer)object);
        });
        mapping.put(Integer.TYPE, (statement, index, object) -> {
            statement.setInt(index, (Integer)object);
        });
        mapping.put(String.class, (statement, index, object) -> {
            statement.setString(index, (String)object);
        });
        mapping.put(Double.class, (statement, index, object) -> {
            statement.setDouble(index, (Double)object);
        });
        mapping.put(Double.TYPE, (statement, index, object) -> {
            statement.setDouble(index, (Double)object);
        });
        mapping.put(Float.class, (statement, index, object) -> {
            statement.setFloat(index, (Float)object);
        });
        mapping.put(Float.TYPE, (statement, index, object) -> {
            statement.setFloat(index, (Float)object);
        });
        mapping.put(Boolean.class, (statement, index, object) -> {
            statement.setBoolean(index, (Boolean)object);
        });
        mapping.put(Boolean.TYPE, (statement, index, object) -> {
            statement.setBoolean(index, (Boolean)object);
        });
        mapping.put(byte[].class, (statement, index, object) -> {
            statement.setBytes(index, (byte[])((byte[])object));
        });
        mapping.put(UUID.class, (statement, index, object) -> {
            statement.setString(index, object.toString());
        });
    }

    public static void registerMapping(Class<?> clazz, TriConsumer<PreparedStatement, Integer, Object> mapper) {
        mapping.put(clazz, mapper);
    }

    private Supplier<Connection> connectionSupplier;
    private SqlBiConsumer<R, Connection> connectionCloser;
    private String query;
    private PreparedStatement statement;
    private SqlFunction<PreparedStatement, R> generator;
    private boolean valid = true;

    public PreparedQueryImpl(Supplier<Connection> connectionSupplier, String query, SqlFunction<PreparedStatement, R> generator, SqlBiConsumer<R, Connection> connectionCloser) {
        this.connectionSupplier = connectionSupplier;
        this.query = query;
        this.generator = generator;
        this.connectionCloser = connectionCloser;
    }

    public R execute(Object... args) {
        return execute(() -> {
            this.preExecute(args);
        });
    }

    public R execute(SqlRunnable preparation) {
        try {
            return executeDangerous(preparation);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private R executeDangerous(SqlRunnable preparation) throws SQLException {
        if (!this.valid) {
            throw new IllegalStateException("This prepared query was already used: it is not valid anymore");
        } else {
            this.valid = false;

            Connection connection = this.connectionSupplier.get();
            if (connection == null) {
                throw new SQLException("Connection is not available");
            } else {
                statement = connection.prepareStatement(query);
                preparation.run();
                R value = generator.apply(statement);
                connectionCloser.apply(value, connection);
                return value;
            }
        }
    }

    public void preExecute(Object... args) {
        try {
            for (int i = 0; i < args.length; i++) {
                Object object = args[i];

                mapping.get(object.getClass()).apply(statement, i + 1, object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public interface SqlFunction<T, E> {
        E apply(T t) throws SQLException;
    }

    public interface SqlBiConsumer<T, E> {
        void apply(T t, E e) throws SQLException;
    }

    public interface SqlRunnable {
        void run() throws SQLException;
    }

    public interface TriConsumer<T, E, K> {
        void apply(T t, E e, K k) throws SQLException;
    }
}
