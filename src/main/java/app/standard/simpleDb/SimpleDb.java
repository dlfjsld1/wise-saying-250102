package app.standard.simpleDb;

import app.standard.Util;
import lombok.Setter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SimpleDb {
    // 데이터베이스 연결 정보를 저장할 변수들
    private String dbUrl;  // JDBC URL (데이터베이스 주소)
    private String dbUser; // 데이터베이스 사용자 이름
    private String dbPassword; // 데이터베이스 비밀번호
    private Map<String, Connection> connections; // 데이터베이스 연결 객체
    @Setter
    private boolean devMode = false; // 개발 모드 활성화 여부 (로그 출력 등에 사용)

    // 생성자: SimpleDb 객체를 만들 때 호출되는 메서드
    // 데이터베이스 연결 정보를 받아서 초기화하고, 실제 데이터베이스 연결을 시도
    public SimpleDb(String host, String user, String password, String dbName) {
        // JDBC URL 생성 (예: jdbc:mysql://localhost:3306/mydatabase)
        this.dbUrl = "jdbc:mysql://" + host + ":3306/" + dbName;
        this.dbUser = user; // 사용자 이름 설정
        this.dbPassword = password; // 비밀번호 설정
        connections = new HashMap<>();

    }

//    현재 스레드의 이름을 키로 사용하여 connections 맵에서 해당 스레드에 대한 Connection 객체를 가져온다.
//    만약 해당 스레드에 대한 Connection 객체가 없다면, 새로운 Connection 객체를 생성하고 connections 맵에 추가한다.
//    그리고 해당 Connection 객체를 반환한다.
    private Connection getCurrentThreadConnection() {
        try{
            String currentThreadName = Thread.currentThread().getName();
            Connection conn = connections.get(currentThreadName);

            if(conn == null) {
                Connection currentThreadConn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                connections.put(currentThreadName, currentThreadConn);

                return currentThreadConn;
            }
            return conn;
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
    }

    // SQL 실행 메서드 (SELECT 결과가 boolean으로 반환되는 쿼리)
    public boolean selectBoolean(String sql, List<Object> params) {
        return _run(sql, Boolean.class, params);
    }

    public String selectString(String sql, List<Object> params) {
        return _run(sql, String.class, params);
    }

    public Long selectLong(String sql, List<Object> params) {
        return _run(sql, Long.class, params);
    }

    public LocalDateTime selectDateTime(String sql, List<Object> params) {
        return _run(sql, LocalDateTime.class, params);
    }

    public Map<String, Object> selectRow(String sql, List<Object> params) {
        return _run(sql, Map.class, params);
    }

    public <T> T selectRow(String sql, List<Object> params, Class<T> cls) {
        List<T> rows = selectRows(sql, params, cls);
        if (rows.isEmpty()) {
            return null;
        }
        return rows.getFirst();
    }

    public List<Map<String, Object>> selectRows(String sql, List<Object> params) {
        return _run(sql, List.class, params);
    }

    public <T> List<T> selectRows(String sql, List<Object> params, Class<T> cls) {
        return selectRows(sql, params).stream()
            .map(m -> Util.Mapper.mapToObj(m, cls)).toList();
    }

    public int delete(String sql, List<Object> params) {
        return _run(sql, Integer.class, params);
    }

    public int update(String sql, List<Object> params) {
        return _run(sql, Integer.class, params);
    }

    public long insert(String sql, List<Object> params) {
        return _run(sql, Long.class, params);
    }

    // Sql 객체 생성
    public Sql genSql() {
        return new Sql(this);
    }

    public int run(String sql, Object... params) {
        return _run(sql, Integer.class, Arrays.stream(params).toList());
    }
    // SQL 실행 메서드 (INSERT, UPDATE, DELETE 등 결과를 반환하지 않는 쿼리)
    // type - 0: boolean 1: String
    public <T> T _run(String sql, Class<T> cls, List<Object> params) {
        Connection connection = getCurrentThreadConnection();
        System.out.println(sql);
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(stmt, params);//파라미터 설정
            if (sql.startsWith("SELECT")) {
                ResultSet rs = stmt.executeQuery();
                return parseResultSet(rs, cls);
            }
            if(sql.startsWith("INSERT")) {
                if(cls == Long.class) {
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    if(rs.next()) {
                        return cls.cast(rs.getLong(1));
                    }
                }
                if(cls == Integer.class) {
                    return cls.cast(stmt.executeUpdate());
                }
            }

            // PreparedStatement를 사용하여 SQL 쿼리 실행
            return cls.cast(stmt.executeUpdate()); // 실제 반영된 로우 수 반환
        } catch (SQLException e) {
            throw new RuntimeException("SQL 실행 실패: " + e.getMessage());
        }
    }

    private <T> T parseResultSet(ResultSet rs, Class<T> cls) throws SQLException {
        if (cls == Boolean.class) {
            rs.next();
            return cls.cast(rs.getBoolean(1));
        } else if (cls == String.class) {
            rs.next();
            return cls.cast(rs.getString(1));
        } else if (cls == Long.class) {
            rs.next();
            return cls.cast(rs.getLong(1));
        } else if (cls == LocalDateTime.class) {
            rs.next();
            return cls.cast(rs.getTimestamp(1).toLocalDateTime());
        } else if (cls == Map.class) {
            rs.next();

            Map<String, Object> row = rsRowToMap(rs);

            //아래에서 cast는 캐스팅을 하는데 캐스팅이란 타입을 변환하는 것을 말한다.
            return cls.cast(row);
        } else if(cls == List.class) {
            List<Map<String, Object>> rows = new ArrayList<>();
            while(rs.next()) {
                Map<String, Object> row = rsRowToMap(rs);
                rows.add(row);
            }
            return cls.cast(rows);
        }
        throw new RuntimeException("정의되지 않은 타입이 반환됨.");
    }

    private Map<String, Object> rsRowToMap(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        Map<String, Object> row = new HashMap<>();

        for (int i = 1; i <= columnCount; i++) {
            String cname = metaData.getColumnName(i);
            row.put(cname, rs.getObject(i));
        }
        return row;
    }

    // PreparedStatement에 파라미터 바인딩 메서드
    private void setParams(PreparedStatement stmt, List<Object> params) throws SQLException {
        // 가변 인자(params)로 받은 값들을 PreparedStatement의 '?' 위치에 순서대로 설정
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i)); // '?' 위치는 1부터 시작
        }
    }


    public List<Long> selectLongs(String sql, List<Object> params) {
        List<Map<String, Object>> maps = selectRows(sql, params);
        //아래 코드를 해석하자면 maps를 스트림으로 변환하고, 각 맵의 첫번째 값을 Long으로 변환하고, 리스트로 변환한다.
        return maps.stream()
                .map(m -> (Long)m.values().iterator().next())
                .toList();
    }

    public void close() {
        try {
            Connection connection = getCurrentThreadConnection();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void startTransaction() {
        try {
            Connection connection = getCurrentThreadConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        try {
            Connection connection = getCurrentThreadConnection();
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit() {
        try {
            Connection connection = getCurrentThreadConnection();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}