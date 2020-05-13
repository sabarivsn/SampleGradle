package com.sample;

public class Runner {

    private static final Object syncObj = new Object();

    public static void main(String[] args) {
        try {
            ServiceRunner springRunner = new ServiceRunner("svcThread", syncObj);
            springRunner.start();
            synchronized (syncObj) {
                syncObj.wait();
            }
            try(HttpClient client = new HttpClient("http://localhost:9090/demo")) {
                System.out.println(client.getResponse(false));
            }
            springRunner.stopRunner();
            try(HttpClient client = new HttpClient("http://localhost:9090/actuator/shutdown")) {
                System.out.println(client.getResponse(true));
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
