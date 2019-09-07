package mpack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Util {
    public static byte[] rf(File f, int l) throws IOException {
        FileInputStream in = null;
        byte[] data = new byte[l];

        try {
            in = new FileInputStream(f);
            in.read(data);
        } finally {
            if (in != null)
                in.close();
        }

        return data;
    }

    public static void log(String s, int priority){

    }
}
