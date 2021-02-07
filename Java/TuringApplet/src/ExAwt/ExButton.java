package ExAwt;

import java.awt.image.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ExButton extends Canvas implements Runnable {
    /**
     * 
     */
    private static final long serialVersionUID = 5916865086969444090L;
    // image types (4 bits)
    static public final int CNT_IMGTYPE_MASK = 0x000F0000;
    static public final int CNT_FG_IMG = 0x00010000;
    static public final int CNT_BK_IMG = 0x00020000;

    // fgState index (8 bits) -- CARE: IS AN INDEX --
    static public final int CNT_NFGSTATES = 3;
    static public final int CNT_FG_ENABLED = 0;
    static public final int CNT_FG_DISABLED = 1;
    static public final int CNT_FG_EMPHASIZED = 2; // obvious enabled

    // bgState index (8 bits) -- CARE: IS AN INDED --
    static public final int CNT_NBKSTATES = 3;
    static public final int CNT_BK_NORMAL = 0;
    static public final int CNT_BK_UP = 1;
    static public final int CNT_BK_DOWN = 2;

    private int m_nCrtState; // 0x00RTFFBB;
                             // R=reserved by ExButtonImage
                             // T=image type;
                             // FF=fgState index;
                             // BB=bgState index

    private Image m_offImage;
    private Graphics m_offGraphics;
    private ExButtonImage m_imgFgButtons[], m_imgBkButtons[];
    public Rectangle m_imgFgR;
    private Component m_Parent;

    public ExButton(Component parent) {
        m_nCrtState = 0;
        m_imgFgButtons = m_imgBkButtons = null;
        m_imgFgR = null;
        m_Parent = parent;
    }

    public void init(Image fgImage, Image bkImage) {
        // get the button size from the fgImage size;
        // bgImage should exceed in size fgImage on both directions;
        try {
            MediaTracker mediaTracker = new MediaTracker(this);
            mediaTracker.addImage(fgImage, 0);
            mediaTracker.addImage(bkImage, 1);
            mediaTracker.waitForID(0);
            m_imgFgR = new Rectangle(fgImage.getWidth(null), fgImage.getHeight(null));
            super.setSize(m_imgFgR.width, m_imgFgR.height);
            m_offImage = createImage(m_imgFgR.width, m_imgFgR.height);
            m_offGraphics = m_offImage.getGraphics();
            mediaTracker.waitForID(1);
        } catch (Exception e) {
            return;
        }

        m_nCrtState = (CNT_FG_ENABLED << 8) | CNT_BK_NORMAL; // image type not significant here

        m_imgFgButtons = new ExButtonImage[CNT_NFGSTATES];
        for (int i = 0; i < CNT_NFGSTATES; i++) {
            m_imgFgButtons[i] = new ExButtonImage(CNT_FG_IMG | (i << 8), fgImage, this); // reserved bits not
                                                                                         // significant here
        }

        m_imgBkButtons = new ExButtonImage[CNT_NBKSTATES];
        for (int i = 0; i < CNT_NBKSTATES; i++) {
            m_imgBkButtons[i] = new ExButtonImage(CNT_BK_IMG | i, bkImage, this); // reserved bits not significant here
        }

        new Thread(this).start();
    }

    public void setEnabled(boolean enabled) {
        if (((m_nCrtState & 0x0000FF00) >> 8) == (enabled ? CNT_FG_ENABLED : CNT_FG_DISABLED))
            return;
        m_nCrtState &= 0xFFFF00FF;
        if (enabled) {
            m_nCrtState |= CNT_FG_ENABLED << 8;
        } else {
            m_nCrtState |= CNT_FG_DISABLED << 8;
            m_nCrtState = (m_nCrtState & 0xFFFFFF00) | CNT_BK_NORMAL;
        }
        repaint();
    }

    public boolean isEnabled() {
        return (m_nCrtState & 0x0000FF00) == CNT_FG_ENABLED;
    }

    public void setEmphasized(boolean emphasized) {
        if (((m_nCrtState & 0x0000FF00) >> 8) == CNT_FG_DISABLED)
            return;
        m_nCrtState &= 0xFFFF00FF;
        m_nCrtState |= emphasized ? (CNT_FG_EMPHASIZED << 8) : (CNT_FG_ENABLED << 8);
        repaint();
    }

    public boolean isEmphasized() {
        return (m_nCrtState & 0x0000FF00) == CNT_FG_EMPHASIZED;
    }

    public void update(Graphics g) {
        m_imgBkButtons[m_nCrtState & 0x000000FF].update(m_offGraphics);
        m_imgFgButtons[(m_nCrtState & 0x0000FF00) >> 8].update(m_offGraphics);

        switch (m_nCrtState & 0x000000FF) {
        case ExButton.CNT_BK_DOWN:
            m_offGraphics.copyArea(1, 1, m_imgFgR.width - 3, m_imgFgR.height - 3, 1, 1);
            break;
        case ExButton.CNT_BK_UP:
            m_offGraphics.copyArea(2, 2, m_imgFgR.width - 3, m_imgFgR.height - 3, -1, -1);
            break;
        }
        g.drawImage(m_offImage, 0, 0, null);
    }

    public void paint(Graphics g) {
        update(g);
    }

    /*
     * Thread Handler
     */
    public void run() {
        int i;

        for (i = 0; i < CNT_NBKSTATES; i++) {
            m_imgBkButtons[i].update(null);
        }
        for (i = 0; i < CNT_NFGSTATES; i++) {
            m_imgFgButtons[i].update(null);
        }
    }

    /*
     * Mouse Handlers
     */
    public boolean mouseEnter(Event evt, int x, int y) {
        if (((m_nCrtState & 0x0000FF00) >> 8) == CNT_FG_DISABLED)
            return false;
        if (m_imgFgR.contains(x, y)) {
            m_nCrtState = (m_nCrtState & 0xFFFFFF00) | ExButton.CNT_BK_UP;
            repaint();
            return true;
        }
        return false;
    }

    public boolean mouseExit(Event evt, int x, int y) {
        if (((m_nCrtState & 0x0000FF00) >> 8) == CNT_FG_DISABLED)
            return false;
        m_nCrtState = (m_nCrtState & 0xFFFFFF00) | ExButton.CNT_BK_NORMAL;
        repaint();
        return true;
    }

    public boolean mouseDown(Event evt, int x, int y) {
        if (((m_nCrtState & 0x0000FF00) >> 8) == CNT_FG_DISABLED)
            return false;
        if (m_imgFgR.contains(x, y)) {
            m_nCrtState = (m_nCrtState & 0xFFFFFF00) | ExButton.CNT_BK_DOWN;
            repaint();
            return true;
        }
        return false;
    }
    
    @Override
    public void processMouseEvent(MouseEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        
        if ((((m_nCrtState & 0x0000FF00) >> 8) == CNT_FG_DISABLED) && m_imgFgR.contains(x, y)) {
            m_nCrtState = (m_nCrtState & 0xFFFFFF00) | ExButton.CNT_BK_UP;
            repaint();
            if (m_Parent != null) {
                m_Parent.dispatchEvent(evt);
            }
        }
    }

//    public boolean mouseUp(Event evt, int x, int y) {
//        if (((m_nCrtState & 0x0000FF00) >> 8) == CNT_FG_DISABLED)
//            return false;
//        if (m_imgFgR.contains(x, y)) {
//            m_nCrtState = (m_nCrtState & 0xFFFFFF00) | ExButton.CNT_BK_UP;
//            repaint();
//            if (m_Parent != null) {
//                evt.id = Event.ACTION_EVENT;
//                evt.target = this;
//                m_Parent.deliverEvent(evt);
//            }
//            return true;
//        }
//        return false;
//    }
}

