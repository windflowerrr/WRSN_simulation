import java.util.*;

public class Charge {

    static double w1 = 0.6, w2 = 0.4;  //权重
    // 初始化最大和最小飞行高度
    static double maxFlightHeight = Double.NEGATIVE_INFINITY;
    static double minFlightHeight = Double.POSITIVE_INFINITY;
    // 初始化最大和最小lifeTime
    static double maxLifeTime = Double.NEGATIVE_INFINITY;
    public static void chargeQueue(List<Cluster> clusters){
        List<Node> clusterHeads = new ArrayList<>();
        for (Cluster cluster : clusters) {
            clusterHeads.add(cluster.getClusterHead());
            // 遍历所有簇，计算每个簇的飞行高度，并更新最大和最小高度，最大最小生命期
            double height = cluster.calculateFlightHeight();
            maxFlightHeight = Math.max(maxFlightHeight, height);
            minFlightHeight = Math.min(minFlightHeight, height);
            maxLifeTime = Math.max(maxLifeTime, cluster.getClusterHead().getLifeTime());
        }
        // 将簇头节点按 lifeTime 从小到大排序
        clusterHeads.sort(Comparator.comparingDouble(Node::getLifeTime));
        //充电队列
        Queue<Node> prioritizedQueue = new LinkedList<>();
        // 获取并移除 lifeTime 最小的节点
        Node minLifeTimeNode = clusterHeads.remove(0);
        // 加入到新的队列中
        prioritizedQueue.add(minLifeTimeNode);
        //  按优先级规则将节点加入到队列中
        while (!clusterHeads.isEmpty()){
            Node lastAddedNode = prioritizedQueue.peek();
            // 计算每个节点的优先级
            Node nextNode = clusterHeads.stream()
                    .max(Comparator.comparingDouble(node -> calculatePriority(node, lastAddedNode)))
                    .orElse(null);
            // 将优先级最高的节点加入到优先队列并从原列表中移除
            if (nextNode != null) {
                prioritizedQueue.add(nextNode);
                clusterHeads.remove(nextNode);
            }
        }
        // 输出最终按优先级排列的节点队列
        System.out.println("按优先级排序后的充电顺序:");
        for (Node node : prioritizedQueue) {
            System.out.println(node.getId());
        }

    }
    // 计算节点的优先级
    public static double calculatePriority(Node node, Node lastAddedNode) {
        double remainingLifeRatio = node.getLifeTime() / maxLifeTime;
        double heightDifferenceRatio = Math.abs(lastAddedNode.getCluster().calculateFlightHeight() - node.getCluster().calculateFlightHeight()) / (maxFlightHeight - minFlightHeight);
        return w1 * (1 - remainingLifeRatio) + w2 * (1 - heightDifferenceRatio);
    }

    // 计算无人机飞行过程中的高度变化能量消耗
    public static double calculateEnergyConsumption(double m, double g, double[] heights) {
        double totalEnergy = 0.0; // 总能量消耗，单位为焦耳 (J)

        // 遍历所有簇，计算相邻簇之间的高度差对应的能量消耗
        for (int i = 0; i < heights.length - 1; i++) {
            double heightDiff = Math.abs(heights[i + 1] - heights[i]);  // 高度差的绝对值
            totalEnergy += m * g * heightDiff;  // 计算该段飞行的能量消耗
        }

        return totalEnergy;
    }
}
