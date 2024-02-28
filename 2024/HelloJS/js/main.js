import { Graph } from "./graph.js"

// html elements
export let hDiv = document.getElementById("hMainDiv")
export let hCanvas = document.getElementById("hMainCanvas")

// main entry point
export let graph = new Graph(hCanvas, hDiv.clientWidth, hDiv.clientHeight);
graph.repaint();

// #region - hook user interface callbacks
// browser resize event handlers
const resizeObserver = new ResizeObserver(entries => {
    for(const entry of entries) {
        switch(entry.target.id) {
        case hDiv.id:
            graph.resize(entry.contentRect.width, entry.contentRect.height);
            graph.repaint();
            return;
        }
    }
});
resizeObserver.observe(hDiv);

// mouse click event handler
// hCanvas.addEventListener('mousedown', graph.onMouseDown.bind(graph));
// hCanvas.addEventListener('mousemove', graph.onMouseMove.bind(graph));
// hCanvas.addEventListener('mouseup', graph.onMouseUp.bind(graph));

let clicked = false;
let dragging = false;
let dragFromX = 0;
let dragFromY = 0;

hCanvas.addEventListener('mousedown', (event) => {
  clicked = true;
  dragFromX = event.clientX - hCanvas.offsetLeft;
  dragFromY = event.clientY - hCanvas.offsetTop;
});

hCanvas.addEventListener('mousemove', (event) => {
  if (clicked) {
    let x = event.clientX - hCanvas.offsetLeft;
    let y = event.clientY - hCanvas.offsetTop;
    if (!dragging) {
        dragging = true;
        graph.onDragStart(dragFromX, dragFromY);
    }
    graph.onDrag(x, y);
  }
});

hCanvas.addEventListener('mouseup', (event) => {
  clicked = false;
  let x = event.clientX - hCanvas.offsetLeft;
  let y = event.clientY - hCanvas.offsetTop;
  if (dragging) {
    dragging = false;
    graph.onDragEnd(x, y);
  } else {
    graph.onClick(x, y);
  }
});
  
// #endregion - hook user interface callbacks