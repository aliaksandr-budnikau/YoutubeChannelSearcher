$(document).ready(function () {
    $.ajax({
        url: "http://localhost:8080/performance"
    }).then(function (data) {
        var speed = {
            y: data.speed,
            type: 'scatter',
            name: 'speed'
        };
        Plotly.newPlot('speed', [speed]);

        var quantity = {
            y: data.quantity,
            type: 'scatter',
            name: 'quantity'
        };
        Plotly.newPlot('quantity', [quantity]);
    });
});