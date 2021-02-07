import java.applet.Applet;
import java.awt.*;
import ExAwt.*;

public class Turing extends Applet implements TuringObserver {
    /**
     * 
     */
    private static final long serialVersionUID = 3558656826098523682L;
    private final int EXEC_STATEMASK = 0x0F;
    private final int EXEC_CONTINOUS = 1;
    private final int EXEC_STEPBYSTEP = 2;

    int m_nFlags; // ----SSSS
                  // SSSS = index of Turing's current state.

    TuringRTS m_turingRTS;
    Image m_imgBackground = null;
    TextArea m_turingProgram;
    ExButton m_ebRun, m_ebStop, m_ebStep, m_ebRestart;

    public void buttonStates(int states) {
        m_ebRun.setEnabled((states & 0x8) != 0);
        m_ebStop.setEnabled((states & 0x4) != 0);
        m_ebStep.setEnabled((states & 0x2) != 0);
        m_ebRestart.setEnabled((states & 0x1) != 0);
    }

    public void init() {
        Image imgRun, imgStop, imgStep, imgRestart;
        String strParam = getParameter("Background");
        Rectangle r = super.getBounds();

        imgRun = getImage(getDocumentBase(), "res/ButtonRun24x24.gif");
        imgStop = getImage(getDocumentBase(), "res/ButtonStop24x24.gif");
        imgStep = getImage(getDocumentBase(), "res/ButtonStep24x24.gif");
        imgRestart = getImage(getDocumentBase(), "res/ButtonRestart24x24.gif");
        if (strParam != null) {
            try {
                MediaTracker mediaTracker = new MediaTracker(this);

                m_imgBackground = getImage(getDocumentBase(), strParam);
                mediaTracker.addImage(m_imgBackground, 0);
                mediaTracker.waitForID(0);
            } catch (Exception e) {
                return;
            }
        }
        setLayout(null);

        m_turingRTS = new TuringRTS(this, m_imgBackground);
        add(m_turingRTS);
        m_turingRTS.setBounds(0, 0, r.width, 48);
        m_turingRTS.init("#", 0);

        m_ebRun = new ExButton(this);
        add(m_ebRun);
        m_ebRun.setLocation(0, 54);
        m_ebRun.init(imgRun, m_imgBackground);

        m_ebStop = new ExButton(this);
        add(m_ebStop);
        m_ebStop.setLocation(26, 54);
        m_ebStop.init(imgStop, m_imgBackground);

        m_ebStep = new ExButton(this);
        add(m_ebStep);
        m_ebStep.setLocation(52, 54);
        m_ebStep.init(imgStep, m_imgBackground);

        m_ebRestart = new ExButton(this);
        add(m_ebRestart);
        m_ebRestart.setLocation(r.width - 24, 54);
        m_ebRestart.init(imgRestart, m_imgBackground);

        buttonStates(0xB);
        m_turingProgram = new TextArea();
        add(m_turingProgram);
        m_turingProgram.setBounds(0, 84, r.width, 154);
        Font f = m_turingProgram.getFont();
        f = new Font("Courier", f.getStyle(), f.getSize());
        m_turingProgram.setFont(f);

        m_nFlags = 0;
    }

    public void paint(Graphics g) {
        if (m_imgBackground != null)
            ExGraphics.tileBackground(this, m_imgBackground, null);
    }

    public boolean action(Event evnt, Object what) {
        if (evnt.target == m_ebStop) {
            m_nFlags &= ~EXEC_STATEMASK;
            m_nFlags |= EXEC_STEPBYSTEP;
            buttonStates(0x0);
            return true;
        }
        if (evnt.target == m_ebStep || evnt.target == m_ebRun) {
            m_nFlags &= ~EXEC_STATEMASK;
            m_nFlags |= (evnt.target == m_ebRun) ? EXEC_CONTINOUS : EXEC_STEPBYSTEP;

            buttonStates(evnt.target == m_ebRun ? 0x4 : 0x0);
            TuringAPI turingAPI = new TuringAPI(m_turingRTS);
            turingAPI.parseProgram(m_turingProgram, this);
            m_turingProgram.requestFocus();
            return true;
        }
        if (evnt.target == m_ebRestart) {
            m_nFlags = 0;

            remove(m_turingRTS);
            m_turingRTS = new TuringRTS(this, m_imgBackground);
            add(m_turingRTS);
            m_turingRTS.setBounds(0, 0, super.getBounds().width, 48);
            m_turingRTS.init("#", 0);
            m_turingProgram.select(0, 0);
            m_turingProgram.requestFocus();
            buttonStates(0xB);
            return true;
        }
        return false;
    }

    public void turingStatus(TuringAPI id, int what) {
        switch (what) {
        case TuringRTS.STATUS_RUNNING:
            if ((m_nFlags & EXEC_STATEMASK) == EXEC_CONTINOUS) {
                TuringAPI turingAPI = new TuringAPI(m_turingRTS);
                turingAPI.parseProgram(m_turingProgram, this);
                return;
            }
            buttonStates(0xB);
            break;
        case TuringRTS.STATUS_HALTED:
            m_nFlags &= ~EXEC_STATEMASK;
            m_nFlags |= EXEC_STEPBYSTEP;
            m_turingRTS.updateStatus();
            buttonStates(0xB);
            break;
        case TuringRTS.STATUS_HANGED:
            buttonStates(0x1);
            break;
        }
        m_turingProgram.requestFocus();
    }

    public void turingBreak(TuringAPI id) {
        m_nFlags &= ~EXEC_STATEMASK;
        m_nFlags |= EXEC_STEPBYSTEP;
    }
}
