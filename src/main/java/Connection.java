import org.apache.commons.dbcp2.BasicDataSource;

public class Connection {

    private BasicDataSource dataSource;

    public Connection () {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/gameCore?useTimezone=true&serverTimezone=UTC");
        dataSource.setUsername("ADM");
        dataSource.setPassword("Tetsugairu35.");
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }
}
