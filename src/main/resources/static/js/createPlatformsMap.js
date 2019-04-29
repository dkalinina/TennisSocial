var mymap;
var markers;
var createdMarker;
var createdMarkers;
var greenIcon = new L.Icon({
    iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});

function createMap() {
    mymap = L.map('mapid').setView([0, 0], 0);
    markers = L.layerGroup().addTo(mymap);
    createdMarkers = L.layerGroup().addTo(mymap);

    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
            '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
            'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id: 'mapbox.streets'
    }).addTo(mymap);
}

function getCityCoords(cities, index) {
    var current_city = cities.find(function(x) {return x.index == index});
    return [current_city.latitude, current_city.longitude];
}

function createPlatformsMarkers(platforms, cities, cityFieldId) {
    var current_city_coords = getCityCoords(cities, $("#"+cityFieldId).val());
    markers.clearLayers();
    var lats = [current_city_coords[0]], longs = [current_city_coords[1]];
    platforms.forEach(function (platform) {
        L.marker([platform.latitude, platform.longitude]).addTo(markers);
        lats.push(platform.latitude);
        longs.push(platform.longitude);
    })

    var lat = lats.reduce(function (a, b) {
        return a + b;
    }) / (platforms.length+1);
    var long = longs.reduce(function (a, b) {
        return a + b;
    }) / (platforms.length+1);
    mymap.setView([lat, long], 10);

}

function recreateNewMarker() {
    createdMarkers.clearLayers();
    position = [$('#latitude').val(), $('#longitude').val()]
    createdMarker = L.marker(position, {draggable: true, icon: greenIcon}).addTo(createdMarkers)
        .on('dragend', function () {
            $('#latitude').val(createdMarker.getLatLng().lat);
            $('#longitude').val(createdMarker.getLatLng().lng);
        });
    mymap.setView(position, 13);
}

function timetableChange(element) {
    var select = "[id='"+$(element).get(0).parentNode.id+"'] [type=time]";

    if (element.value) {
        $(select).attr("disabled", true)
    } else {
        $(select).attr("disabled", false)
    }
}

function toggleButtons() {
    document.getElementById('creationFrm').classList.toggle('unvisible');
    document.getElementById('creationBtn').classList.toggle('unvisible');
}

function preparePlatform() {
    toggleButtons();

    var minZoom = 10;
    var center = mymap.getCenter();
    if (mymap._zoom < minZoom) {
        mymap.setView(center, minZoom);
    }
    $('#latitude').val(center.lat);
    $('#longitude').val(center.lng);
    createdMarkers.clearLayers();
    createdMarker = L.marker(center, {draggable: true, icon: greenIcon}).addTo(createdMarkers)
        .on('dragend', function () {
            $('#latitude').val(createdMarker.getLatLng().lat);
            $('#longitude').val(createdMarker.getLatLng().lng);
        });
}

function createPlatform() {
    toggleButtons();
    L.marker(createdMarker.getLatLng()).addTo(markers);
    createdMarkers.clearLayers();
}