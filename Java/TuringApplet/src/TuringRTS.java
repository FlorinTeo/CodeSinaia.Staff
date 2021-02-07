import java.applet.Applet;
import java.awt.*;
import ExAwt.*;

public class TuringRTS extends Canvas {
    /**
     * 
     */
    private static final long serialVersionUID = -4726916080270452838L;
    public char m_symLastTested = '?';
    // internal tracing mode
    public final static int TRACE = 0x0001;
    // internal danger mode
    public final static int DANGER_MASK = 0x0FF0;
    public final static int DANGER = 0x1000;
    // internal exclusive status flags
    public final static int STATUS = 0x000E;
    public final static int STATUS_RUNNING = 0x0002;
    public final static int STATUS_HALTED = 0x0004;
    public final static int STATUS_HANGED = 0x0006;

    int m_nFlags; // --------.--------.---Ddddd.ddddssst
                  // t = tracing mode (TRACE_OFF/TRACE_ON)
                  // sss = machine status (8 states allowed)
                  // dddddddd = tape is extended to right => inf. loop danger!
                  // the danger rate is decreased to 0!
                  // D = danger sensor on(1)/off(0)

    // --------------logical parameters-------------------
    String m_strTape; // Turing's input tape;
    int m_nHead; // Logical Head current position; [0 .. )

    // --------------graphic parameters-------------------
    String m_strGTape; // a backup of the Turing's input tape,
                       // synchronized almost all the time with the m_strTape
                       // (except the ~TRACE state).
    int m_nGHead; // current graphic head view; [0 .. )
                  // synchronized almost all the time with the m_nHead
                  // (except the ~TRACE state).
    int ViewW, ViewH; // pixel sizes of the Tape Window;
    int SlotW, SlotH; // pixel sizes of one tape slot;
    int SlotB; // pixel Slot Base (y-offset in drawText call);
    int ViewSz; // logical size of the Tape Window (nb. of slots);
    int m_nView; // current offset of the View; [-ViewSz/4 .. )
    int m_nHeadOffset; // offset used when the head is moving L/R.
    int m_nScrollOffset;// offset used when view is scrolled L/R.

    Applet m_Parent; // reference to the parent Applet.
    // graphic off-screen Images
    // main off-screen image - support for all others images.
    // m_offGraphics is also store, in order to speed-up the animations.
    Image m_offImage;
    Graphics m_offGraphics;
    Image m_imgBackground; // background image, supplied at construction.
                           // if no background (first constructor)
                           // the current background color is used.
    Image m_imgOffTape; // off-screen image for the Turing input tape.
    Image m_imgHead; // off-screen image for the head.

    private void enlargeTapes(int toIndex) {
        if ((m_nFlags & DANGER) == DANGER && toIndex >= m_strTape.length()) {
            int dangerRate = (m_nFlags & DANGER_MASK) >> 4;
            if (dangerRate > 0) {
                m_nFlags &= ~DANGER_MASK;
                m_nFlags |= (dangerRate - 1) << 4;
            }
        }
        for (int i = m_strTape.length(); i <= toIndex; i++)
            m_strTape += "#";
        for (int i = m_strGTape.length(); i <= toIndex; i++)
            m_strGTape += "#";
    }

