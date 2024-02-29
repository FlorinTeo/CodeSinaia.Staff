import { Graph } from "./graph.js"

// html elements
export let hDiv = document.getElementById("hMainDiv")
export let hCanvas = document.getElementById("hMainCanvas")

// main entry point
export let graph = new Graph(hCanvas, hDiv.clientWidth, hDiv.clientHeight);
graph.repaint();

// #region - graphic functions
// clears the content of the drawing canvas
function canvasClear() {
  let context = hCanvas.getContext("2d");
  context.fillStyle = "white";
  context.fillRect(0, 0, hCanvas.width, hCanvas.height);
}

// resizes the drawing canvas to the given width and height
function canvasResize(width, height) {
    hCanvas.width = width;
    hCanvas.height = height;
}

// draws a line between the given coordinates, in given color
function drawLine(fromX, fromY, toX, toY, color) {
    // draw a  line from the clickedNode to the mouse position
    let context = hCanvas.getContext("2d");
    // if currently hovering over a node, line is black, otherwise is lightgray
    context.strokeStyle = color;
    context.lineWidth = 2;
    context.moveTo(fromX, fromY);
    context.lineTo(toX, toY);
    context.stroke();
}
// #endregion - graphic functions

// #region - UI callback functions
// state variables to control UI actions
let clickedNode = null;
let dragging = false;

// browser resize event handler
const resizeObserver = new ResizeObserver(entries => {
    for(const entry of entries) {
        switch(entry.target.id) {
        case hDiv.id:
            canvasResize(entry.contentRect.width, entry.contentRect.height);
            canvasClear();
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
    canvasClear();
    let color = (graph.getNode(x,y) != null) ? "#000000" : "#CCCCCC";
    drawLine(clickedNode.x, clickedNode.y, x, y, color);
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
      // clicking over an existent node => remove it
      graph.removeNode(droppedNode);
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
  canvasClear();
  graph.repaint();
  // and reset dragging state
  dragging = false;
  clickedNode = null;
});
// #endregion - hook user interface callbacks