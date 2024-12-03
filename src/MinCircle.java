import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinCircle {
    public static Cluster findMinCluster(List<Node> nodes, double maxRadius) {
        return welzl(nodes, new ArrayList<>(), maxRadius);
    }

    private static Cluster welzl(@NotNull List<Node> points, List<Node> boundary, double maxRadius) {
        if (points.isEmpty() || boundary.size() == 3) {
            return createClusterFromBoundary(boundary, maxRadius); // 传递 maxRadius
        }

        Node p = points.remove(new Random().nextInt(points.size()));

        Cluster cluster = welzl(points, boundary, maxRadius);

        if (cluster != null && contains(cluster, p)) {
            points.add(p); // 放回节点
            return cluster; // 返回当前最小簇
        }

        boundary.add(p);
        Cluster minCluster = welzl(points, boundary, maxRadius);
        boundary.remove(p); // 回溯移除
        points.add(p); // 放回节点

        return minCluster; // 返回最小簇
    }

    @org.jetbrains.annotations.NotNull
    private static Cluster createClusterFromBoundary(@NotNull List<Node> boundary, double maxRadius) {
        if (boundary.isEmpty()) return new Cluster(new Node(0, 0, 0), null, 0); // id 设为 0

        if (boundary.size() == 1) {
            Cluster cluster = new Cluster(boundary.get(0), boundary.get(0), 0); // 簇头为自身
            cluster.addNode(boundary.get(0)); // 添加节点
            return cluster;
        }

        if (boundary.size() == 2) {
            Node p1 = boundary.get(0);
            Node p2 = boundary.get(1);
            Node center = new Node(0, (p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2); // id 设为 0
            double radius = distance(p1, p2) / 2;
            Cluster cluster = new Cluster(center, null, radius); // 不指定簇头
            cluster.addNode(p1);
            cluster.addNode(p2);
            // 选择离中心最近的节点作为簇头
            Node closestNode = getClosestNode(center, boundary);
            cluster.setClusterHead(closestNode); // 设置簇头
            return cluster;
        }

        Cluster cluster =  createClusterFromThreePoints(boundary.get(0), boundary.get(1), boundary.get(2), maxRadius);
        // 添加边界节点到簇内
        for (Node node : boundary) {
            cluster.addNode(node);
        }

        // 选择离中心最近的节点作为簇头
        Node closestNode = getClosestNode(cluster.getCenter(), boundary);
        cluster.setClusterHead(closestNode); // 设置簇头

        return cluster;

    }


    private static Cluster createClusterFromThreePoints(Node a, Node b, Node c, double maxRadius) {
        double ax = a.getX(), ay = a.getY();
        double bx = b.getX(), by = b.getY();
        double cx = c.getX(), cy = c.getY();

        double d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by));
        double ux = ((ax * ax + ay * ay) * (by - cy) + (bx * bx + by * by) * (cy - ay) + (cx * cx + cy * cy) * (ay - by)) / d;
        double uy = ((ax * ax + ay * ay) * (cx - bx) + (bx * bx + by * by) * (ax - cx) + (cx * cx + cy * cy) * (bx - ax)) / d;

        Node center = new Node(0, ux, uy); // id 设为 0
        double radius = Math.max(distance(center, a), Math.max(distance(center, b), distance(center, c)));

        // 只有当半径小于或等于 maxRadius 时，才返回新簇
        if (radius <= maxRadius) {
            return new Cluster(center, null, radius); // 不指定簇头
        } else {
            return null; // 返回 null 表示不符合条件
        }
    }

    private static boolean contains(Cluster cluster, Node point) {
        return distance(cluster.getCenter(), point) <= cluster.getRadius();
    }

    private static double distance(Node p1, Node p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    // 获取距离簇中心最近的节点
    private static Node getClosestNode(Node center, List<Node> nodes) {
        Node closestNode = null;
        double minDistance = Double.MAX_VALUE;

        for (Node node : nodes) {
            double dist = distance(center, node);
            if (dist < minDistance) {
                minDistance = dist;
                closestNode = node; // 更新最小距离的节点
            }
        }

        return closestNode;
    }
}
