import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import graphics.MazeCanvas;
import graphics.MazeCanvas.Side;

public class Snake extends Explorer {

    public Snake(MazeCanvas mc, Maze m) {
        super(mc, m, Color.YELLOW, Color.RED);
    }
    
    @Override
    protected boolean onEnterCell(Cell cell, Side side) {
        super.onEnterCell(cell, side);
        _mc.step(10);
        cell.removeWall(side);
        return false;
    }
    
    @Override
    protected ArrayList<Side> onGetNextSteps(Cell cell) {
        ArrayList<Side> sides = new ArrayList<Side>();
        Collections.addAll(sides, new Side[] { Side.Bottom, Side.Top, Side.Right, Side.Left});
        _mc.step(10);
        return sides;
    }
    
    @Override
    protected void onStepForward(Cell cell, Side side) {
        super.onStepForward(cell, side);
        cell.removeWall(side);
    }
    
    @Override
    protected void onExitCell(boolean done, Cell cell, Side side) {
        super.onExitCell(done, cell, side);
        if (cell.getRow()==0 || cell.getRow()==_mc.getRows()-1) {
            _mc.drawCenter(cell.getRow(), cell.getCol(), Color.RED.darker());
        }
    }

}
