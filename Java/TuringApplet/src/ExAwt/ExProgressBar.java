package ExAwt;

import java.awt.*;

public class ExProgressBar extends Canvas {
    private static final long serialVersionUID = 6139063585549671888L;
    // private Component m_Parent;
    private float m_fRangeMin;
    private float m_fRangeMax;
    private float m_fCellWidth;
    private int m_nSteps;
    private int m_nCrtStep;

    private Image m_bkImage;
    private Image m_offImage;
    private Graphics m_offGraphics;

    private void resetBar() {
        Rectangle r = super.getBounds();
        m_offImage = createImage(r.width, r.height);
        m_offGraphics = m_offImage.getGraphics();
        ExGraphics.tileBackground(m_offGraphics, r, m_bkImage, null);
        m_offGraphics.setColor(Color.lightGray);
        m_offGraphics.draw3DRect(0, 0, r.width - 1, r.height - 1, false);
        m_nCrtStep = 0;
    }

    public ExProgressBar(Component parent) {
        // m_Parent = parent;
        m_offImage = null;
        m_fRangeMin = -1;
        m_fRangeMax = -1;
    }

    public void init(Image bkImage) {
        MediaTracker mediaTracker = new MediaTracker(this);

        m_bkImage = bkImage;
        try {
            mediaTracker.addImage(m_bkImage, 0);
            mediaTracker.waitForID(0);
        } catch (Exception e) {
        }
    }

    public boolean configure(float fRangeMin, float fRangeMax, int nSteps) {
        if (fRangeMin >= fRangeMax || nSteps <= 0)
            return false;
        m_fRangeMin = fRangeMin;
        m_fRangeMax = fRangeMax;
        m_nSteps = nSteps;
        m_fCellWidth = (m_fRangeMax - m_fRangeMin) / m_nSteps;
        m_nCrtStep = 0;
        return true;
    }

    public boolean progress(float fCrtPos) {
        int newStep;
        Rectangle r;

        if (m_offImage == null || fCrtPos > m_fRangeMax || fCrtPos < m_fRangeMin)
            return false;

        newStep = (int) ((fCrtPos - m_fRangeMin) / m_fCellWidth);

        if (newStep < m_nCrtStep)
            resetBar();

        m_offGraphics.setColor(Color.cyan.darker());
        r = super.getBounds();
        for (int i = m_nCrtStep; i <= newStep; i++) {
            m_offGraphics.fill3DRect(i * r.width / m_nSteps + 1, 2, r.width / m_nSteps - 1, r.height - 4, true);
        }
        m_nCrtStep = newStep;
        repaint();
        return true;
    }

    public void clear() {
        resetBar();
        repaint();
        m_fRangeMin = -1;
        m_fRangeMax = -1;
    }

    public void update(Graphics g) {
        if (m_offImage == null) {
            resetBar();
        }
        g.drawImage(m_offImage, 0, 0, null);
    }

    public void paint(Graphics g) {
        update(g);
    }
}
