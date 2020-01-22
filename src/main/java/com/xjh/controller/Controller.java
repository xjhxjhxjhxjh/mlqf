package com.xjh.controller;

import com.xjh.pojo.PCB;
import com.xjh.pojo.PCBsQueue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author xjhxjhxjh
 * @date 2019/12/16 8:57
 */
@RestController
public class Controller {

    /**
     *  三个存放进程的队列
     */
    private static PCBsQueue q1 = new PCBsQueue();
    private static PCBsQueue q2 = new PCBsQueue();
    private static PCBsQueue q3 = new PCBsQueue();
    /**
     * 当前时间
     */
    private static double time = 0;
    /**
     * 记录轮转信息
     */
    private static List<PCB> step = new ArrayList<>();
    /**
     * 记录结果
     */
    private static List<PCB> result = new ArrayList<>();

    @RequestMapping("/add")
    public ModelAndView add(ModelAndView modelAndView, PCB pcb) {
        pcb.setLife(pcb.getServiceTime());
        q1.getQueue().add(pcb);
        modelAndView.setViewName("index");
        modelAndView.addObject("PCBs", q1.getQueue());
        return modelAndView;
    }

    @RequestMapping("/compute")
    public ModelAndView compute(ModelAndView modelAndView, double q1Time, double q2Time, double q3Time) {
        if (!q1.getQueue().isEmpty()) {

            // 设置时间片周期
            q1.setTime(q1Time);
            q2.setTime(q2Time);
            q3.setTime(q3Time);
            // 根据到达时间对q1进行排序
            q1.setQueue(new LinkedList<>(q1.getQueue().stream().
                    sorted(Comparator.comparing(PCB::getArriveTime)).collect(toList())));
            time += q1.getQueue().peek().getArriveTime();

            // q1轮转
            while (!q1.getQueue().isEmpty()) {
                PCB pcb = q1.getQueue().poll();
                pcb.setQueueName("队列1");
                // 当q1为执行进程时，执行q2，q3。当有q1有执行进程，立即抢回。
                if (time < pcb.getArriveTime()) {
                    double count = pcb.getArriveTime() - time;
                    count = runQ2(count);
                    count = runQ3(count);
                }
                if (time < pcb.getArriveTime()) {
                    time = pcb.getArriveTime();
                }
                // 设置时间
                pcb.setNow1(time);
                pcb.setStartTime(time);
                double ans = pcb.getLife() - q1.getTime();
                time += ans > 0 ? q1.getTime() : pcb.getLife();
                pcb.setNow2(time);
                if (ans > 0) {
                    pcb.setLife(ans);
                    q2.getQueue().add(pcb);
                } else {
                    pcb.setLife(0);
                    finish(pcb);
                }
                step.add(new PCB(pcb));
            }

            // q2轮转
            while (!q2.getQueue().isEmpty()) {
                doQ2(q2.getTime());
            }

            // q3轮转
            while (!q3.getQueue().isEmpty()) {
                doQ3(q3.getTime());
            }
        }
        modelAndView.addObject("steps", step);
        modelAndView.addObject("results", result);
        modelAndView.setViewName("result");
        q1.setQueue(new LinkedList<>());
        q2.setQueue(new LinkedList<>());
        q3.setQueue(new LinkedList<>());
        result = new ArrayList<>();
        step = new ArrayList<>();
        time = 0;
        return modelAndView;
    }

    private void finish(PCB pcb) {
        pcb.setCircle(pcb.getNow2() - pcb.getStartTime());
        pcb.setWeightedCircle(pcb.getCircle() / pcb.getServiceTime());
        result.add(pcb);
    }

    /**
     * 计算队列2执行count时间的结果
     * @param count
     */
    private double runQ2(double count) {
        while (!q2.getQueue().isEmpty() && count > 0) {
            if (q2.getTime() > count) {
                doQ2(count);
                count = 0;
            } else {
                count -= q2.getTime();
                doQ2(q2.getTime());
            }
        }
        return count;
    }

    /**
     * 计算队列3执行count时间的结果
     * @param count
     */
    private double runQ3(double count) {
        while (!q3.getQueue().isEmpty() && count > 0) {
            if (q3.getTime() > count) {
                doQ3(count);
                count = 0;
            } else {
                count -= q3.getTime();
                doQ3(q3.getTime());
            }
        }
        return count;
    }

    /**
     * 完成一个q2的轮转
     * @param timeA
     */
    private void doQ2(double timeA) {
        if (!q2.getQueue().isEmpty()) {
            PCB pcb = q2.getQueue().poll();
            pcb.setQueueName("队列2");
            double ans = pcb.getLife() - timeA;
            pcb.setNow1(time);
            time += ans > 0 ? timeA : pcb.getLife();
            pcb.setNow2(time);
            if (ans > 0) {
                pcb.setLife(ans);
                q3.getQueue().add(pcb);
            } else {
                pcb.setLife(0);
                finish(pcb);
            }
            step.add(new PCB(pcb));
        }
    }

    /**
     * 完成一个q3轮转
     * @param timeA
     */
    private void doQ3(double timeA) {
        if (!q3.getQueue().isEmpty()) {
            PCB pcb = q3.getQueue().poll();
            pcb.setQueueName("队列3");
            pcb.setNow1(time);
            double ans = pcb.getLife() - timeA;
            time += ans > 0 ? timeA : pcb.getLife();
            pcb.setNow2(time);
            if (ans > 0) {
                pcb.setLife(ans);
                q3.getQueue().add(pcb);
            } else {
                pcb.setLife(0);
                finish(pcb);
            }
            step.add(new PCB(pcb));
        }
    }
}