public class Node {
    private int id;
    private double x;        // 节点的x坐标
    private double y;        // 节点的y坐标

    private double currentEnergy;   //节点当前能量

    private double energyConsumptionRate;  //节点能耗率

    private double lifeTime;  //节点生存时长

    private Cluster cluster; // 簇头节点所属的簇

    public Node(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public Node(int id) {
        this.id = id;
    }


    public int getId(){
        return id;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getCurrentEnergy() {
        return currentEnergy;
    }

    public void setEnergyConsumptionRate(double energyConsumptionRate) {
        this.energyConsumptionRate = energyConsumptionRate;
    }

    public double getEnergyConsumptionRate() {
        return energyConsumptionRate;
    }
    public void setLifeTime(double lifeTime) {
        this.lifeTime = lifeTime;
    }
    public double getLifeTime() {
        return lifeTime;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Cluster getCluster() {
        return cluster;
    }


    @Override
    public String toString() {
        return String.format("Node{ID=%d, x=%.2f, y=%.2f}",id, x, y);
    }
}
