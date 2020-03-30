package com.xpzheng.watermark.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 命令行执行工具集合
 * 
 * @author xpzheng
 *
 */
public class CmdUtils {

    /**
     * 执行某个命令
     * 
     * @param cmd
     * @return
     */
    public static final int execute(String cmd) {
        Process exec = null;
        try {
            exec = Runtime.getRuntime().exec(cmd);
            // 消耗error输入流的数据
            new Thread(new InputStreamCustomer(exec.getErrorStream())).start();
            // 消耗return输入流的数据
            new Thread(new InputStreamCustomer(exec.getInputStream())).start();
            return exec.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (exec != null) {
                exec.destroy();
            }
        }

        return -1;
    }

    /**
     * 输入流消费者
     * 
     * @author xpzheng
     *
     */
    private static class InputStreamCustomer implements Runnable {

        private InputStream in;

        public InputStreamCustomer(InputStream in) {
            super();
            this.in = in;
        }

        @Override
        public void run() {
            try (InputStreamReader ir = new InputStreamReader(in); BufferedReader br = new BufferedReader(ir)) {
                while (br.readLine() != null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
