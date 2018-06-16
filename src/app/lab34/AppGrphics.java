package app.lab34;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.awt.image.BufferedImage;

public class AppGrphics extends SwingWorker
{
    JFrame frame;
    JPanel panel;
    JPanel panel2;
    JButton pathButton;
    JButton loadButton;
    JButton unloadButton;
    JButton blurButton;
    JButton negativeButton;
    JButton rotateButton;
    JButton unblurButton;
    JSplitPane split;
    JScrollPane scroll;
    JToolBar toolBar;
    Loader loader= new Loader();
    static String path = "photos";
    List<JButton> list;
    CustomClassLoader classloader = new CustomClassLoader(AppGrphics.class.getClassLoader());
    Object objRotate;
    Object objBlur;
    Object objunBlur;
    Object objNeagtive;
    public AppGrphics(final String[] av) throws Exception {
        initGui(av);
    }

    void initGui(final String[] av) throws Exception {
        panel = new JPanel();
        panel2 = new JPanel();
        pathButton = new JButton("Wgraj");
        pathButton.addActionListener(e->{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File((System.getProperty("user.home"))));
            if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
            {
                this.path = fileChooser.getCurrentDirectory().getPath();
                try {
                    doInBackground();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        loadButton = new JButton("Załaduj");
        loadButton.addActionListener(e ->{
            try
            {
                objBlur=classloader.loadClass("p.Blur").newInstance();
                objunBlur=classloader.loadClass("p.unBlur").newInstance();
                objNeagtive=classloader.loadClass("p.Negative").newInstance();
                objRotate = classloader.loadClass("p.Rotate").newInstance();
            }
            catch(ClassNotFoundException e1)
            {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            }
            loadButton.setVisible(false);
            unloadButton.setVisible(true);
        });
        unloadButton = new JButton("Wyładuj");
        unloadButton.setVisible(false);
        unloadButton.addActionListener(e ->{
            CustomClassLoader.unload();
            System.out.println("Unload Class");
            unloadButton.setVisible(false);
            loadButton.setVisible(true);
        });
        rotateButton = new JButton("Rotacja");
        rotateButton.addActionListener((ActionEvent e) ->{
            try {
                Method method= objRotate.getClass().getMethod("rotateCw",BufferedImage.class);
                for(int i =0 ;i<list.size();i++)
                {
                    ImageIcon icon = (ImageIcon) list.get(i).getIcon();
                    Image im = icon.getImage();
                    BufferedImage bf = imageToBufferedImage(im);
                    BufferedImage resoult = (BufferedImage) method.invoke(objRotate,bf);
                    list.get(i).setIcon(new ImageIcon(resoult));
                }
                toolBar.repaint();
                panel2.repaint();
                CustomClassLoader.unload();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }   catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        });
        negativeButton = new JButton("Negatyw");
        negativeButton.addActionListener(e -> {
            try {
                Method method = objNeagtive.getClass().getMethod("Negative", BufferedImage.class);
                for(int i =0 ;i<list.size();i++)
                {
                    ImageIcon icon = (ImageIcon) list.get(i).getIcon();
                    Image im = icon.getImage();
                    BufferedImage bf = imageToBufferedImage(im);
                    BufferedImage resoult = (BufferedImage) method.invoke(objNeagtive,bf);
                    list.get(i).setIcon(new ImageIcon(resoult));
                }
                toolBar.repaint();
                panel2.repaint();
            }  catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        });
        blurButton = new JButton("Blur");
        blurButton.addActionListener(e->{
            try {

                Method method = objBlur.getClass().getMethod("Blur", BufferedImage.class);
                for(int i =0 ;i<list.size();i++)
                {
                    ImageIcon icon = (ImageIcon) list.get(i).getIcon();
                    Image im = icon.getImage();
                    BufferedImage bf = imageToBufferedImage(im);
                    BufferedImage resoult = (BufferedImage) method.invoke(objBlur,bf);
                    list.get(i).setIcon(new ImageIcon(resoult));
                }
                toolBar.repaint();
                panel2.repaint();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        });
        unblurButton = new JButton("unBlur");
        unblurButton.addActionListener(e->{
            try {

                Method method = objunBlur.getClass().getMethod("unBlur", BufferedImage.class);
                for(int i =0 ;i<list.size();i++)
                {
                    ImageIcon icon = (ImageIcon) list.get(i).getIcon();
                    Image im = icon.getImage();
                    BufferedImage bf = imageToBufferedImage(im);
                    BufferedImage resoult = (BufferedImage) method.invoke(objunBlur,bf);
                    list.get(i).setIcon(new ImageIcon(resoult));
                }
                toolBar.repaint();
                panel2.repaint();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        });
        panel.add(pathButton);
        panel.add(loadButton);
        panel.add(unloadButton);
        panel.add(rotateButton);
        panel.add(negativeButton);
        panel.add(blurButton);
        panel.add(unblurButton);
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel,panel2);
        frame = new JFrame("Application");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(split);
        frame.setSize(1600,600);
        frame.setVisible(true);
    }

    void createPanel2(String path) throws IOException {
        loader.read(path);
        list = loader.getList();
        if(toolBar == null)
            toolBar = new JToolBar();
        else
            toolBar.removeAll();
        for(int i =0;i<list.size();i++)
        {
            toolBar.add(list.get(i));
        }
        toolBar.setVisible(true);
        toolBar.setSize(panel2.getSize());
        panel2.add(toolBar);
        panel2.repaint();
    }

    public static void setPath(String ppath)
    {
        path = ppath;
    }

    public void path(String path) throws Exception {
        setPath(path);
        doInBackground();
    }
    @Override
    protected Object doInBackground() throws Exception {
        createPanel2(path);
        return null;
    }

    public static BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi;
        bi = new BufferedImage
                (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }
}
