/*package org.autonomous.agent;

import org.autonomous.models.EnsemblePredictor;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.vms.VmSimple;

public class AutonomousAgent {

    DatacenterBroker broker;
    public EnsemblePredictor predictor = new EnsemblePredictor();

    private int maxVMs = 5;          
    private double lastScaleTime = -10; 

    public AutonomousAgent(DatacenterBroker broker) {
        this.broker = broker;
    }

    public void decide(double[] features) {

        double score = predictor.predict(features);
        double currentTime = broker.getSimulation().clock();

        

        if (score > 60
                && broker.getVmCreatedList().size() < maxVMs
                && currentTime - lastScaleTime > 10) {

            VmSimple vm = new VmSimple(1000, 1);
            vm.setRam(1024).setBw(1000);

            broker.submitVm(vm);

            System.out.println("Agent: SCALE OUT at time " + currentTime);

            lastScaleTime = currentTime; 
        }
    }
}
package org.autonomous.agent;

import org.autonomous.models.EnsemblePredictor;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.vms.VmSimple;

import java.util.*;

public class AutonomousAgent {

    DatacenterBroker broker;
    public EnsemblePredictor predictor = new EnsemblePredictor();

    private int maxVMs = 5;
    private double lastScaleTime = -10;

    private List<Double> history = new ArrayList<>();

    public AutonomousAgent(DatacenterBroker broker) {
        this.broker = broker;
    }

    public void decide(double[] features) {

        double currentScore = predictor.predict(features);
        double time = broker.getSimulation().clock();
        int vmCount = broker.getVmCreatedList().size();

        history.add(currentScore);
        if (history.size() > 5) history.remove(0);

        double futureScore = history.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(currentScore);


        double avg = futureScore;
        double upperThreshold = avg + 10;
        double lowerThreshold = avg - 10;

        System.out.println("Current: " + currentScore + 
                           " | Future: " + futureScore);

        if (futureScore > upperThreshold &&
                vmCount < maxVMs &&
                time - lastScaleTime > 10) {

            VmSimple vm = new VmSimple(1000, 1);
            vm.setRam(2048).setBw(1000);

            broker.submitVm(vm);

            System.out.println("Agent: SCALE OUT");
            lastScaleTime = time;
        }

        else if (futureScore < lowerThreshold &&
                vmCount > 1 &&
                time - lastScaleTime > 10) {

            VmSimple vmToRemove =
                    (VmSimple) broker.getVmCreatedList().get(vmCount - 1);

            vmToRemove.getHost().destroyVm(vmToRemove);

            System.out.println("Agent: SCALE IN");
            lastScaleTime = time;
        }
    }
}
package org.autonomous.agent;

import org.autonomous.models.EnsemblePredictor;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.vms.VmSimple;

import java.util.*;

public class AutonomousAgent {

    DatacenterBroker broker;
    public EnsemblePredictor predictor = new EnsemblePredictor();

    private int maxVMs = 5;
    private double lastScaleTime = -10;

    private List<Double> history = new ArrayList<>();

    public AutonomousAgent(DatacenterBroker broker) {
        this.broker = broker;
    }

    public String decide(double[] features) {

        double currentScore = predictor.predict(features);
        double time = broker.getSimulation().clock();
        int vmCount = broker.getVmCreatedList().size();

        // store history
        history.add(currentScore);
        if (history.size() > 5) history.remove(0);

        // future prediction
        double futureScore = history.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(currentScore);

        // adaptive thresholds
        double avg = history.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(currentScore);

        double upperThreshold = avg + 2;
        double lowerThreshold = avg - 2;

        System.out.println(
            "Future: " + futureScore +
            " | Upper: " + upperThreshold +      
            " | Lower: " + lowerThreshold
        );

        System.out.println("Time: " + time +
                " | Current: " + currentScore +
                " | Future: " + futureScore);

        // SCALE OUT
        if (futureScore > upperThreshold &&
                vmCount < maxVMs &&
                time - lastScaleTime > 2) {

            VmSimple vm = new VmSimple(1000, 1);
            vm.setRam(2048).setBw(1000);

            broker.submitVm(vm);

            System.out.println("SCALE OUT");

            lastScaleTime = time;
            return "SCALE_OUT";
        }

        // SCALE IN
        else if (futureScore < lowerThreshold &&
                vmCount > 1 &&
                time - lastScaleTime > 5) {

            VmSimple vmToRemove =
                    (VmSimple) broker.getVmCreatedList().get(vmCount - 1);

            vmToRemove.getHost().destroyVm(vmToRemove);

            System.out.println("SCALE IN");

            lastScaleTime = time;
            return "SCALE_IN";
        }

        return "NONE";
    }
}
*/
package org.autonomous.agent;

import org.autonomous.models.EnsemblePredictor;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.vms.VmSimple;

import java.util.*;

public class AutonomousAgent {

    DatacenterBroker broker;
    public EnsemblePredictor predictor = new EnsemblePredictor();

    private int maxVMs = 3;
    private double lastScaleTime = -10;

    // store past scores
    private List<Double> history = new ArrayList<>();

    public AutonomousAgent(DatacenterBroker broker) {
        this.broker = broker;
    }

    public String decide(double[] features) {

        double currentScore = predictor.predict(features);
        double time = broker.getSimulation().clock();
        int vmCount = broker.getVmCreatedList().size();
        System.out.println("VM Count: " + vmCount);

    
        history.add(currentScore);
        if (history.size() > 3) history.remove(0);

  
        double futureScore = history.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(currentScore);

    
        double avg = history.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(currentScore);

        double diff = (currentScore - avg)*2;

        System.out.println(
            "Time: " + time +
            " | Current: " + currentScore +
            " | Avg: " + avg +
            " | Diff: " + diff
        );

        if (diff > 0.3 &&
                vmCount < maxVMs &&
                time - lastScaleTime > 0.4) {

            VmSimple vm = new VmSimple(1000, 1);
            vm.setRam(2048).setBw(1000);

            broker.submitVm(vm);

            System.out.println("SCALE OUT");

            lastScaleTime = time;
            return "SCALE_OUT";
        }

        else if (diff < -0.1 &&
                vmCount > 1 &&
                time - lastScaleTime > 0.4) {

            VmSimple vmToRemove =
                    (VmSimple) broker.getVmCreatedList().get(vmCount - 1);

            vmToRemove.getHost().destroyVm(vmToRemove);

            System.out.println("SCALE IN");

            lastScaleTime = time;
            return "SCALE_IN";
        }

        return "NONE";
    }
}