    private Image initOffTape(int nFrom, int nSize) {
        Image offTape;
        Graphics g;
        int i, k;
        char caTape[];

        enlargeTapes(nFrom + nSize);
        caTape = m_strGTape.toCharArray();

        offTape = createImage(SlotW * nSize, SlotH);
        g = offTape.getGraphics();
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, SlotW * nSize, SlotH);
        g.setColor(Color.gray);
        for (i = 0, k = nFrom; k < nFrom + nSize; i += SlotW, k++) {
            if (k < 0) {
                g.fillRect(i, 0, SlotW, SlotH);
            } else {
                g.setColor(Color.lightGray);
                g.draw3DRect(i, 0, SlotW - 1, SlotH - 1, false);
                if (k == 0) {
                    g.setColor(Color.black);
                    g.drawLine(i, 0, i, SlotH);
                }
                g.setColor(caTape[k] == '#' ? Color.gray : Color.blue);
                g.drawChars(caTape, k, 1, i + 3, SlotB);
            }
        }
        g.dispose();
        return offTape;
    }

    private void getHeadImg(String imgName) {
        try {
            MediaTracker mediaTracker = new MediaTracker(this);
            m_imgHead = m_Parent.getImage(m_Parent.getDocumentBase(), imgName);
            mediaTracker.addImage(m_imgHead, 0);
            mediaTracker.waitForID(0);
        } catch (Exception e) {
        }
    }

    public TuringRTS(Applet parent) {
        m_nFlags = TRACE | STATUS_RUNNING;
        m_strTape = null;
        m_imgBackground = null;
        m_nHeadOffset = 0;
        m_nScrollOffset = 0;
        m_Parent = parent;
        getHeadImg("res/Head.gif");
    }

    public TuringRTS(Applet parent, Image imgBackground) {
        this(parent);
        m_imgBackground = imgBackground;
    }

    public void init(String strTape, int nHead) {
        Font f = new Font("Courier", Font.BOLD, 12);
        FontMetrics fm = getFontMetrics(f);
        Rectangle r = super.getBounds();

        setFont(f);

        m_strTape = new String(strTape);
        m_strGTape = new String(strTape);
        m_nHead = m_nGHead = nHead;

        SlotW = fm.getMaxAdvance() + 4;
        SlotH = fm.getMaxAscent() + fm.getMaxDescent() + 4;
        SlotB = fm.getMaxAscent() + 2;
        ViewSz = r.width / SlotW;
        ViewW = SlotW * ViewSz;
        ViewH = SlotH;
        setSize(ViewW, 2 * ViewH);
        m_nView = m_nGHead - ViewSz / 4;

        m_offImage = createImage(super.getBounds().width, super.getBounds().height);
        m_offGraphics = m_offImage.getGraphics();

        m_imgOffTape = initOffTape(m_nGHead - ViewSz / 4, ViewSz);
    }

    // scrolls L/R the view 1/4 of the ViewW
    private void scrollG(int dir) {
        int nSlotsOff = ViewSz / 4;
        int from = dir == 1 ? 0 : -nSlotsOff * SlotW;
        int to = dir == 1 ? -nSlotsOff * SlotW : 0;

        m_imgOffTape = initOffTape(Math.min(m_nView, m_nView + dir * nSlotsOff), ViewSz + nSlotsOff);
        m_nHeadOffset = dir == 1 ? 0 : nSlotsOff * SlotW;
        for (m_nScrollOffset = from; m_nScrollOffset != to; m_nScrollOffset -= dir) {
            repaint();
            try {
                Thread.sleep(5);
            } catch (Exception e) {
            }
        }

        if (dir == 1) {
            Graphics g = m_imgOffTape.getGraphics();
            g.copyArea(nSlotsOff * SlotW, 0, ViewSz * SlotW, SlotH, -nSlotsOff * SlotW, 0);
            g.dispose();
        }
        m_nView += dir * nSlotsOff;
        m_nScrollOffset = 0;
        m_nHeadOffset = 0;
    }

    private void updateReferencedSlots(int from, int to) {
        int i;
        Graphics g;
        char chArray[] = m_strTape.toCharArray();
        char chGArray[] = m_strGTape.toCharArray();
        boolean dirty = false;

        if (from < 0 || to < 0)
            return;
        g = m_imgOffTape.getGraphics();
        for (i = from; i != to + 1; i++) {
            if (chGArray[i] != chArray[i]) {
                g.setColor(Color.red);
                g.fillRect((i - m_nView) * SlotW + 1, 1, SlotW - 2, SlotH - 2);
                g.setColor(Color.yellow);
                g.drawChars(chArray, i, 1, (i - m_nView) * SlotW + 3, SlotB);
                dirty = true;
            }
        }
        g.dispose();
        if (!dirty)
            return;
        repaint();
        try {
            Thread.sleep(400);
        } catch (Exception e) {
        }
        g = m_imgOffTape.getGraphics();
        for (i = from; i != to + 1; i++) {
            if (chGArray[i] != chArray[i]) {
                g.setColor(Color.lightGray);
                g.fillRect((i - m_nView) * SlotW + 1, 1, SlotW - 2, SlotH - 2);
                g.setColor(chArray[i] == '#' ? Color.gray : Color.blue);
                g.drawChars(chArray, i, 1, (i - m_nView) * SlotW + 3, SlotB);
                chGArray[i] = chArray[i];
            }
        }
        m_strGTape = String.valueOf(chGArray);
        g.dispose();
        repaint();
    }

    // handles L and R operations of graphic head, and highlights
    // the origin slot changes, if any.
    private void moveG(int dir) {
        // no need to call enlargeTapes() here, this method is called after the
        // logical operation has been performed. The tapes were enlarged at that
        // moment.
        if ((m_nGHead + dir) < m_nView || (m_nGHead + dir) > (m_nView + ViewSz - 1))
            scrollG(dir);
        for (m_nHeadOffset = dir; dir * m_nHeadOffset <= SlotW; m_nHeadOffset += dir) {
            if (m_strGTape.charAt(m_nGHead) != m_strTape.charAt(m_nGHead)) {
                if (m_nHeadOffset == dir) {
                    char chArray[] = new char[1];
                    Graphics g = m_imgOffTape.getGraphics();

                    chArray[0] = m_strTape.charAt(m_nGHead);

                    g.setColor(Color.red);
                    g.fillRect((m_nGHead - m_nView) * SlotW + 1, 1, SlotW - 2, SlotH - 2);
                    g.setColor(Color.yellow);
                    g.drawChars(chArray, 0, 1, (m_nGHead - m_nView) * SlotW + 3, SlotB);
                    g.dispose();
                }
                if (m_nHeadOffset * dir == SlotW / 2) {
                    char chArray[] = m_strGTape.toCharArray();
                    Graphics g = m_imgOffTape.getGraphics();
                    chArray[m_nGHead] = m_strTape.charAt(m_nGHead);
                    g.setColor(Color.lightGray);
                    g.fillRect((m_nGHead - m_nView) * SlotW + 1, 1, SlotW - 2, SlotH - 2);
                    g.setColor(chArray[m_nGHead] == '#' ? Color.gray : Color.blue);
                    g.drawChars(chArray, m_nGHead, 1, (m_nGHead - m_nView) * SlotW + 3, SlotB);
                    g.dispose();
                    m_strGTape = String.valueOf(chArray);
                }
            }
            repaint();
            try {
                Thread.sleep(5);
            } catch (Exception e) {
            }
        }
        m_nGHead += dir;
        m_nHeadOffset = 0;
    }

    public void update(Graphics g) {
        if (m_strTape == null)
            return;

        if (m_imgBackground != null) {
            ExGraphics.tileBackground(m_offGraphics, super.getBounds(), m_imgBackground, null);
        }

        // all images are drawn here.
        m_offGraphics.drawImage(m_imgOffTape, m_nScrollOffset, 0, null);
        m_offGraphics.drawImage(m_imgHead, (m_nGHead - m_nView) * SlotW - 1 + m_nHeadOffset + m_nScrollOffset,
                SlotH + 4, null);

        // final update of the image
        g.drawImage(m_offImage, 0, 0, null);
    }

    public void paint(Graphics g) {
        update(g);
    }

    // this method is called only if the machine transit to TRACE mode, or
    // on failure (HANG/HALT). In the latter case, the head becomes red.
    public int updateStatus() {
        int dir;

        if (m_nGHead <= m_nHead) {
            dir = 1;
            updateReferencedSlots(Math.max(m_nView, 0), m_nGHead);
        } else {
            dir = -1;
            updateReferencedSlots(m_nGHead, m_nView + ViewSz - 1);
        }
        while (m_nGHead != m_nHead) {
            moveG(dir);
        }
        if (dir == 1) {
            updateReferencedSlots(m_nGHead, m_nView + ViewSz - 1);
        } else {
            updateReferencedSlots(Math.max(m_nView, 0), m_nGHead);
        }

        m_strGTape = new String(m_strTape);

        return m_nFlags & STATUS;
    }

    // handles L and R operations. Noting more. Any more complex operations,
    // like L(s) or L(!s) should be handled in TuringAPI, by using the basic
    // operations - it's the spirit of the hole Turing Machine theory.
    // if TRACE mode, the graphical head follows the logical head.
    // if the machine hangs, the api level calls automatically the updateStatus()
    // which transits to this hang state.
    public int move(int dir) {
        if ((m_nFlags & STATUS) != STATUS_RUNNING)
            return (m_nFlags & STATUS);

        if ((m_nFlags & DANGER) == DANGER && (m_nFlags & DANGER_MASK) == 0) {
            m_nFlags &= ~STATUS;
            m_nFlags |= STATUS_HANGED;
            getHeadImg("res/HeadHanged.gif");
            repaint();
            return m_nFlags & STATUS;
        }

        m_nHead += dir;
        if (dir > 0)
            enlargeTapes(m_nHead);

        if ((m_nFlags & TRACE) == TRACE)
            moveG(dir);

        if (m_nHead < 0) {
            m_nFlags &= ~STATUS;
            m_nFlags |= STATUS_HANGED;
            getHeadImg("res/HeadHanged.gif");
            repaint();
            return STATUS_HANGED;
        }
        return STATUS_RUNNING;
    }

    public char read() {
        if ((m_nFlags & STATUS) != STATUS_RUNNING)
            return '\0';
        return m_strTape.charAt(m_nHead);
    }

    public int write(char sym) {
        char chArray[] = m_strTape.toCharArray();

        if ((m_nFlags & STATUS) != STATUS_RUNNING)
            return (m_nFlags & STATUS);
        chArray[m_nHead] = sym;
        m_strTape = String.valueOf(chArray);

        if ((m_nFlags & TRACE) == TRACE) {
            // at this point, m_strTape and m_strGTape should be synchronized,
            // and m_nHead should be the same with m_nGHead.
            m_strGTape = String.valueOf(chArray);
            try {
                Graphics g;

                g = m_imgOffTape.getGraphics();
                g.setColor(Color.red);
                g.fillRect((m_nGHead - m_nView) * SlotW + 1, 1, SlotW - 2, SlotH - 2);
                g.setColor(Color.yellow);
                g.drawChars(chArray, m_nGHead, 1, (m_nGHead - m_nView) * SlotW + 3, SlotB);
                g.dispose();
                repaint();

                Thread.sleep(400);
                g = m_imgOffTape.getGraphics();
                g.setColor(Color.lightGray);
                g.fillRect((m_nGHead - m_nView) * SlotW + 1, 1, SlotW - 2, SlotH - 2);
                g.setColor(chArray[m_nGHead] == '#' ? Color.gray : Color.blue);
                g.drawChars(chArray, m_nGHead, 1, (m_nGHead - m_nView) * SlotW + 3, SlotB);
                g.dispose();
                repaint();
            } catch (Exception e) {
            }
        }
        return STATUS_RUNNING;
    }

    public void setDangerBoundary(int danger) {
        if (danger != -1) {
            m_nFlags &= ~DANGER_MASK;
            m_nFlags |= (danger << 4) & DANGER_MASK;
            m_nFlags |= DANGER;
        } else {
            m_nFlags &= ~DANGER;
        }
    }

    public boolean getTracing() {
        return (m_nFlags & TRACE) == TRACE;
    }

    public void setTracing(boolean traceOn) {
        if (traceOn) {
            m_nFlags |= TRACE;
            updateStatus();
        } else
            m_nFlags &= ~TRACE;
    }
}
