package server.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 图片的数据类 持有shape的list id
 */
public class Picture implements Serializable{

    private String id;

    private List<Shape> shapes;

    public Picture(String id, List<Shape> shapes) {
        this.id = id;
        this.shapes = shapes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }
}
