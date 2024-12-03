import java.util.*;

public class BS {
    static double rho = 16;   //覆盖阈值

    // 判断节点是否在六边形内
    public static boolean contains(Node point, Hexagon hexagon, int r) {
        if (hexagon == null) {
            throw new IllegalArgumentException("传入的 hexagon 为 null");
        }
        List<Node> vertices = hexagon.calculateVertices(r);
        Node p1 = vertices.get(0);
        int n = vertices.size();
        boolean inside = false;
        for (int i = 0; i <= n; i++) {
            Node p2 = vertices.get(i % n);
            // 判断射线是否与边相交
            if (point.getY() > Math.min(p1.getY(), p2.getY())) {
                if (point.getY() <= Math.max(p1.getY(), p2.getY())) {
                    if (point.getX() <= Math.max(p1.getX(), p2.getX())) {
                        // 计算斜率
                        double xIntersection = p1.getX();
                        if (p1.getY() != p2.getY()) {
                            xIntersection += (point.getY() - p1.getY()) * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY());
                        }

                        // 如果斜率不是垂直线，且交点在右侧
                        if (p1.getX() == p2.getX() || point.getX() <= xIntersection) {
                            inside = !inside;
                        }
                    }
                }
            }

            // 更新上一个点
            p1 = p2;

        }

