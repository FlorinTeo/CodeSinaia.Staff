import { RADIUS } from "./graph/node.js"
import { Graph } from "./graph/graph.js"
import { Queue } from "./queue/queue.js"
import { Graphics } from "./graphics.js"
import { ContextMenu } from "./contextMenu.js"

// html elements
export let hDiv = document.getElementById("hMainDiv");
export let hCanvas = document.getElementById("hMainCanvas");
export let hNodeState = document.getElementById("hNodeState");

// global objects
export let ctxMenuCanvas = new ContextMenu("hCtxMenuCanvas");
export let ctxMenuNode = new ContextMenu("hCtxMenuNode");
export let graphics = new Graphics(hCanvas);
export let graph = new Graph(graphics);
export let queue = new Queue(graphics);

// main entry point
function repaint() {
  graphics.clear();
  graph.repaint();
  queue.repaint();
}

repaint();

// #region - window event handlers
// state variables to control UI actions
let clickedNode = null;
let dragging = false;

// browser resize event handler
const resizeObserver = new ResizeObserver(entries => {
    for(const entry of entries) {
        switch(entry.target.id) {
        case hDiv.id:
            graphics.resize(entry.contentRect.width, entry.contentRect.height);
            repaint();
            return;
        }
    }
});
resizeObserver.observe(hDiv);
// #endregion - window event handlers

// #region - mouse event handlers
// mouse down event handler
// retain the target node (if any) at the beginning of a click or drag action
hCanvas.addEventListener('mousedown', (event) => {
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  clickedNode = graph.getNode(x, y);
});

// mouse move event handler
// draw the guide line from the target node (if any) to the mouse position
hCanvas.addEventListener('mousemove', (event) => {
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  let hoverNode = graph.getNode(x, y);
  dragging = (event.button == 0) && (clickedNode != null);
  if (dragging) {
    repaint();
    if (hoverNode != null) {
      graphics.drawLine(clickedNode.x, clickedNode.y, hoverNode.x, hoverNode.y, RADIUS, RADIUS, 'black');
    } else {
      graphics.drawLine(clickedNode.x, clickedNode.y, x, y, RADIUS, 0, '#CCCCCC');
    }
  } else {
    hNodeState.innerHTML = (hoverNode != null) ? hoverNode.toString() : '';
  }
});

// mouse up event handler
hCanvas.addEventListener('mouseup', (event) => {
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  let droppedNode = graph.getNode(x, y);
 
  // check if  we're dropping over an existent node
  if(droppedNode != null) {
    // dropped over an existent node
    // check if we were dragging or just clicking
    if (dragging) {
      // dragging over an existent node => reset edge from clickedNode to droppedNode
      graph.resetEdge(clickedNode, droppedNode)
    } else if (event.button == 0) { // left-click => remove node
      queue.clear();
      graph.removeNode(droppedNode);
    }
  } else {
    // dropped over an empty area
    // check if we were dragging or just clicking
    if (dragging) {
      // dragging over an empty area => move the node clicked at the begining
      clickedNode.x = x;
      clickedNode.y = y;
    } else if (event.button == 0) { // left-click => add node
      graph.addNode(x, y);
    }
  }
  // in all cases repaint the graph
  repaint();
  // and reset dragging state
  dragging = false;
  clickedNode = null;
});

// wheel action event
// when targeting a node resets, wheel-up resets the node's state color
// to default light gray, wheel-down rotates through different state colors
hCanvas.addEventListener('wheel', (event) => {
  event.preventDefault(); 
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  let targetNode = graph.getNode(x, y);
  if (targetNode != null) {
    if (event.ctrlKey) {
      let prevLabel = graph.reLabel(targetNode, event.deltaY);
      prevLabel = queue.reLabel(prevLabel, targetNode.label);
    } else {
      targetNode.toggleFill(event.deltaY);
    }
    repaint();
  }
});
// #endregion - mouse event handlers

// #region - context menu handlers
// When right-click on a node, open the context menu options 
hCanvas.addEventListener('contextmenu', (event) => {
  event.preventDefault();
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  clickedNode = graph.getNode(x, y);
  if (clickedNode != null) {
    // customize and show hCtxMenuNode
    ctxMenuNode.setInput('hCtxMenuNode_State', clickedNode.state); // Hello, there!
    ctxMenuNode.setVisible(new Map([
      ['hCtxMenuNode_Dequeue', clickedNode.label == queue.peek()],
    ]));
    ctxMenuNode.show(event.pageX-10, event.pageY-10, () => { clickedNode = null; } );
  } else {
    // customize and show hCtxMenuCanvas
    ctxMenuCanvas.setInput('hCtxMenuCanvas_ResetS', 0);
    ctxMenuCanvas.setVisible(new Map([
      ['hCtxMenuCanvas_ResetS', graph.nodes.size > 0 && !graph.matchAll((node) => { return node.state == 0; })],
      ['hCtxMenuCanvas_ResetC', graph.nodes.size > 0 && !graph.matchAll((node) => { return node.fillIndex == 0; })],
      ['hCtxMenuCanvas_ResetQ', queue.size > 0],
      ['hCtxMenuCanvas_ResetG', graph.nodes.size > 0],
    ]));
    ctxMenuCanvas.show(event.pageX-10, event.pageY-10);
  }
});

// #region - Canvas context menu handlers
ctxMenuCanvas.addContextMenuListener('hCtxMenuCanvas_ResetS', (_, value) => {
  graph.traverse((node) => { node.state = value; });
});

ctxMenuCanvas.addContextMenuListener('hCtxMenuCanvas_ResetC', () => {
  graph.traverse((node)=>{ node.fillIndex=0; });
  repaint();
});

ctxMenuCanvas.addContextMenuListener('hCtxMenuCanvas_ResetQ', () => {
  queue.clear();
  repaint();
});

ctxMenuCanvas.addContextMenuListener('hCtxMenuCanvas_ResetG', () => {
  graph.clear();
  queue.clear();
  repaint();
});
// #endregion - Canvas context menu handlers

// #region - Node context menu handlers
ctxMenuNode.addContextMenuListener('hCtxMenuNode_State', (_, value) => {
  clickedNode.state = value;
  hNodeState.innerHTML = clickedNode.toString();
});

ctxMenuNode.addContextMenuListener('hCtxMenuNode_Enqueue', () => {
  queue.enqueue(clickedNode.label);
  repaint();
});

ctxMenuNode.addContextMenuListener('hCtxMenuNode_Dequeue', () => {
  queue.dequeue(clickedNode.label);
  repaint();
});
// #endregion - Node context menu handlers
// #endregion - context menu handlers
