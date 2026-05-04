package cloudsim.practice.cloudsim_pr3;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Cloudlet;
import java.util.*;

public class PriorityBroker extends DatacenterBroker {

    public PriorityBroker(String name) throws Exception {
        super(name);
    }

    @Override
    protected void submitCloudlets() {

        List<Cloudlet> list = getCloudletList();

        // Sort cloudlets based on priority (higher length = higher priority)
        Collections.sort(list, new Comparator<Cloudlet>() {
            public int compare(Cloudlet c1, Cloudlet c2) {
                return Long.compare(c2.getCloudletLength(), c1.getCloudletLength());
            }
        });

        System.out.println("Executing Cloudlets in Priority Order:");

        for (Cloudlet cloudlet : list) {
            System.out.println("Cloudlet ID: " + cloudlet.getCloudletId()
                    + " Length: " + cloudlet.getCloudletLength());

            sendNow(getVmsToDatacentersMap().get(cloudlet.getVmId()),
                    org.cloudbus.cloudsim.core.CloudSimTags.CLOUDLET_SUBMIT,
                    cloudlet);
        }
    }
}