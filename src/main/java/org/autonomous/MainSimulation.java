package org.autonomous;

import org.autonomous.agent.AutonomousAgent;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;


import java.util.*;

public class MainSimulation {

    public static void main(String[] args) {

        CloudSim sim = new CloudSim();

  
        Host host = new HostSimple(
            16000,     // RAM
            10000,     // BW
            1000000,   // Storage
            List.of(
                new PeSimple(1000),
                new PeSimple(1000),
                new PeSimple(1000),
                new PeSimple(1000)
            )
        );

        Datacenter dc = new DatacenterSimple(sim, List.of(host));
        DatacenterBroker broker = new DatacenterBrokerSimple(sim);

        Vm vm1 = new VmSimple(1000, 1);
        vm1.setRam(4096).setBw(3000);

        Vm vm2 = new VmSimple(1000, 1);
        vm2.setRam(4096).setBw(3000);

        broker.submitVmList(List.of(vm1, vm2));

        AutonomousAgent agent = new AutonomousAgent(broker);

        List<Cloudlet> cloudletList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Cloudlet cloudlet = new CloudletSimple(
                10000,
                1,
                new UtilizationModelDynamic(0.5)
            );
            cloudlet.setSizes(256); 
            cloudletList.add(cloudlet);
        }

        broker.submitCloudletList(cloudletList);

        for (int i = 0; i < cloudletList.size(); i++) {
            Vm vm = (i % 2 == 0) ? vm1 : vm2;
            broker.bindCloudletToVm(cloudletList.get(i), vm);
        }
        

        sim.addOnClockTickListener(e -> {

            List<double[]> metrics = new ArrayList<>();

            for (Vm vm : broker.getVmCreatedList()) {
                double[] m = MetricMonitor.collect(vm);
                metrics.add(m);
            }

            String action = agent.decide(FeatureBuilder.build(metrics));

            for (Vm vm : broker.getVmCreatedList()) {

                double[] m = MetricMonitor.collect(vm);
                double score = agent.predictor.predict(m);

                CSVLogger.log(
                    (int) Math.round(sim.clock()),
                    (int) vm.getId(),
                    m[0], m[1], m[2],
                    score,
                    action
                );
            }
        });
        sim.terminateAt(50);

        sim.start();

        System.out.println("Simulation finished!");
    }
}
