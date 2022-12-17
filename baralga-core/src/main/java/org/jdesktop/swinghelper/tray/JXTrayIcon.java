/*
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swinghelper.tray;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class JXTrayIcon extends TrayIcon {	
    private JPopupMenu menu;
    private static JDialog dialog;
    static {
        dialog = new JDialog((Frame) null, "TrayDialog");
        dialog.setUndecorated(true);
        dialog.setAlwaysOnTop(true);
    }
    
    private static PopupMenuListener popupListener = new PopupMenuListener() {
        
        public void popupMenuWillBecomeVisible(final PopupMenuEvent event) {
        }

        public void popupMenuWillBecomeInvisible(final PopupMenuEvent event) {
            dialog.setVisible(false);
        }

        public void popupMenuCanceled(final PopupMenuEvent event) {
            dialog.setVisible(false);
        }
    };


    public JXTrayIcon(final Image image) {
        super(image);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent event) {
                showJPopupMenu(event);
            }

            @Override
            public void mouseReleased(final MouseEvent event) {
                showJPopupMenu(event);
            }

        });
    }

    private void showJPopupMenu(final MouseEvent event) {
        if (event.isPopupTrigger() && menu != null) {
            Dimension size = menu.getPreferredSize();
            dialog.setLocation(event.getX(), event.getY() - size.height);
            dialog.setVisible(true);
            menu.show(dialog.getContentPane(), 0, 0);
            // popup works only for focused windows
            dialog.toFront();
        }
    }

    public JPopupMenu getJPopupMenu() {
        return menu;
    }

    public void setJPopupMenu(final JPopupMenu menu) {
        if (this.menu != null) {
            this.menu.removePopupMenuListener(popupListener);
        }
        this.menu = menu;
        menu.addPopupMenuListener(popupListener);
    }

    private static void createGui() {
        JXTrayIcon tray = new JXTrayIcon(createImage());
        tray.setJPopupMenu(createJPopupMenu());
        try {
            SystemTray.getSystemTray().add(tray);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(() -> createGui());
    }

    static Image createImage() {
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = (Graphics2D) image.getGraphics();
        g2D.setColor(Color.RED);
        g2D.fill(new Ellipse2D.Float(0, 0, image.getWidth(), image.getHeight()));
        g2D.dispose();
        return image;
    }

    static JPopupMenu createJPopupMenu() {
        final JPopupMenu menu = new JPopupMenu();
        menu.add(new JMenuItem("Item 1"));
        menu.add(new JMenuItem("Item 2"));
        JMenu submenu = new JMenu("Submenu");
        submenu.add(new JMenuItem("item 1"));
        submenu.add(new JMenuItem("item 2"));
        submenu.add(new JMenuItem("item 3"));
        menu.add(submenu);        
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        menu.add(exitItem);
        return menu;
    }
} 
