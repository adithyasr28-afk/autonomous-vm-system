/*package org.autonomous;

import org.cloudbus.cloudsim.vms.Vm;

public class MetricMonitor {

    public static double[] collect(Vm vm) {

        double time = vm.getSimulation().clock();

        // Simulated dynamic workload
        double cpu = (Math.sin(time) + 1) * 50;   // 0–100 varying
        double ram = (Math.cos(time) + 1) * 50;
        double bw  = (Math.sin(time / 2) + 1) * 50;

        return new double[]{cpu, ram, bw};
    }
}*/
package org.autonomous;

import org.cloudbus.cloudsim.vms.Vm;

public class MetricMonitor {

    public static double[] collect(Vm vm) {

        double time = vm.getSimulation().clock();

        double cpu = (Math.sin(time / 2) + 1) * 50;
        double ram = (Math.cos(time / 2) + 1) * 50;
        double bw  = (Math.sin(time / 3) + 1) * 50;

        return new double[]{cpu, ram, bw};
    }
}