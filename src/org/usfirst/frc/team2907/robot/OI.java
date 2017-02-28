package org.usfirst.frc.team2907.robot;

import org.usfirst.frc.team2907.robot.commands.ShiftCommand;
import org.usfirst.frc.team2907.robot.commands.TurnServoCommand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick driveStick = new Joystick(0);
	public JoystickButton shiftButton = new JoystickButton(driveStick, 1);
	
	public OI()
	{
		shiftButton.whenPressed(new TurnServoCommand());
	}
}
