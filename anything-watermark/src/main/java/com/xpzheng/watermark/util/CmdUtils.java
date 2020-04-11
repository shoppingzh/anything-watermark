package com.xpzheng.watermark.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 命令行执行工具集合
 * 
 * @author xpzheng
 *
 */
public class CmdUtils {

    private static Logger LOG = LoggerFactory.getLogger(CmdUtils.class);

    /**
     * 执行某个命令
     * 
     * @param cmd
     * @return
     * @throws IOException
     */
    public static final int execute(String cmd) throws Exception {
        Process exec = null;
        try {
            exec = Runtime.getRuntime().exec(cmd);
            // 消耗error输入流的数据
            new Thread(new InputStreamCustomer(exec.getErrorStream())).start();
            // 消耗return输入流的数据
            new Thread(new InputStreamCustomer(exec.getInputStream())).start();
            return exec.waitFor();
        } finally {
            if (exec != null) {
                exec.destroy();
            }
        }
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
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
