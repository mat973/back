package app.repositories;

import app.entities.Point;
import app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    Collection<Point> findAllByUser(User user);
    void deleteAllByUser(User user);
}
