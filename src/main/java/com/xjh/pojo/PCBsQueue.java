package com.xjh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author xjhxjhxjh
 * @date 2019/12/16 8:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PCBsQueue {
    /**
     * 队列时间片
     */
    private double time;

    /**
     * 存储进程的队列
     */
    private Queue<PCB> queue = new LinkedList<>();

    /**
     * 队列名
     */
    private String name;
}
