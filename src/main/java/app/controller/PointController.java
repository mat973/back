package app.controller;

import app.data.ResponseMessage;
import app.entities.Point;
import app.repositories.PointRepository;
import app.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import app.model.Graphic;

import java.security.Principal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final Graphic graphic;
    private static final Logger logger = LoggerFactory.getLogger(PointController.class);

    PointController(PointRepository pointRepository, UserRepository userRepository, Graphic graphic) {
        this.pointRepository = pointRepository;
        this.graphic = graphic;
        this.userRepository = userRepository;
    }

    @CrossOrigin
    @GetMapping
    Collection<Point> allPoints(Principal user) {
        logger.info("all points request from "+user.getName());
        Collection<Point> points = pointRepository.findAllByUser(userRepository.findOneByUsername(user.getName()));
        ArrayDeque<Point> pointsReverse = new ArrayDeque<>();
        for (Point point : points) {
            pointsReverse.addFirst(point);
        }
        return pointsReverse;
    }

    @CrossOrigin
    @PostMapping
    Point newPoint(@RequestBody Point newPoint, Principal user) {
        logger.info("New point request from "+user.getName());
        logger.info(newPoint.toString());
        // Округление до 3 знаков после запятой
        newPoint.setX(Math.round(newPoint.getX()*1000)/1000.0);
        newPoint.setY(Math.round(newPoint.getY()*1000)/1000.0);

        newPoint.setResult(graphic.isInArea(newPoint));
        newPoint.setOdz(graphic.isInODZ(newPoint));
        newPoint.setUser(userRepository.findOneByUsername(user.getName()));
        return pointRepository.save(newPoint);
    }

    @CrossOrigin
    @DeleteMapping
    ResponseMessage deletePoint(Long id, Principal user) {
        logger.info("Delete point request from "+user.getName());
        pointRepository.deleteById(id);
        return new ResponseMessage("successful delete");
    }

    @CrossOrigin
    @DeleteMapping("deleteall")
    ResponseMessage deleteAllPoints(Principal user) {
        logger.info("Delete all points request from "+user.getName());
        Collection<Point> points = pointRepository.findAllByUser(userRepository.findOneByUsername(user.getName()));
        for (Point point : points) {
            pointRepository.deleteById(point.getId());
        }
        return new ResponseMessage("successful delete");
    }


    @CrossOrigin
    @GetMapping("recalculate")
    Collection<Point> allPointsRecalculation(Double r, Principal user) {
        logger.info("Recalculate points request from "+user.getName());
        List<Point> recalculated = new ArrayList<>();
        Collection<Point> points = pointRepository.findAllByUser(userRepository.findOneByUsername(user.getName()));

        for (Point p : points) {
            Point point = new Point(null, p.getX(), p.getY(), r, false, p.isOdz(), null);
            point.setResult(graphic.isInArea(point));
            recalculated.add(point);
        }

        return recalculated;
    }

}
