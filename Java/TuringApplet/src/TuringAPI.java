import java.awt.*;
import java.lang.Thread;

public class TuringAPI extends Thread {
    private final String STR_BLANC_CHARS = " \t\n\r";
    private TuringRTS m_rts;
    private TextArea m_turingProgram;
    private int m_PC; // program counter; index in m_turingProgram text
    private TuringObserver m_Observer;

    private int markError(int to) {
        m_turingProgram.select(m_PC, to == -1 ? m_turingProgram.getText().length() : to);
        return TuringRTS.STATUS_HALTED;
    }

    private int nextStatement(char Program[]) {
        for (; m_PC < Program.length && (STR_BLANC_CHARS.indexOf(Program[m_PC]) != -1 || Program[m_PC] == '['
                || Program[m_PC] == ';'); m_PC++) {
            int _PC = m_PC;
            if (Program[m_PC] == '[') {
                _PC = String.valueOf(Program).indexOf(']', m_PC);
            }
            if (Program[m_PC] == ';') {
                _PC = String.valueOf(Program).indexOf('\n', m_PC);
            }
            if (_PC == -1) {
                m_turingProgram.select(m_PC, Program.length);
                return TuringRTS.STATUS_HALTED;
            }
            m_PC = _PC;

        }
        return m_PC < Program.length ? TuringRTS.STATUS_RUNNING : TuringRTS.STATUS_HALTED;
    }

    private int parseMovement(String Program) {
        char Prog[] = Program.toCharArray();
        char direction, target;
        boolean exclude;
        int retCode;
        int _PC = m_PC;

        direction = Prog[_PC];
        _PC++;
        if (_PC >= Prog.length || Prog[_PC] != '(') {
            m_PC = _PC;
            return m_rts.move(direction == 'L' ? -1 : 1);
        }
        _PC++;
        if (_PC >= Prog.length)
            return markError(_PC);
        exclude = (Prog[_PC] == '!');
        if (exclude)
            _PC++;
        if (_PC >= Prog.length)
            return markError(_PC);
        target = Prog[_PC];
        _PC++;
        if (_PC >= Prog.length || Prog[_PC] != ')')
            return markError(_PC);
        _PC++;
        if (direction == 'R')
            m_rts.setDangerBoundary(4);
        do {
            retCode = m_rts.move(direction == 'L' ? -1 : 1);
        } while (retCode == TuringRTS.STATUS_RUNNING
                && ((!exclude && (m_rts.read() != target)) || (exclude && (m_rts.read() == target))));
        m_rts.setDangerBoundary(-1);
        m_PC = _PC;
        return retCode;
    }

    private int parseControl(String Program) {
        int _PC = m_PC;

        if (Program.indexOf('}', _PC) == -1)
            return markError(-1);
        switch (Program.charAt(++_PC)) {
        case 'B':
            if (Program.charAt(++_PC) != '}')
                return markError(_PC);
            m_Observer.turingBreak(this);
            m_PC = ++_PC;
            return TuringRTS.STATUS_RUNNING;
        case 'T':
            char trace = Program.charAt(++_PC);
            if (trace == '0')
                m_rts.setTracing(false);
            else if (trace == '1')
                m_rts.setTracing(true);
            else
                return markError(_PC);
            if (Program.charAt(++_PC) != '}')
                return markError(_PC);
            m_PC = ++_PC;
            return TuringRTS.STATUS_RUNNING;
        case '\"':
            int i1 = Program.indexOf('\"', ++_PC);
            int i2 = Program.indexOf('}', _PC);
            int retCode, p;
            boolean tracing = m_rts.getTracing();

            if (i1 == -1 || i1 != i2 - 1)
                return markError(i2);
            if (tracing)
                m_rts.setTracing(false);
            for (p = 0, retCode = TuringRTS.STATUS_RUNNING; retCode == TuringRTS.STATUS_RUNNING
                    && Program.charAt(_PC) != '\"'; _PC++, p++) {
                char ch = Program.charAt(_PC);
                switch (ch) {
                case '>':
                    p = -1;
                    break;
                case '<':
                    p = 0;
                    break;
                default:
                    retCode = m_rts.write(ch);
                    if (retCode == TuringRTS.STATUS_RUNNING)
                        retCode = m_rts.move(1);
                    break;
                }
            }
            for (; retCode == TuringRTS.STATUS_RUNNING && p > 0; p--)
                retCode = m_rts.move(-1);
            if (tracing)
                m_rts.setTracing(true);
            m_PC = _PC + 2;
            return TuringRTS.STATUS_RUNNING;
        }
        return markError(Program.indexOf('}', m_PC));
    }

    /*----Start of JUMP Parsing Routines---------------*/
    private int skipToChar(String target, int _PC, String Program) {
        for (; _PC < Program.length() && target.indexOf(Program.charAt(_PC)) == -1; _PC++)
            ;
        return (_PC < Program.length()) ? _PC : -1;
    }

    private int skipToAnyButChar(String target, int _PC, String Program) {
        for (; _PC < Program.length() && target.indexOf(Program.charAt(_PC)) != -1; _PC++)
            ;
        return (_PC < Program.length()) ? _PC : -1;
    }

    /*----End of JUMP Parsing Routines-----------------*/
    private int parseJumpGotoLabel(int _PC, String Program, String jmpGoto) {
        if (jmpGoto == null)
            return _PC + 1;
        jmpGoto = (new String("[")).concat(jmpGoto).concat("]");
        _PC = Program.indexOf(jmpGoto);
        return (_PC == -1) ? Program.length() : _PC + jmpGoto.length();
    }

