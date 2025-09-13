/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.lang.*;
import java.net.URL;

/**
 *
 * @author Faizi
 */
public class map extends NewJFrame
{
   public static final int MIN_ZOOM = 0;
   public static final int MAX_ZOOM = 21;
   public static double l1,l2;
   
   JFrame frame;

   /**
    * In map.html file default zoom value is set to 4.
    */
   private static int zoomValue = 4;

   public void fromMap() {
       //double latti=0,longi=0;
        final Browser browser = new Browser();
       BrowserView browserView = new BrowserView(browser);

       JButton zoomInButton = new JButton("Zoom In");
       zoomInButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               if (zoomValue < MAX_ZOOM) {
                   browser.executeJavaScript("map.setZoom(" + ++zoomValue + ")");
               }
           }
       });

       JButton zoomOutButton = new JButton("Zoom Out");
       zoomOutButton.addActionListener((ActionEvent e) -> {
           if (zoomValue > MIN_ZOOM) {
               browser.executeJavaScript("map.setZoom(" + --zoomValue + ")");
           }
       });
       

       JButton setMarkerButton = new JButton("Close");
       setMarkerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                		System.out.println("Frame Closed.");
                                frame.dispose();
                                //SwingUtilities.updateComponentTreeUI(super);
                                //SwingUtilities.invokeLater();
                                setPanes();
                               
            }

       
   });
               
        
       

       JPanel toolBar = new JPanel();
       toolBar.add(zoomInButton);
       toolBar.add(zoomOutButton);
       toolBar.add(setMarkerButton);

       frame = new JFrame("Google Map");
       //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       frame.add(toolBar, BorderLayout.SOUTH);
       frame.add(browserView, BorderLayout.CENTER);
       frame.setSize(900, 500);
       frame.setLocationRelativeTo(null);
       frame.setVisible(true);
       
       
       browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if (event.isMainFrame()) {
                    Browser browser = event.getBrowser();
                    JSValue value = browser.executeJavaScriptAndReturnValue("window");
                    value.asObject().setProperty("map", new map());
                }
            }
        });
       
       
    /*InputStream urlStream = map.class.getResourceAsStream("mapnew.html");
     String html = null;
     try (BufferedReader urlReader = new BufferedReader(new InputStreamReader (urlStream))) {
         StringBuilder builder = new StringBuilder();
         String row;
         while ((row = urlReader.readLine()) != null) {
             builder.append(row);
         }
         html = builder.toString(); 
     }  catch (IOException e) {
         throw new RuntimeException(e);
     }
*/
     
     browser.loadURL("C://mapnew.html");
   
    
     System.out.println("Opened Browser");
    // return l1;
   }
   
  
   
   public static void save(String firstName, String lastName) {
            System.out.println("Latitude = " + firstName);
            System.out.println("Longitude = " + lastName);
            l1=Double.parseDouble(firstName);
            l2=Double.parseDouble(lastName);
         
        }

}