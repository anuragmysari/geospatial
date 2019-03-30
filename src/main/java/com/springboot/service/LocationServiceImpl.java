package com.springboot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.springboot.mapper.LocationMapper;
import com.springboot.model.Coordinates;

@Configuration
public class LocationServiceImpl implements LocationService {

	@Autowired
	LocationMapper locationMapper;

	@Override
	public List<String> getState(double longitude, double latitude) {

		List<String> locations = new ArrayList<>();
		for (Entry<String, List<Double[]>> entry : LocationMapper.getMap().entrySet()) {
			Coordinates p = new Coordinates(longitude, latitude);
			if (isLocatedinState(p, entry.getValue())) {
				locations.add(entry.getKey());
			}
		}
		return locations;
	}

	// Returns true if the coordinate p lies inside the coordinates[] with n vertices
	private boolean isLocatedinState(Coordinates p, List<Double[]> list) {

		// There must be at least 3 vertices in coordinates[]
		if (list.size() < 3)
			return false;

		// Create a coordinate for line segment from p to 180(max value)
		Coordinates extreme = new Coordinates(180, p.getLatitude());

		// Count intersections of the above line with sides of coordinates
		int count = 0;
		int i = 0;
		do {
			int next = (i + 1) % list.size();

			// Check if the line segment from 'p' to 'extreme' intersects
			// with the line segment from 'coordinates(i)' to 'coordinates(next)'
			Coordinates coordinate1 = new Coordinates(list.get(i)[0], list.get(i)[1]);
			Coordinates coordinate2 = new Coordinates(list.get(next)[0], list.get(next)[1]);
			if (doIntersect(coordinate1, coordinate2, p, extreme)) {
				// If the coordinate 'p' is colinear with line segment 'i-next',
				// then check if it lies on segment. If it lies, return true,
				// otherwise false
				if (orientation(coordinate1, p, coordinate2) == 0)
					return onSegment(coordinate1, p, coordinate2);

				count++;
			}
			i = next;
		} while (i != 0);

		// Return true if count is odd, false otherwise
		return count % 2 == 1;
	}

	// Given three colinear coordinates p, q, r, the function checks if
	// coordinate q lies on line segment 'pr'
	boolean onSegment(Coordinates p, Coordinates q, Coordinates r) {
		return (q.getLongitude() <= Math.max(p.getLongitude(), r.getLongitude())
				&& q.getLongitude() >= Math.min(p.getLongitude(), r.getLongitude())
				&& q.getLatitude() <= Math.max(p.getLatitude(), r.getLatitude())
				&& q.getLatitude() >= Math.min(p.getLatitude(), r.getLatitude()));

	}

	// To find orientation of ordered triplet (p, q, r).
	// The function returns following values
	// 0 --> p, q and r are colinear
	// 1 --> Clockwise
	// 2 --> Counterclockwise
	int orientation(Coordinates p, Coordinates q, Coordinates r) {
		int val = (int) ((q.getLatitude() - p.getLatitude()) * (r.getLongitude() - q.getLongitude())
				- (q.getLongitude() - p.getLongitude()) * (r.getLatitude() - q.getLatitude()));

		if (val == 0)
			return 0; // colinear
		return (val > 0) ? 1 : 2; // clock or counterclock wise
	}

	boolean doIntersect(Coordinates p1, Coordinates q1, Coordinates p2, Coordinates q2) {
		// Find the four orientations needed for general and
		// special cases
		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);

		// General case
		if (o1 != o2 && o3 != o4)
			return true;

		// Special Cases
		// p1, q1 and p2 are colinear and p2 lies on segment p1q1
		if (o1 == 0 && onSegment(p1, p2, q1))
			return true;

		// p1, q1 and p2 are colinear and q2 lies on segment p1q1
		if (o2 == 0 && onSegment(p1, q2, q1))
			return true;

		// p2, q2 and p1 are colinear and p1 lies on segment p2q2
		if (o3 == 0 && onSegment(p2, p1, q2))
			return true;

		// p2, q2 and q1 are colinear and q1 lies on segment p2q2
		if (o4 == 0 && onSegment(p2, q1, q2))
			return true;

		return false; // Doesn't fall in any of the above cases
	}

}