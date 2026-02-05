package ca.sait.crs.services;

import ca.sait.crs.contracts.Course;
import ca.sait.crs.contracts.Registration;
import ca.sait.crs.contracts.RegistrationService;
import ca.sait.crs.contracts.Student;
import ca.sait.crs.exceptions.CannotCreateRegistrationException;

import java.util.ArrayList;

public final class ProxyRegistrationService implements RegistrationService {
	private final RegistrationService realService;

	public ProxyRegistrationService(RegistrationService realService) {
		if (realService == null) {
			throw new IllegalArgumentException("Real registration service cannot be null.");
		}
		this.realService = realService;
	}

	@Override
	public Registration register(Student student, Course course) throws CannotCreateRegistrationException {
		if (student == null || course == null) {
			System.out.println("Registration failed. Missing student or course data.");
			return null;
		}


        //grade validation
		// gpa must be > 2.0
		if (student.getGpa() <= 2.0) {
			System.out.println("Registration blocked. GPA must be greater than 2.0");
			return null;
		}

        //prevents registering twice
        for (Registration reg : realService.getRegistrations()) {
            if (reg.getStudent().getName().equalsIgnoreCase(student.getName()) &&
                    reg.getCourse().getCode().equalsIgnoreCase(course.getCode())) {
                System.out.println("Student \"" + student.getName() + "\" is already registered in \"" + course.getName() + "\" course.");
                return null;
            }
        }

        // if allowed it delegates to real service
		return realService.register(student, course);
	}

	@Override
	public ArrayList<Registration> getRegistrations() {
		return realService.getRegistrations();
	}
}


// TODO: Define the ProxyRegistrationService
// TODO: Implement the RegistrationService interface
// TODO: Check student can be registered before passing to RealRegistrationService
// TODO: Make this class immutable.
