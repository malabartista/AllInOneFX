/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Xacebans
 */
public class ImageUtils {

    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
//            BufferedImage bufImage = ImageIO.read(in);
//            Area clip = new Area(new Rectangle(0, 0, bufImage.getWidth(), bufImage.getHeight()));
//            Area oval = new Area(new Ellipse2D.Double(0, 0, bufImage.getWidth() - 1, bufImage.getHeight() - 1));
//            clip.subtract(oval);
//            Graphics g2d = bufImage.createGraphics();
//            g2d.setClip(clip);
//            g2d.setColor(Color.BLACK);
//            g2d.fillRect(0, 0, bufImage.getWidth(), bufImage.getHeight());
//            Image image = SwingFXUtils.toFXImage(bufImage, null);

        // With Corner Radius
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)
        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }
}
