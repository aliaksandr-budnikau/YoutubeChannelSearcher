$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/performance"
    }).then(function(data) {
        var trace = {
            x: data.x,
            y: data.y,
            type: 'scatter'
        };
        var data = [trace];
        Plotly.newPlot('plot', data);
        $('.greeting-id').append(data);
    });
});