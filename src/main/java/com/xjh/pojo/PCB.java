package com.xjh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xjhxjhxjh
 * @date 2019/12/16 8:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PCB {

    /**
     * 到达时间
     */
    private double arriveTime;

    /**
     * 服务时间
     */
    private double serviceTime;

    /**
     * 剩余服务时间
     */
    private double life;

    /**
     * 进程名
     */
    private String name;

    /**
     * 当前时间间隔
     */
    private double now1;
    private double now2;

    /**
     * 开始时间
     */
    private double startTime;

    /**
     * 所处队列名
     */
    private String queueName;

    /**
     * 周期
     */
    private double circle;

    /**
     * 带权周期
     */
    private double weightedCircle;

    public PCB(PCB pcb) {
        this.queueName = pcb.getQueueName();
        this.life = pcb.life;
        this.name = pcb.name;
        this.now1 = pcb.now1;
        this.now2 = pcb.now2;
        this.arriveTime = pcb.arriveTime;
    }
}
