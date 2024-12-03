public class UAV {
        // 无人机当前位置坐标
        private double x;
        private double y;
        private double height;

        // 无人机能量相关参数
        private double totalEnergy;    // 总能量
        private double remainEnergy;   // 剩余能量
        private double minEnergy;      // 最小能量阈值

        // 无人机飞行参数
        private double speed;          // 飞行速度
        private double maxHeight;      // 最大飞行高度
        private double minHeight;      // 最小飞行高度
        private double chargePower;    // 充电功率Ps
        private double maxChargeRadius; // 最大充电半径 dmax

        public UAV(double initX, double initY, double initHeight, double totalEnergy,
                   double speed, double chargePower, double maxChargeRadius) {
            this.x = initX;
            this.y = initY;
            this.height = initHeight;
            this.totalEnergy = totalEnergy;
            this.remainEnergy = totalEnergy;
            this.speed = speed;
            this.chargePower = chargePower;
            this.maxChargeRadius = maxChargeRadius;
            this.minEnergy = totalEnergy * 0.1; // 设置最小能量阈值为总能量的10%
        }

        // 计算飞行功率
       /* public double calculateFlyingPower(double speed) {
            double term1 = P0 * (1 + 3 * Math.pow(speed, 2) / Math.pow(Utip, 2));
            double term2 = Pi * Math.pow(Math.sqrt(1 + Math.pow(speed, 4)/(4 * Math.pow(v0, 4)))
                    - Math.pow(speed, 2)/(2 * Math.pow(v0, 2)), 0.5);
            double term3 = 0.5 * d0 * rho * s * A * Math.pow(speed, 3);
            return term1 + term2 + term3;
        }*/

        // 计算悬停功率
        /*public double calculateHoverPower() {
            return P0 + Pi;
        }*/

        // 移动到新位置
        public void moveTo(double newX, double newY, double newHeight) {
            // 计算移动距离
            double distance = Math.sqrt(Math.pow(newX - x, 2) + Math.pow(newY - y, 2)
                    + Math.pow(newHeight - height, 2));
            // 计算移动时间
            double flyTime = distance / speed;
            // 计算能量消耗
          //  double energyCost = calculateFlyingPower(speed) * flyTime;
            // 更新位置和能量
            this.x = newX;
            this.y = newY;
            this.height = newHeight;
           // this.remainEnergy -= energyCost;
        }

        // 进行充电
        public double charge(double distance, double chargeTime) {
            if (distance > maxChargeRadius) {
                return 0;
            }
            // 计算充电能量消耗
            double energyCost = chargePower * chargeTime;
            // 更新剩余能量
            this.remainEnergy -= energyCost;
            // 返回节点实际接收到的能量
            return calculateReceivedPower(distance) * chargeTime;
        }

        // 计算节点接收到的功率
        private double calculateReceivedPower(double distance) {
            double alpha = 0.8; // 充电效率系数
            double beta = 0.4;  // 环境衰减系数
            return alpha * chargePower / Math.pow(distance + beta, 2);
        }

        // Getters and Setters
        public double getX() { return x; }
        public double getY() { return y; }
        public double getHeight() { return height; }
        public double getRemainEnergy() { return remainEnergy; }
        public double getSpeed() { return speed; }
        public double getChargePower() { return chargePower; }
        public double getMaxChargeRadius() { return maxChargeRadius; }
        public boolean hasEnoughEnergy() { return remainEnergy > minEnergy; }
    }



