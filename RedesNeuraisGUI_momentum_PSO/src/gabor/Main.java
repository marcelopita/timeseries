/*
Copyright (C) 2010

This file is part of the Gabor applet
written by Max Bügler
http://www.maxbuegler.eu/

Gabor applet is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.

Gabor applet is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package gabor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

/**
 * Date: May 6, 2010
 * Time: 12:14:42 PM
 * <p/>
 * Copyright 2010 by Max Buegler.
 * Licensed under General Public License Version 3
 */

public class Main extends Applet {
    private JComboBox image;
    private JSpinner noise;
    private JTextField lamda,theta,psi,sigma,gamma;
    private JButton process,create;
    private BufferedImage input;
    private ImagePanel in,out;
    public void init(){
        setLayout(new BorderLayout());
        JPanel ttopPanel=new JPanel();
        JPanel btopPanel=new JPanel();
        JPanel topPanel=new JPanel();
        JPanel mainPanel=new JPanel(new GridLayout(1,2));
        try{
            input= ImageIO.read(new URL("http://www.maxbuegler.eu/images/fliege.bmp"));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        image=new JComboBox(new String[]{"Image 1","Image 2","Image 3","Image 4","Image 5","Image 6","Image 7","Image 8","Image 9"});
        image.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try{
                    switch(image.getSelectedIndex()){
                        case 0:
                            input= ImageIO.read(new URL("https://fbcdn-sphotos-a.akamaihd.net/hphotos-ak-ash3/525770_396383637056003_100000531113161_1444567_1370516333_n.jpg"));
                            break;
                        case 1:
                            input= ImageIO.read(new URL("http://www.maxbuegler.eu/images/kirche.bmp"));
                            break;
                        case 2:
                            input= ImageIO.read(new URL("http://www.maxbuegler.eu/images/whatever.bmp"));
                            break;
                        case 3:
                            input= ImageIO.read(new URL("http://www.maxbuegler.eu/images/lines.bmp"));
                            break;
                        case 4:
                            input= ImageIO.read(new URL("http://www.maxbuegler.eu/images/star.bmp"));
                            break;
                        case 5:
                            input= ImageIO.read(new URL("http://www.maxbuegler.eu/images/snake.bmp"));
                            break;
                        case 6:
                            input= ImageIO.read(new URL("http://www.maxbuegler.eu/images/points.bmp"));
                            break;
                        case 7:
                            input= ImageIO.read(new URL("http://www.maxbuegler.eu/images/hare.bmp"));
                            break;
                        case 8:
                            input= ImageIO.read(new URL("http://www.maxbuegler.eu/images/servatius.bmp"));
                            break;


                    }
                    in.setImage(input);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        create=new JButton("Create filter");
        process=new JButton("Apply filter");
        noise=new JSpinner(new SpinnerNumberModel(0,0,9,1));
        ttopPanel.add(new JLabel("Input:"));
        ttopPanel.add(image);
        ttopPanel.add(new JLabel("Noise:"));
        ttopPanel.add(noise);

        ttopPanel.add(create);
        ttopPanel.add(process);

        lamda=new JTextField("4.0",3);
        theta=new JTextField("0.6",3);
        psi=new JTextField("1.0",3);
        sigma=new JTextField("2.0",3);
        gamma=new JTextField("0.3",3);

        btopPanel.add(new JLabel("Lamda:"));
        btopPanel.add(lamda);
        btopPanel.add(new JLabel("Theta:"));
        btopPanel.add(theta);
        btopPanel.add(new JLabel("Psi:"));
        btopPanel.add(psi);
        btopPanel.add(new JLabel("Sigma:"));
        btopPanel.add(sigma);
        btopPanel.add(new JLabel("Gamma:"));
        btopPanel.add(gamma);

        topPanel.setLayout(new GridLayout(2,1));
        topPanel.add(ttopPanel);
        topPanel.add(btopPanel);
        add(topPanel,BorderLayout.NORTH);
        in=new ImagePanel();
        in.setImage(input);
        out=new ImagePanel();
        create.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                double l=Double.parseDouble(lamda.getText());
                double t=Double.parseDouble(theta.getText());
                double p=Double.parseDouble(psi.getText());
                double s=Double.parseDouble(sigma.getText());
                double g=Double.parseDouble(gamma.getText());
                double[][] fil=GaborFilter.createGarborFilter(l,t,p,s,g);
                out.setImageSigned(fil,5);
                //double[][] fil=MexicanHat.getMexicanHat2D(s,(int)l,(int)t);
                //out.setImageSigned(fil,5);
                in.setImage(convertImage(input,(Integer)noise.getModel().getValue()));
            }
        });
        ActionListener al=new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                double l=Double.parseDouble(lamda.getText());
                double t=Double.parseDouble(theta.getText());
                double p=Double.parseDouble(psi.getText());
                double s=Double.parseDouble(sigma.getText());
                double g=Double.parseDouble(gamma.getText());
                double[][] fil=GaborFilter.createGarborFilter(l,t,p,s,g);
                out.setImageSigned(GaborFilter.applyGarborFilter(convertImage(input,(Integer)noise.getModel().getValue()), fil));
               // System.out.println(input.getRGB(1,2));
                
                
                
