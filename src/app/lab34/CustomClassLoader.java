package app.lab34;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomClassLoader extends ClassLoader {

    private static CustomClassLoader _instance;

    public static CustomClassLoader get() {
        if (_instance == null)
            _instance = new CustomClassLoader(AppGrphics.class.getClassLoader());

        return _instance;
    }

    public static void unload() {
        _instance = null;
    }


    CustomClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class getClass(String name) {
        String file = name.replace('.', File.separatorChar) + ".class";
        byte[] b;
        try {
            b = loadClassFileData(file);
            Class c = defineClass(name, b, 0, b.length);
            resolveClass(c);
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Class loadClass(String name) throws ClassNotFoundException {
        System.out.println("Loading Class '" + name + "'");
        if (name.startsWith("p")) {
            System.out.println("Loading Class using CustomClassLoader");
            return getClass(name);
        }
        return super.loadClass(name);
    }

    private byte[] loadClassFileData(String name) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
        int size = stream.available();
        byte buff[] = new byte[size];
        DataInputStream in = new DataInputStream(stream);
        in.readFully(buff);
        in.close();
        return buff;
    }
}