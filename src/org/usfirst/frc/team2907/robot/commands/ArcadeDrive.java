package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArcadeDrive extends Command
{
	
	public ArcadeDrive()
	{
		requires(Robot.driveTrain);
	}
	
	protected void initialize()
	{
	}
	
	protected void execute()
	{
		Robot.driveTrain.arcadeDrive(Robot.oi.driveStick.getAxis(AxisType.kY), -Robot.oi.driveStick.getRawAxis(4));
		
		double accelX = Math.abs(Robot.driveTrain.getSensorBoard().getWorldLinearAccelX());
		double accelY = Math.abs(Robot.driveTrain.getSensorBoard().getWorldLinearAccelY());
		
		//System.out.println("accel x : " + accelX + ", accel y : " + accelY);
		
		accelX = (accelX < .6) ? 0 : 1;
		accelY = (accelY < .6) ? 0 : 1;
		
		Robot.oi.driveStick.setRumble(RumbleType.kLeftRumble, accelY);
		Robot.oi.driveStick.setRumble(RumbleType.kRightRumble, accelX);
	}
	
	protected boolean isFinished()
	{
		return false;
	}
	
	protected void end()
	{
		Robot.driveTrain.arcadeDrive(0, 0);
	}
	
	protected void interrupted()
	{
		end();
	}
}
