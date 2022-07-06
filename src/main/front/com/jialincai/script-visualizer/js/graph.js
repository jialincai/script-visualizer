// Global d3 variables
var canvas = d3.select("#network"),
  width = canvas.attr("width"),
  height = canvas.attr("height"),
  maxNodeSize = 50,
  maxLinkWidth = 20,
  ctx = canvas.node().getContext("2d"),
  simulation = d3.forceSimulation()
    .force("x", d3.forceX(width/2))
    .force("y", d3.forceY(height/2))
    .force("collide", d3.forceCollide().radius(d => d.size + maxNodeSize * 0.75))
    .force("charge", d3.forceManyBody()
      .strength(-20))
    .force("link", d3.forceLink()
      .id(function (d) { return d.name; }));

// Links scene script selection to filePath
var dropdownMenu = document.getElementById("scriptSelect");
loadData();
dropdownMenu.oninput = loadData;

function loadData() {
    d3.json(dropdownMenu.value, function (err, graph) {
        if (err) throw err;

        // Adds nodes and edges to graph simulation
        normalizeNodeSize(graph.nodes);
        normalizeLinkSize(graph.links);
        simulation.nodes(graph.nodes);
        simulation.force("link")
                  .links(graph.links);

        // Links slider to min edge weight threshold.
        var slider = document.getElementById("myRange");
        slider.oninput = function() {
            update();
        }

        // Reheat the simulation upon loading new data
        simulation.alphaTarget(25).restart();
        simulation.tick(150);
        simulation.alphaTarget(0);

        simulation.on("tick", update);
      
        function update() {
          graph.nodes.forEach(fixBound);
          ctx.clearRect(0, 0, width, height);
      
          ctx.globalAlpha = 0.5;
          graph.links.forEach(link => drawLink(link, slider.value));
      
          ctx.globalAlpha = 1.0;
          graph.nodes.forEach(drawNode);
        }

        canvas
        .call(d3.drag()
            .container(canvas.node())
            .subject(dragsubject)
            .on("start", dragstarted)
            .on("drag", dragged)
            .on("end", dragended));
      });
}

function dragsubject() {
    return simulation.find(d3.event.x, d3.event.y)
}

function fixBound(d) {
var offset = maxNodeSize * 1;
if      (d.x > width - offset) {d.x = width - offset}
else if (d.x < offset)     {d.x = offset}
if      (d.y > height - offset) {d.y = height - offset}
else if (d.y < offset)     {d.y = offset}
}

function dragstarted() {
  if (!d3.event.active) simulation.alphaTarget(0.3).restart();
  d3.event.subject.fx = d3.event.subject.x;
  d3.event.subject.fy = d3.event.subject.y;
  console.log(d3.event.subject);
}

function dragged() {
  d3.event.subject.fx = d3.event.x;
  d3.event.subject.fy = d3.event.y;
}

function dragended() {
  if (!d3.event.active) simulation.alphaTarget(0);
  d3.event.subject.fx = null;
  d3.event.subject.fy = null;
}

function normalizeNodeSize(nodeArray) {
  const maxCount = d3.max(nodeArray, d => +d.count);

  const radiusScale = d3.scaleSqrt()
      .domain([0, maxCount])
      .range([0, maxNodeSize]);

  const myNodes = nodeArray.map(d => {
      d.size = radiusScale(+d.count);
  });
}

function normalizeLinkSize(linkArray) {
  const maxCount = d3.max(linkArray, l => +l.weight);
  const maxSize = 20;

  const radiusScale = d3.scaleSqrt()
      .domain([0, maxCount])
      .range([0, maxLinkWidth]);

  const myLinks = linkArray.map(l => {
      l.width = radiusScale(+l.weight);
  });

  // Set the maximum slider value max edge weight.
  window.onload = function() {
    document.getElementById("myRange").max=maxCount;
  } 
}

function drawNode(d) {
  ctx.beginPath();
  ctx.fillStyle = "gray"
  ctx.moveTo(d.x, d.y);
  ctx.arc(d.x, d.y, d.size, 0, Math.PI*2);
  ctx.fill();
  ctx.fillStyle = "black"
  ctx.fillText(d.name, d.x, d.y);
}

function drawLink(l, threshold) {
  if (l.weight >= threshold) {
    ctx.beginPath();
    ctx.lineWidth = l.width;
    ctx.strokeStyle = strokeColor(l.sentiment) ;
    ctx.moveTo(l.source.x, l.source.y);
    ctx.lineTo(l.target.x, l.target.y);
    ctx.stroke();
  }
    
  function strokeColor(sentiment) {
    if (sentiment < 0) {
      return "red";
    } else return "green";
  }
}