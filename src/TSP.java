import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TSP {
    // 计算两个节点之间的欧几里得距离
    private static double distance(Node p1, Node p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    // 获取所有簇的中心节点（可以选择簇的质心或者簇头）
    public static List<Node> getClusterCenters(List<Cluster> clusters) {
        List<Node> centers = new ArrayList<>();
        for (Cluster cluster : clusters) {
            Node center = cluster.getCenter();  // 假设簇有一个 `getCenter()` 方法
            centers.add(center);
        }
        return centers;
    }

    // 构建距离矩阵
    public static double[][] buildDistanceMatrix(List<Node> centers) {
        int n = centers.size();
        double[][] distanceMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    distanceMatrix[i][j] = distance(centers.get(i), centers.get(j));
                } else {
                    distanceMatrix[i][j] = 0;  // 自己到自己的距离为0
                }
            }
        }
        return distanceMatrix;
    }

    public static double solveTSP(double[][] dist) {
        int n = dist.length;
        int allVisited = (1 << n) - 1; // 所有节点都已访问的状态

        // dp[mask][i] = 最短路径，mask表示已访问的节点集合，i表示最后访问的节点
        double[][] dp = new double[1 << n][n];
        for (int i = 0; i < (1 << n); i++) {
            Arrays.fill(dp[i], Double.MAX_VALUE);  // 初始化为很大的数
        }

        // 初始化起点（可以选择任何一个作为起点）
        dp[1][0] = 0;  // 从节点0出发

        for (int mask = 1; mask < (1 << n); mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0) continue;  // 如果u没有被访问过，跳过
                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0) {  // 如果v没有被访问过
                        dp[mask | (1 << v)][v] = Math.min(dp[mask | (1 << v)][v], dp[mask][u] + dist[u][v]);
                    }
                }
            }
        }

        // 返回从起点到所有节点的最短路径再回到起点
        double minCost = Double.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            minCost = Math.min(minCost, dp[allVisited][i] + dist[i][0]);
        }

        return minCost;
    }



}
