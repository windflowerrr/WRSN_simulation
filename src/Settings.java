import java.util.List;

public class Settings {
    private static final double MIN_CONSUMPTION_RATE = 0.3;  //簇头节点最小能耗率
    private static final double MAX_CONSUMPTION_RATE = 1.0;  //簇头节点最大能耗率

    public static double nodeBatteryCapacity = 2000;   //节点电池容量

    // 为每个簇头节点分配能耗率
    public static void allocateConsumptionRates(List<Cluster> clusters) {
        // 计算最大和最小簇内节点数
        int maxClusterSize = 0;
        int minClusterSize = Integer.MAX_VALUE;;
        for (Cluster cluster : clusters) {
            maxClusterSize = Math.max(maxClusterSize, cluster.getNodeCount());
            minClusterSize = Math.min(minClusterSize, cluster.getNodeCount());
        }

        for (Cluster cluster : clusters) {
            int clusterSize = cluster.getNodeCount();
            double consumptionRate;
            double divisor = maxClusterSize - minClusterSize;
            if (divisor == 0) {
                // 如果所有簇内节点数都相等，直接设置为中间值
                consumptionRate = (MIN_CONSUMPTION_RATE + MAX_CONSUMPTION_RATE) / 2;
            }else {
                // 计算能耗率，映射到范围 [MIN_CONSUMPTION_RATE, MAX_CONSUMPTION_RATE]
                consumptionRate = MIN_CONSUMPTION_RATE + (MAX_CONSUMPTION_RATE - MIN_CONSUMPTION_RATE) *
                        ((double)(clusterSize - minClusterSize) / (maxClusterSize - minClusterSize));
            }
            cluster.getClusterHead().setEnergyConsumptionRate(consumptionRate);
            //计算簇头节点生存时长
            double life = (nodeBatteryCapacity-nodeBatteryCapacity*0.1)/consumptionRate;
            cluster.getClusterHead().setLifeTime(life);
        }
    }

    // 根据簇的半径生成充电效率
    public static double generateChargingEfficiency(double radius) {
        // 随机生成一个在 0.5% 到 1.5% 之间的充电效率
        double randomEfficiency = 0.5 + (Math.random() * (1.5 - 0.5)); // 生成 0.5 到 1.5 之间的随机数
        // 根据簇的半径调整充电效率，假设半径越大，充电效率越低
        // radius 与充电效率成反比，可以调整系数来改变影响
        return randomEfficiency / (1 + radius * 0.1); // radius 越大，充电效率越小，系数0.1可以调整影响的强度
    }

}
