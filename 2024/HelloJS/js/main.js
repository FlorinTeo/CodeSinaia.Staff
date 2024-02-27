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
hCanvas.addEventListener('click', graph.onMouseClickEvent.bind(graph));
// mouse right-click event handler
hCanvas.addEventListener('contextmenu', graph.onContextMenu.bind(graph));
// #endregion - hook user interface callbacks