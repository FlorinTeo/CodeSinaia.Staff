package ExAwt;

import java.awt.image.*;
import java.awt.*;

public class ExGraphics {
    public static void tileBackground(Component target, Image imgBk, ImageObserver imgObserver) {
        Graphics g = target.getGraphics();
        MediaTracker mediaTracker = new MediaTracker(target);
        Rectangle r = target.getBounds();

        int crtX, crtY;
        try {
            mediaTracker.addImage(imgBk, 0);
            mediaTracker.waitForID(0);
            for (crtY = 0; crtY < r.height; crtY += imgBk.getHeight(imgObserver))
                for (crtX = 0; crtX < r.width; crtX += imgBk.getWidth(imgObserver))
                    g.drawImage(imgBk, crtX, crtY, imgObserver);
        } catch (Exception e) {
            return;
        }
    }

    public static void tileBackground(Graphics target, Rectangle r, Image imgBk, ImageObserver imgObserver) {
        int crtX, crtY;

        for (crtY = 0; crtY < r.height; crtY += imgBk.getHeight(imgObserver))
            for (crtX = 0; crtX < r.width; crtX += imgBk.getWidth(imgObserver))
                target.drawImage(imgBk, crtX, crtY, imgObserver);
    }
}