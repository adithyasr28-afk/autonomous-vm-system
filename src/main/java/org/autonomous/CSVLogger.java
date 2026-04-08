package org.autonomous;

import java.io.FileWriter;

/*public class CSVLogger {

    private static FileWriter writer;

    static {
        try {
            writer = new FileWriter("metrics.csv");
            writer.write("Time,VM,CPU,RAM,BW,EnsembleScore\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(int time, int vm, double cpu, double ram, double bw, double score) {
        try {
            writer.write(time + "," + vm + "," + cpu + "," + ram + "," + bw + "," + score + "\n");
            writer.flush();
        } catch (Exception e) {}
    }
}

public class CSVLogger {

    public static void log(int time, int vmId,
                           double cpu, double ram, double bw,
                           double score, String action) {

        try (FileWriter fw = new FileWriter("metrics.csv", true)) {

            fw.append(time + "," + vmId + "," +
                      cpu + "," + ram + "," + bw + "," +
                      score + "," + action + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    */

import java.io.File;
import java.io.FileWriter;

public class CSVLogger {

    private static boolean headerWritten = false;

    public static void log(int time, int vmId,
                           double cpu, double ram, double bw,
                           double score, String action) {

        try {
            File file = new File("metrics.csv");

            // write header only once
            if (!file.exists() || !headerWritten) {
                FileWriter fw = new FileWriter(file, true);
                fw.append("Time,VM,CPU,RAM,BW,Score,Action\n");
                fw.close();
                headerWritten = true;
            }

            FileWriter fw = new FileWriter(file, true);

            fw.append(time + "," + vmId + "," +
                      cpu + "," + ram + "," + bw + "," +
                      score + "," + action + "\n");

            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}