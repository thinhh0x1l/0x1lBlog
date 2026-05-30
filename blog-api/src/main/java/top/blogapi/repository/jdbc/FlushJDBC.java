package top.blogapi.repository.jdbc;

import org.springframework.stereotype.Repository;
import top.blogapi.dto.internal.UpdateVisitExpireFlush;
import top.blogapi.model.entity.Visit;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class FlushJDBC {

    private final DataSource dataSource;

    public FlushJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertVisit(List<Visit> list) {

        String sql = """
            INSERT INTO visit
            (guest_id, ip, ip_source, os, browser, user_agent, pv, started_at, last_activity)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (Visit v : list) {
                ps.setLong(1, v.getGuestId());
                ps.setString(2, v.getIp());
                ps.setString(3, v.getIpSource());
                ps.setString(4, v.getOs());
                ps.setString(5, v.getBrowser());
                ps.setString(6, v.getUserAgent());
                ps.setInt(7, v.getPv());
                ps.setTimestamp(8, Timestamp.valueOf(v.getStartedAt()));
                ps.setTimestamp(9, Timestamp.valueOf(v.getLastActivity()));

                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException("Insert batch thất bại", e);
        }
    }
}