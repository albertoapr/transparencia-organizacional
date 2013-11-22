function converteInt(s) {
	if (!s)
		return 0;
	
	while (s.length > 1 && s.charAt(0) == '0')
		s = s.slice(1);
	
	return parseInt(s);
}

String.prototype.converteLatitude = function() {
	var latitude_parts = this.replace(/'/g, "").split(" ");
	var sgLatitude = (latitude_parts[3] == "N") ? +1 : -1;
	var decLatitude = (converteInt(latitude_parts[0]) + (converteInt(latitude_parts[1]) / 60.0) + (converteInt(latitude_parts[2]) / 3600.0)) * sgLatitude;
	return isNaN(decLatitude) ? 0 : decLatitude;
};

String.prototype.converteLongitude = function() {
	var longitude_parts = this.replace(/'/g, "").split(" ");
	var sgLongitude = (longitude_parts[3] == "E") ? +1 : -1;
	var decLongitude = (converteInt(longitude_parts[0]) + (converteInt(longitude_parts[1]) / 60.0) + (converteInt(longitude_parts[2]) / 3600.0)) * sgLongitude;
	return isNaN(decLongitude) ? 0 : decLongitude;
};

Number.prototype.converteLatitude = function() {
	var hem = (this > 0) ? "N" : "S";
	var value = Math.abs(this);
	var degrees = Math.floor(value);
	value -= degrees;
	var minutes = Math.floor(value * 60);
	value -= minutes / 60;
	var seconds = Math.floor(value * 3600);
	return degrees.toFixed(0) + " " + minutes.toFixed(0) + " " + seconds.toFixed(0) + " " + hem;
};

Number.prototype.converteLongitude = function() {
	var hem = (this > 0) ? "E" : "W";
	var value = Math.abs(this);
	var degrees = Math.floor(value);
	value -= degrees;
	var minutes = Math.floor(value * 60);
	value -= minutes / 60;
	var seconds = Math.floor(value * 3600);
	return degrees.toFixed(0) + " " + minutes.toFixed(0) + " " + seconds.toFixed(0) + " " + hem;
};
