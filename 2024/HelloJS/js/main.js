import { RADIUS } from "./node.js"
import { Graph } from "./graph.js"
import { Graphics } from "./graphics.js"

// html elements
export let hDiv = document.getElementById("hMainDiv");
export let hCanvas = document.getElementById("hMainCanvas");
export let hDebug = document.getElementById("hDebug");

// global objects
export let graphics = new Graphics(hCanvas);
export let graph = new Graph(graphics);

// main entry point
graph.repaint();

// #region - UI callback functions
// state variables to control UI actions
let clickedNode = null;
let dragging = false;

// browser resize event handler
const resizeObserver = new ResizeObserver(entries => {
    for(const entry of entries) {
        switch(entry.target.id) {
        case hDiv.id:
            graphics.resize(entry.contentRect.width, entry.contentRect.height);
            graph.repaint();
            return;
        }
    }
});
resizeObserver.observe(hDiv);

// mouse down event handler
hCanvas.addEventListener('mousedown', (event) => {
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  clickedNode = graph.getNode(x, y);
});

// mouse move event handler
hCanvas.addEventListener('mousemove', (event) => {
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  dragging = (clickedNode != null);
  if (dragging) {
    graphics.clear();
    let hoverNode = graph.getNode(x, y);
    if (hoverNode != null) {
      graphics.drawLine(clickedNode.x, clickedNode.y, hoverNode.x, hoverNode.y, RADIUS, RADIUS, 'black');
    } else {
      graphics.drawLine(clickedNode.x, clickedNode.y, x, y, RADIUS, 0, '#CCCCCC');
    }
    graph.repaint();
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
    } else {
      switch(event.button) {
        case 0: // left-click => remove node
          graph.removeNode(droppedNode);
          break;
        case 1: // middle-click
          break;
        case 2: // right-click
        droppedNode.toggleFill(0);
        break;
      }
    }
  } else {
    // dropped over an empty area
    // check if we were dragging or just clicking
    if (dragging) {
      // dragging over an empty area => move the node clicked at the begining
      clickedNode.x = x;
      clickedNode.y = y;
    } else {
      // clicking over an empty area => create a new node at that location
      graph.addNode(x, y);
    }
  }
  // in all cases repaint the graph
  graphics.clear();
  graph.repaint();
  // and reset dragging state
  dragging = false;
  clickedNode = null;
});

hCanvas.addEventListener('contextmenu', (event) => {
  event.preventDefault();
});

hCanvas.addEventListener('wheel', (event) => {
  event.preventDefault(); 
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  let targetNode = graph.getNode(x, y);
  if (targetNode != null) {
    targetNode.toggleFill(event.deltaY);
    graphics.clear();
    graph.repaint();
  }
});

// hDebug.addEventListener('click', (event) => {
//   console.log('Debug code!');
// });

// #endregion - hook user interface callbacks