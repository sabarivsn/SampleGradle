package com.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServiceRunner extends Thread {
    private Thread svcThread;
    private Process svcProcess;
    private String threadName;
    private final Object syncObj;

    ServiceRunner(String name, Object syncObj) {
        this.threadName = name;
        this.syncObj = syncObj;
        System.out.println("Creating thread " + name);
    }

    public void run() {
        try {
            svcProcess = Runtime.getRuntime().exec("cmd /c SpringRunner.bat");
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(svcProcess.getInputStream()));
            String line;
            final String STARTED_STRING = "Started SpringBootDemoApplication";
            while((line = reader.readLine()) != null) {
                if(line.contains(STARTED_STRING)) {
                    synchronized (syncObj) {
                        syncObj.notify();
                    }
                }
                System.out.println(line);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Started service thread");
        if(svcThread == null) {
            svcThread = new Thread(this, threadName);
            svcThread.start();
        }
    }

    void stopRunner() {
        if(svcProcess.isAlive()) {
            System.out.println("Stopping service...");
            svcProcess.destroyForcibly();
        }
    }
}
