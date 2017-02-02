package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;
import org.usfirst.frc.team2907.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class CameraYTiltCommand extends Command
{
	private double servoPosition;
	public CameraYTiltCommand(double servoInit)
	{
		super("CamYTilt");
		requires(Robot.camera);
		servoPosition = servoInit;
		
	}
	
	protected void initialize()
	{
		Robot.camera.camYTilt.setPosition(servoPosition);
	}
	
	@Override
	protected boolean isFinished()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
