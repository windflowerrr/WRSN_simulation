import java.io.IOException;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        int count = 50;
        int maxR = 100;
        int r = 30;
        int rho = 16;
        double maxCluster = 30;   //允许的最大簇半径
       //NetGenerator.generateRandomNodes(count, maxR);
        List<Node> nodes = new ArrayList<>();
        try {
            // 文件路径
            String filePath = "C:/data/NodeData.txt";
            // 从文件生成节点
             nodes = NetGenerator.generateNodesFromFile(filePath);

            // 输出生成的节点
          /*  System.out.println("生成的节点列表:");
            for (Node node : nodes) {
                System.out.println("节点 ID: " + node.getId() + ", 坐标: (" + node.getX() + ", " + node.getY() + ")\n");
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
      System.out.println("节点坐标为：");
        for (Node node : nodes) {
            System.out.printf("(%f,%f，序号：%d)\n",node.getX(),node.getY(),node.getId());
        }
        NetGenerator.HexagonStorage(r,maxR);
     for (Hexagon hexagon : NetGenerator.hexagons) {
            System.out.printf("第%d个六边形的中心坐标为(%f,%f)\n",hexagon.getId(),hexagon.getCenterX(),hexagon.getCenterY());
        }
      //int sum = 0;
      for (Hexagon hexagon : NetGenerator.hexagons){
          List<Node> nodesInHexagon = BS.getNodesInHexagons(nodes,hexagon,r);
          if(hexagon.getNodeCount()!=0){
              System.out.printf("第%d个六边形内有%d个节点\n",hexagon.getId(),hexagon.getNodeCount());
          }
          for (Node node : nodesInHexagon) {
              System.out.printf("节点序号为%d\n",node.getId());
          }
         /* sum+=hexagon.getNodeCount();
          System.out.printf("节点个数为：%d\n",sum);*/
        }

        List<Node> uncoveredNodes = new ArrayList<>();

        for (Node node : nodes) {
            boolean isCovered = false;
            // 检查节点是否在任意一个六边形内
            for (Hexagon hexagon : NetGenerator.hexagons) {
                if (BS.contains(node,hexagon,r)) {
                    isCovered = true;
                    break; // 一旦被覆盖，无需继续检查其他六边形
                }
            }

            // 如果节点未被覆盖，加入到未覆盖节点列表
            if (!isCovered) {
                uncoveredNodes.add(node);
            }
        }
        for(Node node : uncoveredNodes){
            System.out.printf("节点%d未被覆盖\n",node.getId());
        }

      List<Hexagon> filteredHexagons = new ArrayList<>();
      filteredHexagons = BS.findHexagonsBelowThreshold();  //不满足区域
       for (Hexagon hexagon : filteredHexagons) {
            System.out.printf("不满足区域为六边形%d\n",hexagon.getId());
        }

        BS.countNeighborsBelowThreshold(filteredHexagons,r);  //计算相邻六边形中也为不满足区域的个数
        List<Hexagon> haveNeighborHexagons = new ArrayList<>();  //个数不为0的不满足区域
        for (Hexagon hexagon : filteredHexagons) {
            if(hexagon.getNeighborCount()!=0 ){
                haveNeighborHexagons.add(hexagon);
            }
            else {
                System.out.printf("六边形%d没有不满足邻居\n",hexagon.getId());
            }
            //System.out.printf("第%d个六边形的neighbor个数为%d\n",hexagon.getId(),hexagon.getNeighborCount());
        }

        // 打印 haveNeighborHexagons 的大小和内容
        System.out.println("有邻居的六边形数量: " + haveNeighborHexagons.size());
        for (Hexagon hex : haveNeighborHexagons) {
            List<Hexagon> currentNeighbors = hex.getNeighbor();     //当前可能与该六边形形成最小圆的邻居
            for(Hexagon hexagon : currentNeighbors){
                System.out.printf("六边形%d的邻居：Hexagon ID:%d\n" , hex.getId(),hexagon.getId());
            }
        }

        List<Cluster>  clusters = new ArrayList<>();
        if(!haveNeighborHexagons.isEmpty()){
           /* if (haveNeighborHexagons == null || haveNeighborHexagons.isEmpty()) {
                throw new IllegalStateException("haveNeighborHexagons 列表为空");
            }*/

            /*for (Hexagon hexagon : haveNeighborHexagons) {
                if (hexagon == null) {
                    throw new IllegalStateException("haveNeighborHexagons 列表包含 null 元素");
                }
            }*/
            clusters = BS.formMinCircle(haveNeighborHexagons, filteredHexagons, nodes, maxCluster, r);
            for(Hexagon hexagon : haveNeighborHexagons){
                System.out.printf("haveNeighborHexagons中包含六边形%d\n",hexagon.getId());
            }
            Settings.allocateConsumptionRates(clusters);
            for (Cluster cluster : clusters){
                System.out.printf("该簇的簇中心为（%f,%f),",cluster.getCenter().getX(),cluster.getCenter().getY());
                System.out.printf("充电效率为%f,",Settings.generateChargingEfficiency(cluster.getRadius()));
                System.out.printf("簇头节点为节点%d,簇头节点的能耗率为：%f,簇半径为%f\n",cluster.getClusterHead().getId(),cluster.getClusterHead().getEnergyConsumptionRate(),cluster.getRadius());
            }
            Charge.chargeQueue(clusters);
        }else {
            System.out.println("无法执行最小圆算法");
        }

        List<Node> centers = TSP.getClusterCenters(clusters); // 获取簇的中心节点
        double[][] distanceMatrix = TSP.buildDistanceMatrix(centers); // 构建距离矩阵
        // 选择 TSP 求解方法：动态规划或贪心
        double minDistance = TSP.solveTSP(distanceMatrix);
        System.out.println("最短路径长度: " + minDistance);
        double []heights = {41.6,31.4,46.6,48.8,5.4,21.6};
        // 无人机的质量和重力加速度
        double m = 1.5;  // 无人机的质量 (kg)
        double g = 9.81; // 重力加速度 (m/s^2)
        // 计算总能量消耗
        double totalEnergy = Charge.calculateEnergyConsumption(m, g, heights);

        // 输出结果
        System.out.println("Total energy consumption due to height changes: " + totalEnergy + " J");

    }

    }
