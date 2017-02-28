package org.usfirst.frc.team2907.robot.commands;

import org.usfirst.frc.team2907.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TurnServoCommand extends Command
{
	public TurnServoCommand()
	{
		super("TurnServoCommand");
		requires(Robot.cameraManager);
		
	}
	
	public void initialize()
	{
		Robot.cameraManager.turnServo();
	}

	@Override
	protected boolean isFinished()
	{
		return true;
	}
}