    private boolean parseJumpSelectors(char chTarget, String jmpSelectors) {
        int iTarget = jmpSelectors.indexOf(chTarget);
        if (iTarget == -1) {
            if (jmpSelectors.indexOf('!') == -1)
                return false;
        } else {
            if (iTarget != 0 && jmpSelectors.charAt(iTarget - 1) == '!')
                return false;
        }
        return true;
    }

    private int parseJumpTarget(int _PC, String Program, String jmpLabel, String jmpSelectors, String jmpGoto) {
        char crtSymbol = m_rts.read();
        int iSearch = 0;

        m_rts.m_symLastTested = crtSymbol;
        if (jmpLabel == null) {
            if (jmpSelectors == null || parseJumpSelectors(crtSymbol, jmpSelectors))
                return parseJumpGotoLabel(_PC, Program, jmpGoto);
            else
                return Program.length();
        }

        jmpLabel = new String("-").concat(jmpLabel).concat("(");

        do {
            int iAux;

            iSearch = Program.indexOf(jmpLabel, iSearch);
            if (iSearch == -1)
                break;
            iSearch += jmpLabel.length();
            iAux = skipToChar(")", iSearch, Program);

            // if the selectors end was found, and the selector matches, and there
            // is a dash after the selectors end, test further.
            if (iAux != -1 && parseJumpSelectors(crtSymbol, Program.substring(iSearch, iAux)) && iAux < Program.length()
                    && Program.charAt(iAux + 1) == '-') {
                iSearch = iAux + 1;
                iAux = skipToAnyButChar("-", iSearch, Program);
                // if there is a label, or a jump end delimiter
                if (iAux != -1) {
                    // if there is a label,
                    if (Program.charAt(iAux) == '<') {
                        iSearch = iAux + 1;
                        iAux = skipToChar(">", iSearch, Program);
                        // if the label ends correctly with '>'
                        if (iAux != -1) {
                            return parseJumpGotoLabel(iAux, Program, Program.substring(iSearch, iAux));
                        }
                    } else if (Program.charAt(iAux) == '>')
                        return iAux + 1;
                }
            }
        } while (true);

        return Program.length();
    }

    private int parseJump(String Program) {
        int _PC;
        String jmpLabel = null;
        String jmpSelectors = null;
        String jmpGoto = null;

        _PC = skipToAnyButChar("-", m_PC, Program);
        if (_PC == -1)
            return markError(-1);

        // parse the Jump label (jmpLabel) first, if any
        if (Program.charAt(_PC) != '(' && Program.charAt(_PC) != '<' && Program.charAt(_PC) != '>') {
            int _BASE = _PC;
            _PC = skipToChar("(-", _BASE, Program);
            if (_PC == -1)
                return markError(_BASE);
            jmpLabel = Program.substring(_BASE, _PC);
        }

        // parse the Jump selectors (jmpSelectors) then, if any
        if (Program.charAt(_PC) == '(') {
            int _BASE = _PC + 1;
            _PC = skipToChar(")", _BASE, Program);
            if (_PC == -1 || _PC == Program.length() - 1 || Program.charAt(_PC + 1) != '-')
                return markError(_PC != -1 ? _PC : _BASE);
            jmpSelectors = Program.substring(_BASE, _PC);
            _PC++;
        } else {
            if (jmpLabel != null)
                return markError(-1);
        }

        // skip the '-' separators; imperatively existant
        _PC = skipToAnyButChar("-", _PC, Program);

        // parse the Jump Goto label (jmpGoto) then, if any
        if (Program.charAt(_PC) == '<') {
            int _BASE = _PC + 1;
            _PC = skipToChar(">", _BASE, Program);
            if (_PC == -1)
                return markError(_BASE);
            jmpGoto = Program.substring(_BASE, _PC);
        }

        // parse the end delimiter
        if (Program.charAt(_PC) == '>') {
            m_PC = parseJumpTarget(_PC, Program, jmpLabel, jmpSelectors, jmpGoto);
            return TuringRTS.STATUS_RUNNING;
        }

        return markError(-1);
    }

    private int parseWrite(String Program) {
        int retCode;
        retCode = m_rts.write(Program.charAt(m_PC++));
        return retCode;
    }

    public TuringAPI(TuringRTS rts) {
        m_rts = rts;
    }

    public void parseProgram(TextArea turingProgram, TuringObserver observer) {
        m_turingProgram = turingProgram;
        m_Observer = observer;
        start();
    }

    public void run() {
        String Program = m_turingProgram.getText();
        char Prog[] = Program.toCharArray();
        int retCode;

        m_PC = m_turingProgram.getSelectionStart();

        retCode = nextStatement(Prog);
        if (retCode != TuringRTS.STATUS_HALTED) {
            switch (Prog[m_PC]) {
            case 'L':
            case 'R':
                retCode = parseMovement(Program);
                break;
            case '{':
                retCode = parseControl(Program);
                break;
            case '-':
                retCode = parseJump(Program);
                break;
            case '@':
                retCode = m_rts.write(m_rts.m_symLastTested);
                m_PC++;
                break;
            default:
                retCode = parseWrite(Program);
                break;
            }
            if (retCode != TuringRTS.STATUS_HALTED) {
                nextStatement(Prog);
                m_turingProgram.select(m_PC, m_PC);
            }
        }
        if (m_Observer != null) {
            m_Observer.turingStatus(this, retCode);
        }
    }
}
