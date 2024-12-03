import java.util.ArrayList;
import java.util.List;

public class Hexagon {
    private static int idCounter = 1; // 静态计数器，用于生成唯一ID
    private int id; // 六边形的唯一标识符
    //private double R;  //六边形半径
    private double centerX;  //中心横坐标
    private double centerY;  //中心纵坐标

    private List<Node> vertices; // 六边形的六个顶点，按顺时针或逆时针顺序排列

    private int nodeCount; // 六边形内的节点数量

    private int neighborCount;  //相邻六边形为不满足区域的个数

    private List<Hexagon> neighbor;  //相邻不满足区域

    public Hexagon(double x, double y) {
        this.id = idCounter++; // 分配当前ID并增加计数器
        this.centerX = x;
        this.centerY = y;
        this.nodeCount = 0;
        this.neighborCount = 0;
        this.neighbor = new ArrayList<>();
    }
    public int getId() {
        return id; // 返回ID
    }
    public double getCenterX() {
        return centerX; // 返回中心横坐标
    }
    public double getCenterY() {
        return centerY; // 返回中心纵坐标
    }
    public int getNodeCount() {
        return nodeCount;
    }
    public List<Hexagon> getNeighbor(){ return neighbor;}

    // 计算并返回六边形的六个顶点坐标
    public List<Node> calculateVertices(int r) {
        List<Node> vertices = new ArrayList<>();
        double angleIncrement = Math.PI / 3; // 60度的增量

        for (int i = 0; i < 6; i++) {
            double angle = i * angleIncrement;
            double x = centerX + r * Math.cos(angle);
            double y = centerY + r * Math.sin(angle);
            vertices.add(new Node(i+1,x, y));
        }
        if (vertices.isEmpty()) {
            throw new IllegalStateException("无法计算六边形顶点，顶点列表为空");
        }
        return vertices;
    }



    // Setter 方法，允许外部更新节点数量
    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public int getNeighborCount() {
        return neighborCount;
    }

    public void setNeighborCount(int neighborCount) {
        this.neighborCount = neighborCount;
    }


}
