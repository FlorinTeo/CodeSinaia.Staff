import { RADIUS } from "./node.js"
import { Graph } from "./graph.js"
import { Graphics } from "./graphics.js"

// html elements
export let hDiv = document.getElementById("hMainDiv");
export let hCanvas = document.getElementById("hMainCanvas");
export let hDebug = document.getElementById("hDebug");
export let hCtxMenu = document.getElementById("hCtxMenu");
export let hCtxMenu_Enqueue = document.getElementById("hCtxMenu_Enqueue");
export let hCtxMenu_Dequeue = document.getElementById("hCtxMenu_Dequeue");
export let hCtxMenu_Reset = document.getElementById("hCtxMenu_Reset");


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
    } else if (event.button == 0) { // left-click => remove node
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
  graphics.clear();
  graph.repaint();
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
    targetNode.toggleFill(event.deltaY);
    graphics.clear();
    graph.repaint();
  }
});

// context menu action
// When right-click on a node, open the context menu options 
hCanvas.addEventListener('contextmenu', (event) => {
  event.preventDefault();
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  let targetNode = graph.getNode(x, y);
  if (targetNode != null) {
    hCtxMenu_Dequeue.style.display = "block";
    hCtxMenu_Enqueue.style.display = "block";
    hCtxMenu_Reset.style.display = "none";
  } else {
    hCtxMenu_Dequeue.style.display = "none";
    hCtxMenu_Enqueue.style.display = "none";
    hCtxMenu_Reset.style.display = "block";
  }
  hCtxMenu.style.left=`${event.pageX-4}px`;
  hCtxMenu.style.top = `${event.pageY-10}px`;
  hCtxMenu.style.display = "block";
});

// #region - context menu handlers
// When leaving context menu area, hide the menu
hCtxMenu.addEventListener('mouseleave', (event) => {
  hCtxMenu.style.display = "none";
});

// Prevent default context menu when right-click-ing on the menu itself
hCtxMenu.addEventListener('contextmenu', (event) => {
  event.preventDefault();
});

// Handler for 'Enqueue' menu click
hCtxMenu_Enqueue.addEventListener('click', (event) => {
  //alert('Not implemented yet!');
  hCtxMenu.style.display = "none";
});

// Handler for 'Dequeue' menu click
hCtxMenu_Dequeue.addEventListener('click', (event) => {
  //alert('Not implemented yet!');
  hCtxMenu.style.display = "none";
});

// Handler for 'Reset' menu click
hCtxMenu_Reset.addEventListener('click', (event) => {
  //alert('Not implemented yet!');
  graph.traverse((node)=>{
    node.fillIndex=0;
  });
  graph.repaint();
  hCtxMenu.style.display = "none";
});
// #endregion - context menu management

// #endregion - hook user interface callbacks