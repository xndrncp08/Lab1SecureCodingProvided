package ca.sait.crs.factories;


import ca.sait.crs.contracts.Course;
import ca.sait.crs.models.RequiredCourse;
import ca.sait.crs.models.OptionalCourse;
import ca.sait.crs.exceptions.CannotCreateCourseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates course instances.
 * @author Nick Hamnett <nick.hamnett@sait.ca>
 * @since June 1, 2023
 */
public class CourseFactory {
	
	/**
	 * Used to store validated entries from the CSV file
	 */
	private static class CourseRecord {
        String name;
        int credits;

        CourseRecord(String name, int credits) {
            this.name = name;
            this.credits = credits;
        }
    }
	
	private static final Map<String, CourseRecord> catalog = new HashMap<>();

	/**
	 * Loads the CSV when class loads
	 */
	static {
        loadCatalog("/courses.csv");
    }
	
    public CourseFactory() {
    }

    /**
     * Load the CSV file data into the catalog HashMap
     * @param path File path to the courses.csv
     */
    private static void loadCatalog(String path) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(CourseFactory.class.getResourceAsStream(path)))) {

            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length != 3) continue;

                String code = parts[0].trim();
                String name = parts[1].trim();
                int credits = Integer.parseInt(parts[2].trim());

                catalog.put(code, new CourseRecord(name, credits));
            }

        } catch (Exception e) {
            System.out.println("Error loading course catalog: " + e.getMessage());
        }
    }
    
    /**
     * Builds a Course instance
     * @param code Course code
     * @param name Name of course
     * @param credits Number of credits for course
     * @return RequiredCourse or OptionalCourse instance
     */
    public Course build(String code, String name, int credits) throws CannotCreateCourseException {
        if (!this.validateCode(code)) {
            throw new CannotCreateCourseException("Course code is invalid.");
        }

        if (!this.validateName(code, name)) {
            throw new CannotCreateCourseException("Course name is invalid.");
        }

        if (!this.validateCredits(code, credits)) {
            throw new CannotCreateCourseException("Course credits is invalid.");
        }
        
        if(credits > 0) {
        	return new RequiredCourse(code, name, credits);
        } else {
        	return new OptionalCourse(code, name, credits);
        }
    }

    /**
     * Checks the course code is valid.
     * @param code Course code
     * @return True if the course code is valid.
     */
    private boolean validateCode(String code) {
        // TODO: Add logic to test code is valid.
    	return catalog.containsKey(code);
    }

    /**
     * Validates course name
     * @param name Course name
     * @return True if course name is valid.
     */
    private boolean validateName(String code, String name) {
        // TODO: Add logic to test name is valid.
    	CourseRecord rec = catalog.get(code);
        return rec != null && rec.name.equalsIgnoreCase(name);
    }

    /**
     * Validates course credits
     * @param credits Course credits
     * @return True if credits value is valid.
     */
    private boolean validateCredits(String code, int credits) {
        // TODO: Add logic to test credits is valid.
    	CourseRecord rec = catalog.get(code);
        return rec != null && rec.credits == credits;
    }
}