        return inside;
    }

    //每个六边形内覆盖的节点
    public static List<Node> getNodesInHexagons(List<Node> allNodes,Hexagon hexagon,int r) {
        int count = 0;
        // 获取该六边形内的所有节点
        List<Node> nodesInHexagon = new ArrayList<>();
        for (Node node : allNodes) {
           if(contains(node,hexagon,r)){
               nodesInHexagon.add(node); // 添加到六边形的节点列表
               count++;
           }

        }
        hexagon.setNodeCount(count);      //更新六边形内的节点数量
        return nodesInHexagon; // 返回该六边形内的所有节点
    }

    // 找到节点数小于阈值 rho 的六边形
    public static List<Hexagon> findHexagonsBelowThreshold() {
        List<Hexagon> filteredHexagons = new ArrayList<>();
        for (Hexagon hexagon : NetGenerator.hexagons) {
            if (hexagon.getNodeCount() < rho && hexagon.getNodeCount()!=0) { // 检查节点数是否小于阈值
                filteredHexagons.add(hexagon); // 添加到结果列表
            }
        }
        return filteredHexagons; // 返回符合条件的六边形列表
    }

    // 判断两个六边形是否相邻
    private static boolean isNeighbor(Hexagon hex1, Hexagon hex2, int r) {
        double distance = Math.sqrt(Math.pow(hex1.getCenterX() - hex2.getCenterX(), 2) +
                Math.pow(hex1.getCenterY() - hex2.getCenterY(), 2));
        return distance == Math.sqrt(3) * r; // 相邻六边形的距离为根号3倍半径
    }

    // 找到相邻六边形中节点数也小于阈值的六边形个数
    public static void countNeighborsBelowThreshold(List<Hexagon> hexagons,int r) {

        for(Hexagon hexagon : hexagons){
            int count = 0;
            for (Hexagon neighbor : hexagons) {
                if (isNeighbor(hexagon, neighbor,r) ) {
                    count++;
                    hexagon.getNeighbor().add(neighbor);
                }
            }
            hexagon.setNeighborCount(count);
        }
    }

    //找到neighbor最小的六边形
    public static Hexagon findHexagonWithMinNonZeroCount(List<Hexagon> hexagons) {
        Hexagon minHexagon = null;
        int minCount = Integer.MAX_VALUE;
        for (Hexagon hexagon : hexagons) {
            int count = hexagon.getNeighborCount(); // 假设每个六边形的邻居数已存储在邻居属性中
            if (count > 0 && count < minCount) {
                minCount = count;
                minHexagon = hexagon; // 更新最小值和对应的六边形
            }
        }
        if(minHexagon != null){
            return minHexagon; // 返回具有最小非零 count 的六边形
        }
        else{
            System.err.println("稀疏区域为空，无法继续分簇");
            return null; // 或者采取其他措施
        }

    }

    //形成最小圆
    public static List<Cluster> formMinCircle(List<Hexagon> filteredHexagons, List<Hexagon> allHexagons, List<Node> nodes,double maxCluster, int r){
        List<Cluster> clusters = new ArrayList<>();
        List<Hexagon> currentHexagons = new ArrayList<>(filteredHexagons);

        //没有不满足邻居的六边形
        for(Hexagon hexagon : allHexagons){
            if(hexagon.getNeighborCount()==0){
                List<Node> nodes1  = getNodesInHexagons(nodes,hexagon,r);
                Cluster minCluster = MinCircle.findMinCluster(nodes1, maxCluster);
                    clusters.add(minCluster);
            }
        }

        for(Hexagon hexagon : filteredHexagons){
            System.out.printf("包含六边形%d\n",hexagon.getId());
        }
        while (!currentHexagons.isEmpty()) { // 当还有六边形未处理时
            Hexagon currentHexagon = findHexagonWithMinNonZeroCount(currentHexagons); // 获取当前处理的六边形
            List<Hexagon> currentNeighbors = currentHexagon.getNeighbor();     //当前可能与该六边形形成最小圆的邻居
            if(currentNeighbors.isEmpty()){
                System.out.println("无邻居，无法成簇\n");
            }else {
                Hexagon currentNeighbor = findHexagonWithMinNonZeroCount(currentNeighbors);//第一个邻居
                System.out.printf("邻居为%d",currentNeighbor.getId());
                List<Node> allNodes = getNodesInHexagons(nodes,currentHexagon,r);
                allNodes.addAll(getNodesInHexagons(nodes,currentNeighbor,r));
                Cluster minCluster = MinCircle.findMinCluster(allNodes,maxCluster);
                currentNeighbors.remove(currentNeighbor);
                while (minCluster == null && !currentNeighbors.isEmpty()){
                    currentNeighbor = findHexagonWithMinNonZeroCount(currentNeighbors);
                    //两个六边形内的节点
                    allNodes = getNodesInHexagons(nodes,currentHexagon,r);
                    allNodes.addAll(BS.getNodesInHexagons(nodes,currentNeighbor,r));
                    // 使用最小簇算法生成覆盖所有节点的最小簇
                    minCluster = MinCircle.findMinCluster(allNodes,maxCluster);
                    currentNeighbors.remove(currentNeighbor);
                }

                if (minCluster!=null) { // 如果找到符合条件的最小圆簇
                    //移除形成簇的两个六边形
                    currentHexagons.remove(currentNeighbor);
                    filteredHexagons.remove(currentNeighbor);
                    filteredHexagons.remove(currentHexagon);
                    for(Hexagon hexagon : filteredHexagons){
                        hexagon.getNeighbor().remove(currentHexagon);
                        hexagon.getNeighbor().remove(currentNeighbor);
                    }
                    clusters.add(minCluster);// 加入到簇集合中
                    System.out.println("最小簇的中心: (" + minCluster.getCenter().getX() + ", " + minCluster.getCenter().getY() + ")");
                    System.out.println("最小簇的半径: \n" + minCluster.getRadius());
                    System.out.printf("合并的两个六边形为%d,%d。\n",currentHexagon.getId(),currentNeighbor.getId());
                } else {
                    System.out.printf("六边形%d没有符合条件的簇。\n",currentHexagon.getId());
                }
            }
            currentHexagons.remove(currentHexagon);
        }
        //如果还有未成簇的六边形
        if(!filteredHexagons.isEmpty()){
            for(Hexagon hexagon : filteredHexagons){
                    List<Node> nodes1  = getNodesInHexagons(nodes,hexagon,r);
                        Cluster minCluster = MinCircle.findMinCluster(nodes1, maxCluster);
                        if(minCluster!=null){
                            clusters.add(minCluster);
                    }
            }
        }

        //满足阈值的六边形
        for(Hexagon hexagon : NetGenerator.hexagons){
            if(hexagon.getNodeCount()>=rho){
                List<Node> nodes2  = getNodesInHexagons(nodes,hexagon,r);
                Cluster minCluster = MinCircle.findMinCluster(nodes2, maxCluster);
                if(minCluster!=null){
                    clusters.add(minCluster);
                }
            }
        }
        System.out.println("所有六边形都已处理完毕。");
        return clusters;
    }

}