class ExButtonImage {
    static public final int CNT_LOADED = 0x00100000;

    public int m_nState;
    public ExButton m_Parent;

    private Image m_imgState;

    public ExButtonImage(int nState, Image image, ExButton parent) {
        FilteredImageSource imgProducer;

        m_nState = nState & ~(CNT_LOADED); // just to make sure "not loaded"
        m_Parent = parent;
        imgProducer = new FilteredImageSource(image.getSource(), new ExButtonFilter(this));
        m_imgState = m_Parent.createImage(imgProducer);
    }

    public void update(Graphics g) {
        if ((m_nState & CNT_LOADED) != CNT_LOADED) {
            try {
                MediaTracker mediaTracker = new MediaTracker(m_Parent);

                mediaTracker.addImage(m_imgState, 0);
                mediaTracker.waitForID(0);
            } catch (Exception e) {
            }
            m_nState |= CNT_LOADED;
        }
        if (g == null)
            return;
        g.drawImage(m_imgState, 0, 0, null);
    }
}

class ExButtonFilter extends RGBImageFilter {
    private ExButtonImage m_Parent;

    public ExButtonFilter(ExButtonImage parent) {
        m_Parent = parent;
    }

    public int filterRGB(int x, int y, int rgb) {
        Color colRgb;

        if ((rgb & 0xFF000000) == 0)
            return rgb;
        Rectangle r = m_Parent.m_Parent.m_imgFgR;

        switch (m_Parent.m_nState & ExButton.CNT_IMGTYPE_MASK) {
        case ExButton.CNT_BK_IMG:
            if (x * y == 0 || x == r.width - 1 || y == r.height - 1) {
                if ((m_Parent.m_nState & 0x000000FF) != ExButton.CNT_BK_NORMAL) {
                    colRgb = new Color(rgb);

                    if (x * y == 0) {
                        colRgb = (m_Parent.m_nState & 0x000000FF) == ExButton.CNT_BK_UP ? colRgb.brighter()
                                : colRgb.darker();
                    }
                    if (x == r.width - 1 || y == r.height - 1) {
                        colRgb = (m_Parent.m_nState & 0x000000FF) == ExButton.CNT_BK_UP ? colRgb.darker()
                                : colRgb.brighter();
                    }
                    rgb = colRgb.getRGB();
                }
            }
            break;
        case ExButton.CNT_FG_IMG:
            switch ((m_Parent.m_nState & 0x0000FF00) >> 8) {
            case ExButton.CNT_FG_DISABLED:
                rgb = (rgb & 0x7fffffff);
                break;
            case ExButton.CNT_FG_EMPHASIZED:
                colRgb = new Color(rgb);
                rgb = colRgb.brighter().getRGB();
                break;
            }
        }
        return rgb;
    }
}
