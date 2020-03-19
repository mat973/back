package app.model;

import app.entities.Point;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Graphic {

    private boolean isInArea(double x, double y, double r) {
        boolean triangle = x >= 0 && y <= 0 && y >= (x - r)/2;
        boolean square = x <= 0 && y >= 0 && x >= -r && y <= r;
        boolean sector = x <= 0 && y <= 0 && Math.sqrt(x*x + y*y) <= r/2;
        return triangle || square || sector;
    }

    private boolean isInODZ(double x, double y) {
        return !(x > 4) && !(x < -4) && !(y < -3) && !(y > 3);
    }

    public boolean isInODZ(Point point) { return isInODZ(point.getX(), point.getY()); }

    public boolean isInArea(Point point) {
        return isInArea(point.getX(), point.getY(), point.getR());
    }

}
