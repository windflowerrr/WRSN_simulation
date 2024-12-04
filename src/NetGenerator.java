import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetGenerator {
    private Node node;


    // 生成随机节点列表
    public static List<Node> nodes = new ArrayList<>();  // 初始化 List

    public static List<Node> generateNodesFromFile(String filePath) throws IOException {
        List<Node> nodes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int id = 1; // 节点 ID 从 1 开始

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(","); // 假设用逗号分隔
            double x = Double.parseDouble(parts[0].trim());
            double y = Double.parseDouble(parts[1].trim());
            nodes.add(new Node(id++, x, y)); // 创建节点
        }

        reader.close();
        return nodes;
    }

//    public static List<Node> generateRandomNodes(int count, double maxR) {   // maxR为网络长度的一半(150)
//
//
//        Random random = new Random();
//
//        for (int i = 0; i < count; i++) {
//          /*  // 生成随机角度和半径
//            double theta = 2 * Math.PI * random.nextDouble(); // 随机角度
//            double radius = Math.sqrt(random.nextDouble()) * maxR; // 随机半径
//            // 计算x和y坐标
//            double x = radius * Math.cos(theta);
//            double y = radius * Math.sin(theta); */
//            double x = random.nextDouble() * maxR;
//            double y = random.nextDouble() * maxR;
//            nodes.add(new Node(i + 1, x, y));           // 创建新节点并添加到列表
//        }
//
//        return nodes;
//    }

    //计算每个正六边形中心坐标
    public static List<Hexagon> hexagons = new ArrayList<>();
    public static List<Hexagon> HexagonStorage(int r,int maxR) {  //r为正六边形边长
        int i = 0;
        double x;
        double y;
        int n;
        while (3.0/2 * r * i -r< maxR){
            i++;
            if(i%2 == 0){   //偶数列六边形
                x = 0;
                y = 0;
                n = 1;
                while (y + Math.sqrt(3)/2*r + Math.sqrt(3)*(n-1)*r-Math.sqrt(3)/2*r <= maxR){
                    x = 3.0/2 * r * (i-1);
                    y = y + Math.sqrt(3)/2*r + Math.sqrt(3)*(n-1)*r;
                    hexagons.add(new Hexagon(x, y));
                    if(x!=0 && y!=0){
                        //对称的六边形中心坐标
                        hexagons.add(new Hexagon(x, -y));
                        hexagons.add(new Hexagon(-x, y));
                        hexagons.add(new Hexagon(-x, -y));
                    } else if (x==0 && y!=0) {
                        hexagons.add(new Hexagon(0, -y));
                    } else if (x!=0 && y==0) {
                        hexagons.add(new Hexagon(-x, 0));
                    }

                    n++;
                }
            }
            else {     //奇数列六边形
                x = 0;
                y = 0;
                n = 1;
                while (Math.sqrt(3)*(n-1)*r-Math.sqrt(3)/2*r <= maxR){
                    x = 3.0/2 * r * (i-1);
                    y = Math.sqrt(3)*(n-1)*r;
                    hexagons.add(new Hexagon(x, y));
                    if(x!=0 && y!=0){
                        //对称的六边形中心坐标
                        hexagons.add(new Hexagon(x, -y));
                        hexagons.add(new Hexagon(-x, y));
                        hexagons.add(new Hexagon(-x, -y));
                    } else if (x==0 && y!=0) {
                        hexagons.add(new Hexagon(0, -y));
                    } else if (x!=0 && y==0) {
                        hexagons.add(new Hexagon(-x, 0));
                    }
                    n++;
                }
            }

        }
        return hexagons;
    }


}
