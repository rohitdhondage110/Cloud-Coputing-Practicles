package cloudsim.practice.cloudsim_pr3;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.*;

public class FirstSimulation {

    public static void main(String[] args) {

        try {
            int numUser = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;

            // Initialize CloudSim
            CloudSim.init(numUser, calendar, traceFlag);

            // Create Datacenter
            Datacenter datacenter0 = createDatacenter("Datacenter_0");

            // Use Custom Broker
            PriorityBroker broker = new PriorityBroker("Broker");
            int brokerId = broker.getId();

            // Create VM
            List<Vm> vmlist = new ArrayList<>();

            Vm vm = new Vm(0, brokerId, 1000, 1, 512, 1000, 1000, "Xen",
                    new CloudletSchedulerTimeShared());

            vmlist.add(vm);
            broker.submitVmList(vmlist);

            // Create multiple Cloudlets (tasks)
            List<Cloudlet> cloudletList = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                Cloudlet cloudlet = new Cloudlet(i,
                        1000 + i * 500, // different lengths = priority
                        1,
                        300,
                        300,
                        new UtilizationModelFull(),
                        new UtilizationModelFull(),
                        new UtilizationModelFull());

                cloudlet.setUserId(brokerId);
                cloudlet.setVmId(0);

                cloudletList.add(cloudlet);
            }

            broker.submitCloudletList(cloudletList);

            // Start Simulation
            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            // Print Results
            List<Cloudlet> result = broker.getCloudletReceivedList();

            System.out.println("\nFinal Output:");

            for (Cloudlet c : result) {
                System.out.println("Cloudlet ID: " + c.getCloudletId()
                        + " | Status: " + c.getStatus()
                        + " | Time: " + c.getActualCPUTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Datacenter createDatacenter(String name) {

        List<Host> hostList = new ArrayList<>();

        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(1000)));

        hostList.add(new Host(0,
                new RamProvisionerSimple(2048),
                new BwProvisionerSimple(10000),
                1000000,
                peList,
                new VmSchedulerTimeShared(peList)));

        DatacenterCharacteristics characteristics =
                new DatacenterCharacteristics("x86", "Linux", "Xen",
                        hostList, 10.0, 3.0, 0.05, 0.001, 0.0);

        try {
            return new Datacenter(name, characteristics,
                    new VmAllocationPolicySimple(hostList),
                    new LinkedList<>(), 0);
        } catch (Exception e) {
            return null;
        }
    }
}