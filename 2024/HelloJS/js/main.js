import { Graph } from "./graph.js"

// html elements
export let hDiv = document.getElementById("hMainDiv")
export let hCanvas = document.getElementById("hMainCanvas")

// custom objects
export let graph = new Graph(hCanvas, hDiv.clientWidth, hDiv.clientHeight)

// hook resize event handlers
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

// hook click event handlers
hCanvas.addEventListener('click', graph.onMouseClickEvent.bind(graph));
hCanvas.addEventListener('contextmenu', graph.onContextMenu.bind(graph));