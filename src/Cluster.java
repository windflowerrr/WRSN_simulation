import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private Node center; // 簇的中心
    private Node ClusterHead;  //簇头
    private double radius; // 簇的半径，表示覆盖范围

    private List<Node> nodes; // 簇内包含的节点

    public Cluster(Node center, Node ClusterHead, double radius) {
        this.center = center;
        this.ClusterHead = ClusterHead;
        this.radius = radius;
        this.nodes = new ArrayList<>();
        // 如果 clusterHead 不为空，将该簇关联到簇头节点
        if (ClusterHead != null) {
            ClusterHead.setCluster(this);
        } else {
            System.out.println("警告：簇头节点 (ClusterHead) 为 null，此簇未关联任何簇头节点。");
        }
    }

    public Node getCenter() {
        return center;
    }

    public Node getClusterHead(){
        return ClusterHead;
    }

    public double getRadius() {
        return radius;
    }

    public void setClusterHead(Node clusterHead) {
        this.ClusterHead = clusterHead;
    }

    public List<Node> getNodes() {
        return nodes; // 返回簇内的节点列表
    }

    public void addNode(Node node) {
        nodes.add(node); // 添加节点到簇内
    }

    // 获取簇内节点数量
    public int getNodeCount() {
        return nodes.size();
    }

    // 计算无人机在当前簇的飞行高度
    public double calculateFlightHeight() {
        return radius / Math.tan(Math.toRadians(30)); // 将角度转换为弧度
    }
}
