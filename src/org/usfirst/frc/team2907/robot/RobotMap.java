package org.usfirst.frc.team2907.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static final int TALON_LEFT_1 = 1;
	public static final int TALON_LEFT_2 = 0;  // todo
	public static final int TALON_RIGHT_1 = 4;
	public static final int TALON_RIGHT_2 = 2; // todo
	public static final int TALON_CLIMB_1 = 3;
	public static final int TALON_CLIMB_2 = 2;
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