                in.setImage(convertImage(input,(Integer)noise.getModel().getValue()));
            }
        };
        process.addActionListener(al);
        lamda.addActionListener(al);
        theta.addActionListener(al);
        psi.addActionListener(al);
        gamma.addActionListener(al);
        sigma.addActionListener(al);
        JPanel inPanel=new JPanel(new BorderLayout());
        inPanel.add(new JLabel("Input image"),BorderLayout.NORTH);
        inPanel.add(in,BorderLayout.CENTER);
        mainPanel.add(inPanel);

        JPanel oPanel=new JPanel(new BorderLayout());
        oPanel.add(new JLabel("Result:"),BorderLayout.NORTH);
        oPanel.add(out,BorderLayout.CENTER);
        mainPanel.add(oPanel);

        add(mainPanel,BorderLayout.CENTER);

    }

    private static Random rnd=new Random();
    public static int[][] convertImage(BufferedImage img, int noise){
        int[][] out=new int[1+img.getWidth()][1+img.getHeight()];
        for (int x=0;x<img.getWidth();x++){
            for (int y=0;y<img.getHeight();y++){
                out[x][y]=((img.getRGB(x,y) >> 16) & 0xFF);
                if (noise>0)out[x][y]=Math.max(0,Math.min(255,out[x][y]+(rnd.nextInt(20)-10)*noise));
            }
        }
        return out;
    }

    public static class ImagePanel extends JPanel{
        private BufferedImage img;
        public void paintComponent(Graphics g){
            g.setColor(Color.WHITE);
            g.fillRect(0,0,getWidth(),getHeight());
            if(img!=null)g.drawImage(img,0,0,null);
        }
        
        public void setImage(BufferedImage img){
            this.img=img;
            repaint();
        }
        public void setImage(int[][] img){
            BufferedImage i=new BufferedImage(img.length,img[0].length,BufferedImage.TYPE_BYTE_GRAY);
            int maxv=-Integer.MAX_VALUE;
            int minv=Integer.MAX_VALUE;
            for (int x=0;x<img.length;x++){
                for (int y=0;y<img[0].length;y++){
                    if (img[x][y]>maxv)maxv=img[x][y];
                    if (img[x][y]<minv)minv=img[x][y];
                }
            }
            for (int x=0;x<img.length;x++){
                for (int y=0;y<img[0].length;y++){
                    int v=(int)Math.round((255*(double)(img[x][y]-minv))/(maxv-minv));
                    i.setRGB(x,y,new Color(v,v,v).getRGB());
                    /*int c=
                    ((img[x][y] & 0xFF) << 16) |
                    ((img[x][y] & 0xFF) << 8)  |
                    ((img[x][y] & 0xFF) << 0);
                    i.setRGB(x,y,c);*/
                }
            }

            this.img=i;
            repaint();
        }
        public void setImage(double[][] img, int zoom){
            BufferedImage i=new BufferedImage(img.length*zoom,img[0].length*zoom,BufferedImage.TYPE_BYTE_GRAY);
            double maxv=-Integer.MAX_VALUE;
            double minv=Integer.MAX_VALUE;
            for (int x=0;x<img.length;x++){
                for (int y=0;y<img[0].length;y++){
                    if (img[x][y]>maxv)maxv=img[x][y];
                    if (img[x][y]<minv)minv=img[x][y];
                }
            }
            for (int x=0;x<img.length;x++){
                for (int y=0;y<img[0].length;y++){
                    int v=(int)Math.round((255*(double)(img[x][y]-minv))/(maxv-minv));
                    for (int zx=0;zx<zoom;zx++){
                        for (int zy=0;zy<zoom;zy++){
                            i.setRGB(x*zoom+zx,y*zoom+zy,new Color(v,v,v).getRGB());
                        }
                    }

                    /*int c=
                    ((img[x][y] & 0xFF) << 16) |
                    ((img[x][y] & 0xFF) << 8)  |
                    ((img[x][y] & 0xFF) << 0);
                    i.setRGB(x,y,c);*/
                }
            }

            this.img=i;
            repaint();
        }
        public void setImageSigned(double[][] img, int zoom){
            BufferedImage i=new BufferedImage(img.length*zoom,img[0].length*zoom,BufferedImage.TYPE_3BYTE_BGR);
            double maxv=-Integer.MAX_VALUE;
            double minv=Integer.MAX_VALUE;
            for (int x=0;x<img.length;x++){
                for (int y=0;y<img[0].length;y++){
                    if (img[x][y]>maxv&&img[x][y]>0)maxv=img[x][y];
                    if (img[x][y]<minv&&img[x][y]<0)minv=img[x][y];
                }
            }
            for (int x=0;x<img.length;x++){
                for (int y=0;y<img[0].length;y++){
                    int v=(int)Math.round(255*(double)img[x][y]/maxv);
                    if (img[x][y]<0)v=(int)Math.round(255*(double)img[x][y]/minv);
                    for (int zx=0;zx<zoom;zx++){
                        for (int zy=0;zy<zoom;zy++){
                            if (img[x][y]>0)
                                i.setRGB(x*zoom+zx,y*zoom+zy,new Color(0,v,0).getRGB());
                            else
                                i.setRGB(x*zoom+zx,y*zoom+zy,new Color(v,0,0).getRGB());
                        }
                    }

                    /*int c=
                    ((img[x][y] & 0xFF) << 16) |
                    ((img[x][y] & 0xFF) << 8)  |
                    ((img[x][y] & 0xFF) << 0);
                    i.setRGB(x,y,c);*/
                }
            }

            this.img=i;
            repaint();
        }

        public static BufferedImage setImageSigned(int[][] img){
            BufferedImage i=new BufferedImage(img.length,img[0].length,BufferedImage.TYPE_3BYTE_BGR);
            double maxv=-Integer.MAX_VALUE;
            double minv=Integer.MAX_VALUE;
            for (int x=0;x<img.length;x++){
                for (int y=0;y<img[0].length;y++){
                    if (img[x][y]>maxv&&img[x][y]>0)maxv=img[x][y];
                    if (img[x][y]<minv&&img[x][y]<0)minv=img[x][y];
                }
            }
            for (int x=0;x<img.length;x++){
                for (int y=0;y<img[0].length;y++){
                    int v=(int)Math.round(255*(double)img[x][y]/maxv);
                    if (img[x][y]<0)v=(int)Math.round(255*(double)img[x][y]/minv);
                    if (img[x][y]>0)
                        i.setRGB(x,y,new Color(0,v,0).getRGB());
                    else
                        i.setRGB(x,y,new Color(v,0,0).getRGB());

                    /*int c=
                    ((img[x][y] & 0xFF) << 16) |
                    ((img[x][y] & 0xFF) << 8)  |
                    ((img[x][y] & 0xFF) << 0);
                    i.setRGB(x,y,c);*/
                }
            }

         //   img=i;
        //    repaint();
            
            return i;
        }


    }

}
