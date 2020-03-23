/**
 * 
 */
package com.xpzheng.watermark.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
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
            print(exec.getInputStream(), false);
            print(exec.getErrorStream(), true);

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
    
    private static final void print(InputStream in, boolean error) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "gbk"));
        String line = null;
        while ((line = br.readLine()) != null) {
            PrintStream ps = error ? System.err : System.out;
            ps.println(line);
        }
        br.close();
    }

}
